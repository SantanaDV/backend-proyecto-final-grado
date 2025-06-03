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

/**
 * Implementación de {@link UsuarioService} que gestiona la lógica de negocio
 * relacionada con la entidad {@link Usuario}, incluyendo creación, actualización,
 * eliminación y validación de credenciales. Se encarga de asignar roles y
 * cifrar contraseñas mediante {@link PasswordEncoder}.
 *
 * Autor: Francisco Santana
 */
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
     * Obtiene un {@link Usuario} a partir de su ID.
     *
     * @param usuarioId ID del usuario a buscar.
     * @return Objeto {@link Usuario} si existe.
     * @throws CustomException si no se encuentra el usuario.
     */
    @Override
    public Usuario getUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new CustomException("No se ha encontrado el usuario por id: " + usuarioId));
    }

    /**
     * Lista todos los usuarios registrados en la base de datos.
     *
     * @return Lista de objetos {@link Usuario}.
     */
    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Crea un nuevo {@link Usuario}. Valida duplicidad de username y correo,
     * asigna roles (ROLE_USER y, si no existe administrador, ROLE_ADMIN) y
     * cifra la contraseña antes de persistirlo.
     *
     * @param usuario Objeto {@link Usuario} con los datos a almacenar.
     * @return El {@link Usuario} creado y guardado.
     * @throws CustomException si el usuario o correo ya están en uso,
     *                         o si un usuario no admin intenta crear un admin.
     */
    @Override
    public Usuario crearUsuario(Usuario usuario) {
        comprobarDuplicidadUsuarioYCorreo(null, usuario.getUsername(), usuario.getCorreo());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean autenticado = auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser");

        List<Role> rolesAsignados = new ArrayList<>();
        roleRepository.findByName("ROLE_USER").ifPresent(rolesAsignados::add);

        if (!existeAdmin()) {
            roleRepository.findByName("ROLE_ADMIN").ifPresent(rolesAsignados::add);
            usuario.setAdmin(true);
        } else {
            if (autenticado) {
                if (!esAdmin() && usuario.isAdmin()) {
                    throw new CustomException("Solo un administrador puede crear otros administradores.");
                } else if (esAdmin() && usuario.isAdmin()) {
                    roleRepository.findByName("ROLE_ADMIN").ifPresent(rolesAsignados::add);
                }
            } else {
                usuario.setAdmin(false);
            }
        }

        usuario.setRoles(rolesAsignados);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza los datos de un {@link Usuario} existente, incluidos
     * username, correo, nombre, apellido, dirección, estado y roles.
     *
     * @param usuarioId  ID del usuario a actualizar.
     * @param datosNuevos Objeto {@link Usuario} con los nuevos valores.
     * @return El {@link Usuario} actualizado y persistido.
     * @throws CustomException si el nuevo username o correo ya están en uso.
     */
    @Override
    public Usuario actualizarUsuario(Integer usuarioId, Usuario datosNuevos) {
        Usuario actual = getUsuario(usuarioId);
        comprobarDuplicidadUsuarioYCorreo(usuarioId, datosNuevos.getUsername(), datosNuevos.getCorreo());
        return usuarioRepository.save(actualizarUsuarioAux(actual, datosNuevos));
    }

    /**
     * Actualiza un {@link Usuario} identificándolo por su username.
     *
     * @param username    Nombre de usuario para buscar al usuario a actualizar.
     * @param datosNuevos Objeto {@link Usuario} con los nuevos valores.
     * @return El {@link Usuario} actualizado y persistido.
     * @throws CustomException si el nuevo username o correo ya están en uso.
     */
    @Override
    public Usuario actualizarUsuarioPorUsuario(String username, Usuario datosNuevos) {
        Usuario actual = obtenerUsuarioPorUsername(username);
        comprobarDuplicidadUsuarioYCorreo(actual.getIdUsuario(), datosNuevos.getUsername(), datosNuevos.getCorreo());
        return usuarioRepository.save(actualizarUsuarioAux(actual, datosNuevos));
    }

    /**
     * Elimina un {@link Usuario} dado su ID.
     *
     * @param id ID del usuario a eliminar.
     */
    @Override
    public void eliminarUsuario(Integer id) {
        usuarioRepository.delete(getUsuario(id));
    }

    /**
     * Elimina un {@link Usuario} dado su username.
     *
     * @param username Nombre de usuario del registro a eliminar.
     */
    @Override
    public void eliminarUsuarioPorUsername(String username) {
        Usuario usuario = obtenerUsuarioPorUsername(username);
        usuarioRepository.delete(usuario);
    }

    /**
     * Valida que la contraseña en texto plano coincida con la contraseña almacenada
     * (hasheada) para el {@link Usuario} con el username indicado.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña en texto plano a validar.
     * @return true si coincide, false en caso contrario.
     */
    @Override
    public boolean validarPasswordPorUsername(String username, String password) {
        // Obtenemos la entidad usuario desde la base de datos
        Usuario usuario = obtenerUsuarioPorUsername(username);
        // Comparamos la contraseña en plano con la almacenada (hasheada)
        return passwordEncoder.matches(password, usuario.getPassword());
    }

    /**
     * Actualiza la contraseña de un {@link Usuario} y la persiste.
     *
     * @param id              ID del usuario a quien se le cambia la contraseña.
     * @param nuevaContraseña Nueva contraseña en texto plano.
     * @return El {@link Usuario} con la contraseña actualizada.
     */
    @Override
    public Usuario actualizarContraseña(Integer id, String nuevaContraseña) {
        Usuario usuario = getUsuario(id); // Obtener el usuario por ID
        usuario.setPassword(passwordEncoder.encode(nuevaContraseña)); // Cifrar la nueva contraseña
        return usuarioRepository.save(usuario); // Guardar el usuario con la nueva contraseña
    }

    /**
     * Verifica si existe un {@link Usuario} con el username dado.
     *
     * @param username Nombre de usuario a comprobar.
     * @return true si existe, false en caso contrario.
     */
    @Override
    public boolean existePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    /**
     * Verifica si ya existe al menos un usuario con rol ADMIN.
     *
     * @return true si hay un administrador en la base de datos, false de lo contrario.
     */
    @Override
    public boolean existeAdmin() {
        return usuarioRepository.existsByRoles_Name("ROLE_ADMIN");
    }

    /**
     * Obtiene un {@link Usuario} por su username.
     *
     * @param username Nombre de usuario a buscar.
     * @return El {@link Usuario} encontrado.
     * @throws CustomException si no se encuentra el usuario.
     */
    @Override
    public Usuario obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("No se encontró el usuario con el username " + username));
    }

    /**
     * Comprueba si el usuario autenticado en el contexto actual tiene el rol ADMIN.
     *
     * @return true si el usuario autenticado es administrador, false en caso contrario.
     */
    private boolean esAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated()
                && auth.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * Actualiza en memoria los campos de un {@link Usuario} existente con los valores
     * proporcionados en el objeto {@link Usuario} nuevo. No persiste cambios.
     * Se encarga de decidir si la contraseña debe ser cifrada y si asignar roles.
     *
     * @param existente Usuario existente recuperado de la base de datos.
     * @param nuevo     Objeto {@link Usuario} que contiene nuevos valores.
     * @return El objeto {@link Usuario} existente con los campos actualizados.
     */
    private Usuario actualizarUsuarioAux(Usuario existente, Usuario nuevo) {
        existente.setUsername(nuevo.getUsername());

        // — SOLO codificar y setear password si viene no nulo —
        if (nuevo.getPassword() != null) {
            existente.setPassword(passwordEncoder.encode(nuevo.getPassword()));
        }
        // — resto de campos igual que antes —
        existente.setCorreo(nuevo.getCorreo());
        existente.setNombre(nuevo.getNombre());
        existente.setApellido(nuevo.getApellido());
        existente.setDireccion(nuevo.getDireccion());
        existente.setEnabled(nuevo.isEnabled());
        existente.setAdmin(nuevo.isAdmin());

        List<Role> roles = new ArrayList<>();
        roleRepository.findByName("ROLE_USER").ifPresent(roles::add);
        if (nuevo.isAdmin() && esAdmin()) {
            roleRepository.findByName("ROLE_ADMIN").ifPresent(roles::add);
        }
        existente.setRoles(roles);

        return existente;
    }

    /**
     * Verifica la duplicidad de username y correo en la base de datos,
     * considerando el ID opcional para updates (no comparar con sí mismo).
     *
     * @param id       ID del usuario que se está actualizando (o null si es creación).
     * @param username Username que se desea validar.
     * @param correo   Correo que se desea validar.
     * @throws CustomException si el username o correo ya están en uso por otro registro.
     */
    private void comprobarDuplicidadUsuarioYCorreo(Integer id, String username, String correo) {
        usuarioRepository.findByUsername(username).ifPresent(u -> {
            if (id == null || !u.getIdUsuario().equals(id)) {
                throw new CustomException("El username ya se encuentra en uso.");
            }
        });

        usuarioRepository.findByCorreo(correo).ifPresent(u -> {
            if (id == null || !u.getIdUsuario().equals(id)) {
                throw new CustomException("El correo ya se encuentra en uso.");
            }
        });
    }
}
