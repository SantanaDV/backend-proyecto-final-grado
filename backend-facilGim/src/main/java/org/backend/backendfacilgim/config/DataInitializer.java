package org.backend.backendfacilgim.config;

import org.backend.backendfacilgim.entity.Role;
import org.backend.backendfacilgim.entity.Usuario;
import org.backend.backendfacilgim.repository.RoleRepository;
import org.backend.backendfacilgim.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Inicializa datos esenciales en la base de datos al iniciar la aplicación.
 * <p>
 * Crea los roles {@code ROLE_USER} y {@code ROLE_ADMIN} si no existen,
 * y también crea un usuario administrador con usuario {@code "admin"} si aún no está registrado.
 * </p>
 *
 * Autor: Francisco Santana
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Ejecuta la inicialización de datos al arrancar la aplicación.
     * <p>
     * - Verifica si existe el rol {@code ROLE_USER}; si no existe, lo crea.
     * - Verifica si existe el rol {@code ROLE_ADMIN}; si no existe, lo crea.
     * - Verifica si existe el usuario con nombre {@code "admin"}; si no existe,
     *   crea un usuario con contraseña codificada {@code "admin"}, correo,
     *   nombre y apellido por defecto, y asigna ambos roles (USER y ADMIN).
     * </p>
     *
     * @param args Argumentos de línea de comandos (no se utilizan).
     * @throws Exception Si ocurre algún error durante la inicialización.
     */
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Crear ROLE_USER si no existe
        Role roleUser = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ROLE_USER");
                    return roleRepository.save(r);
                });

        // Crear ROLE_ADMIN si no existe
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ROLE_ADMIN");
                    return roleRepository.save(r);
                });

        // Crear usuario 'admin' si no existe
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            // Codificar la contraseña antes de guardar
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setCorreo("admin@admin.com");
            admin.setNombre("Admin");
            admin.setApellido("Admin");
            // Asignar roles al admin (ROLE_USER y ROLE_ADMIN)
            admin.getRoles().add(roleUser);
            admin.getRoles().add(roleAdmin);
            usuarioRepository.save(admin);
        }
    }
}
