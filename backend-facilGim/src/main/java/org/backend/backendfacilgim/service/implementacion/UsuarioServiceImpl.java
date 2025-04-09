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
    private PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario getUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId).orElseThrow(() -> new CustomException("No se ha encontrado el ejercicio por id: " + usuarioId));
    }

    @Override
    public Usuario getUsuarioByUsername(String nombreUsuario) {
        return usuarioRepository.findByUsername(nombreUsuario).orElseThrow(() -> new CustomException("No se ha encontrado el ejercicio por el nonbre: " + nombreUsuario));
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        // Verificar si el username ya está en uso
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new CustomException("❌ El username ya se encuentra en uso.");
        }

        //  Verificar si ya existe al menos un ADMIN en la base de datos
        boolean hayAdmin = existeAdmin();

        // Asignar el rol por defecto (ROLE_USER)
        List<Role> rolesAsignados = new ArrayList<>();
        roleRepository.findByName("ROLE_USER").ifPresent(rolesAsignados::add);


        //  Obtener información del usuario autenticado (si existe)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean esAnonimo = (authentication == null || !authentication.isAuthenticated() || authentication.getName().equals("anonymousUser"));

        // Si NO hay administradores, el primer usuario registrado será ADMIN automáticamente
        if (!hayAdmin) {
            roleRepository.findByName("ROLE_ADMIN").ifPresent(rolesAsignados::add);
            usuario.setAdmin(true);
        } else {
            //  Si ya hay admins, verificar que solo un admin pueda crear más admins
            if (esAnonimo) {
                usuario.setAdmin(false); //  Usuario anónimo NO puede ser ADMIN
            } else {
                String usernameAutenticado = authentication.getName();
                Usuario usuarioAutenticado = obtenerUsuarioPorUsername(usernameAutenticado);

                boolean esAdmin = usuarioAutenticado.getRoles().stream()
                        .anyMatch(rol -> rol.getName().equals("ROLE_ADMIN"));

                //  Si el usuario autenticado NO es admin y trata de crear un admin, rechazarlo
                if (!esAdmin && usuario.isAdmin()) {
                    throw new CustomException(" Solo un administrador puede crear otros administradores.");
                }
            }
        }

        // 7️ FORZAR QUE SOLO LOS ADMIN CREADOS POR ADMINISTRADORES TENGAN ESE ROL
        if (!usuario.isAdmin()) {
            usuario.setAdmin(false);
            rolesAsignados.removeIf(rol -> rol.getName().equals("ROLE_ADMIN")); //  Eliminar rol de ADMIN si no corresponde
        }

        // 8️⃣ Asignar los roles validados y encriptar la contraseña
        usuario.setRoles(rolesAsignados);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // 9️⃣ Guardar y devolver el usuario creado
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizarUsuario(Integer usuarioId, Usuario datosNuevos) {
        return actualizarUsuarioAux(getUsuario(usuarioId), datosNuevos);
    }

    @Override
    public Usuario actualizarUsuarioPorUsuario(String username, Usuario datosNuevos) {
        return actualizarUsuarioAux(getUsuarioByUsername(username), datosNuevos);
    }

    @Override
    public void eliminarUsuario(Integer id) {
        usuarioRepository.delete(getUsuario(id));
    }

    @Override
    public void eliminarUsuarioPorUsername(String username) {
        usuarioRepository.delete(usuarioRepository.findByUsername(username).orElseThrow(()-> new CustomException("El usuario debe existir para poder ser eliminado")));
    }

    @Override
    public boolean existePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    private Usuario actualizarUsuarioAux(Usuario usuarioEncontrado, Usuario usuarioDatosNuevos) {
        usuarioEncontrado.setUsername(usuarioDatosNuevos.getUsername());
        usuarioEncontrado.setPassword(passwordEncoder.encode(usuarioDatosNuevos.getPassword()));
        usuarioEncontrado.setCorreo(usuarioDatosNuevos.getCorreo());
        usuarioEncontrado.setNombre(usuarioDatosNuevos.getNombre());
        usuarioEncontrado.setApellido(usuarioDatosNuevos.getApellido());
        usuarioEncontrado.setDireccion(usuarioDatosNuevos.getDireccion());
        usuarioEncontrado.setEnabled(usuarioDatosNuevos.isEnabled());
        usuarioEncontrado.setAdmin(usuarioDatosNuevos.isAdmin());
        return usuarioEncontrado;
    }

    @Override
    public boolean existeAdmin() {
        return usuarioRepository.existsByRoles_Name("ROLE_ADMIN");
    }

    @Override
    public Usuario obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(()-> new CustomException("No se encontro el usuario con el username" +
                " " +username));
    }


}
