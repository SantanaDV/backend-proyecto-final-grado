package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.entity.Usuario;
import org.backend.backendfacilgim.exception.CustomException;

import java.util.List;

public interface UsuarioService {

    // GET

    /**
     * Retorna un usuario por su ID.
     *
     * @param usuarioId ID del usuario
     * @return Objeto Usuario
     */
    Usuario getUsuario(Integer usuarioId);

    /**
     * Lista todos los usuarios registrados.
     *
     * @return Lista de usuarios
     */
    List<Usuario> listarUsuarios();

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario
     * @return Usuario encontrado
     * @throws CustomException si no se encuentra el usuario
     */
    Usuario obtenerUsuarioPorUsername(String username);

    /**
     * Verifica si existe un usuario con el username especificado.
     *
     * @param username El nombre de usuario a comprobar
     * @return true si ya existe, false si no
     */
    boolean existePorUsername(String username);

    /**
     * Verifica si ya existe al menos un usuario con rol de ADMIN.
     *
     * @return true si hay un admin, false en caso contrario
     */
    boolean existeAdmin();


    // POST

    /**
     * Crea un nuevo usuario a partir de un objeto JSON recibido.
     *
     * @param usuario Objeto Usuario recibido
     * @return Usuario creado
     */
    Usuario crearUsuario(Usuario usuario);


    // PUT

    /**
     * Actualiza los datos de un usuario por su ID.
     *
     * @param usuarioId ID del usuario a actualizar
     * @param datosNuevos Nuevos datos del usuario
     * @return Usuario actualizado
     */
    Usuario actualizarUsuario(Integer usuarioId, Usuario datosNuevos);

    /**
     * Actualiza los datos de un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario
     * @param datosNuevos Nuevos datos
     * @return Usuario actualizado
     */
    Usuario actualizarUsuarioPorUsuario(String username, Usuario datosNuevos);


    // DELETE

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar
     */
    void eliminarUsuario(Integer id);

    /**
     * Elimina un usuario por su nombre de usuario.
     *
     * @param username Username del usuario
     */
    void eliminarUsuarioPorUsername(String username);
}
