package org.backend.backendfacilgim.service.implementacion;

import org.backend.backendfacilgim.entity.Role;
import org.backend.backendfacilgim.entity.Usuario;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.repository.RoleRepository;
import org.backend.backendfacilgim.repository.UsuarioRepository;
import org.backend.backendfacilgim.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retorna un usuario a partir de su ID.
     *
     * @param usuarioId el ID del usuario a buscar.
     * @return el usuario encontrado.
     */
    @Override
    public Usuario getUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new CustomException("No se ha encontrado el usuario por id: " + usuarioId));
    }

    /**
     * Retorna un usuario a partir de su username.
     *
     * @param nombreUsuario el username del usuario.
     * @return el usuario encontrado.
     */
    @Override
    public Usuario getUsuarioByUsername(String nombreUsuario) {
        return usuarioRepository.findByUsername(nombreUsuario)
                .orElseThrow(() -> new CustomException("No se ha encontrado el usuario por el nombre: " + nombreUsuario));
    }

    /**
     * Devuelve la lista de todos los usuarios.
     *
     * @return lista de usuarios.
     */
    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Crea un nuevo usuario a partir de la entidad Usuario.
     * Se valida que el username no esté en uso, se verifica si el usuario autenticado tiene
     * privilegios para crear administradores y se encripta la contraseña antes de guardar.
     *
     * @param usuario el objeto Usuario a crear.
     * @return el usuario creado.
     */
    @Override
    public Usuario crearUsuario(Usuario usuario) {
        // Verificar si el username ya está en uso
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new CustomException("El username ya se encuentra en uso.");
        }

        // Obtener la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean autenticado = (authentication != null
                && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser"));


        // Inicializar la lista de roles asignados. Se asigna por defecto ROLE_USER.
        List<Role> rolesAsignados = new ArrayList<>();
        roleRepository.findByName("ROLE_USER").ifPresent(rolesAsignados::add);

        // Si aún no hay admin, el primer usuario se asigna automáticamente como ADMIN.
        if (!existeAdmin()) {
            roleRepository.findByName("ROLE_ADMIN").ifPresent(rolesAsignados::add);
            usuario.setAdmin(true);
        } else {
            // Si ya existen administradores, se debe verificar que únicamente los usuarios
            // autenticados como admin puedan asignar el rol ADMIN.
            if (autenticado) {
                // Si el usuario autenticado NO es admin y se solicita rol admin, se rechaza.
                if (!esAdmin() && usuario.isAdmin()) {
                    throw new CustomException("Solo un administrador puede crear otros administradores.");
                }else if(esAdmin() && usuario.isAdmin()){
                    // Si el usuario logueado es admin y el usuario se marcará como admin, se añade el rol de admin utilizando el repositorio.
                    roleRepository.findByName("ROLE_ADMIN").ifPresent(rolesAsignados::add);
                }
            } else {
                // Si no hay un usuario autenticado, se fuerza a que el usuario no sea admin.
                usuario.setAdmin(false);
            }
        }

        // Asignar los roles validados y encriptar la contraseña utilizando BCrypt.
        usuario.setRoles(rolesAsignados);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Guardar y retornar el usuario creado.
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza los datos de un usuario identificado por su ID.
     *
     * @param usuarioId  el ID del usuario a actualizar.
     * @param datosNuevos los nuevos datos del usuario.
     * @return el usuario actualizado.
     */
    @Override
    public Usuario actualizarUsuario(Integer usuarioId, Usuario datosNuevos) {
        return usuarioRepository.save(actualizarUsuarioAux(getUsuario(usuarioId), datosNuevos));
    }

    /**
     * Actualiza los datos de un usuario identificado por su username.
     *
     * @param username    el username del usuario a actualizar.
     * @param datosNuevos los nuevos datos del usuario.
     * @return el usuario actualizado.
     */
    @Override
    public Usuario actualizarUsuarioPorUsuario(String username, Usuario datosNuevos) {
        return usuarioRepository.save(actualizarUsuarioAux(getUsuarioByUsername(username), datosNuevos));
    }

    /**
     * Elimina un usuario a partir de su ID.
     *
     * @param id el ID del usuario a eliminar.
     */
    @Override
    public void eliminarUsuario(Integer id) {
        usuarioRepository.delete(getUsuario(id));
    }

    /**
     * Elimina un usuario a partir de su username.
     *
     * @param username el username del usuario a eliminar.
     */
    @Override
    public void eliminarUsuarioPorUsername(String username) {
        usuarioRepository.delete(usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("El usuario debe existir para poder ser eliminado")));
    }

    /**
     * Verifica la existencia de un usuario a través de su username.
     *
     * @param username el username a buscar.
     * @return true si existe, false en caso contrario.
     */
    @Override
    public boolean existePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }



    /**
     * Verifica si existe al menos un usuario con rol ADMIN en la base de datos.
     *
     * @return true si existe un administrador, false de lo contrario.
     */
    @Override
    public boolean existeAdmin() {
        return usuarioRepository.existsByRoles_Name("ROLE_ADMIN");
    }

    /**
     * Retorna un usuario a partir de su username.
     *
     * @param username el nombre de usuario.
     * @return el usuario encontrado o lanza una excepción si no se encuentra.
     */
    @Override
    public Usuario obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("No se encontró el usuario con el username " + username));
    }

    /**
     * Método auxiliar que determina si el usuario autenticado es administrador.
     * Extraer esta lógica en un método separado permite reutilizarla y centralizarla.
     *
     * @return true si el usuario autenticado tiene el rol "ROLE_ADMIN", false en caso contrario.
     */
    private boolean esAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Si la autenticación es nula, no está autenticado o es el usuario anónimo, retorna false.
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }
    /**
     * Método auxiliar para actualizar los datos de un usuario. Actualiza campos básicos y además,
     * asigna roles según se requiera. Se fuerza siempre la asignación del rol ROLE_USER y se añade ROLE_ADMIN
     * sólo si el usuario autenticado es admin y se solicita.
     *
     * @param usuarioEncontrado el usuario actual que se va a actualizar.
     * @param usuarioDatosNuevos los nuevos datos del usuario.
     * @return el objeto Usuario actualizado.
     */
    private Usuario actualizarUsuarioAux(Usuario usuarioEncontrado, Usuario usuarioDatosNuevos) {
        usuarioEncontrado.setUsername(usuarioDatosNuevos.getUsername());
        usuarioEncontrado.setPassword(passwordEncoder.encode(usuarioDatosNuevos.getPassword()));
        usuarioEncontrado.setCorreo(usuarioDatosNuevos.getCorreo());
        usuarioEncontrado.setNombre(usuarioDatosNuevos.getNombre());
        usuarioEncontrado.setApellido(usuarioDatosNuevos.getApellido());
        usuarioEncontrado.setDireccion(usuarioDatosNuevos.getDireccion());
        usuarioEncontrado.setEnabled(usuarioDatosNuevos.isEnabled());
        usuarioEncontrado.setAdmin(usuarioDatosNuevos.isAdmin());

        // Actualizar los roles asignados: se asigna siempre ROLE_USER. Si se solicita rol ADMIN,
        // sólo se añade si el usuario autenticado es admin.
        List<Role> rolesActualizados = new ArrayList<>();
        roleRepository.findByName("ROLE_USER").ifPresent(rolesActualizados::add);
        if (usuarioDatosNuevos.isAdmin() && esAdmin()) {
            roleRepository.findByName("ROLE_ADMIN").ifPresent(rolesActualizados::add);
        }
        usuarioEncontrado.setRoles(rolesActualizados);

        return usuarioEncontrado;
    }
}
