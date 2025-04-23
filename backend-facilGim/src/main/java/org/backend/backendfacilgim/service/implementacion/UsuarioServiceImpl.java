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

    @Override
    public Usuario getUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new CustomException("No se ha encontrado el usuario por id: " + usuarioId));
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

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

    @Override
    public Usuario actualizarUsuario(Integer usuarioId, Usuario datosNuevos) {
        Usuario actual = getUsuario(usuarioId);
        comprobarDuplicidadUsuarioYCorreo(usuarioId, datosNuevos.getUsername(), datosNuevos.getCorreo());
        return usuarioRepository.save(actualizarUsuarioAux(actual, datosNuevos));
    }

    @Override
    public Usuario actualizarUsuarioPorUsuario(String username, Usuario datosNuevos) {
        Usuario actual = obtenerUsuarioPorUsername(username);
        comprobarDuplicidadUsuarioYCorreo(actual.getIdUsuario(), datosNuevos.getUsername(), datosNuevos.getCorreo());
        return usuarioRepository.save(actualizarUsuarioAux(actual, datosNuevos));
    }

    @Override
    public void eliminarUsuario(Integer id) {
        usuarioRepository.delete(getUsuario(id));
    }

    @Override
    public void eliminarUsuarioPorUsername(String username) {
        Usuario usuario = obtenerUsuarioPorUsername(username);
        usuarioRepository.delete(usuario);
    }

    @Override
    public boolean existePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    public boolean existeAdmin() {
        return usuarioRepository.existsByRoles_Name("ROLE_ADMIN");
    }

    @Override
    public Usuario obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("No se encontró el usuario con el username " + username));
    }

    private boolean esAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated()
                && auth.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"));
    }

    private Usuario actualizarUsuarioAux(Usuario existente, Usuario nuevo) {
        existente.setUsername(nuevo.getUsername());
        existente.setPassword(passwordEncoder.encode(nuevo.getPassword()));
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
