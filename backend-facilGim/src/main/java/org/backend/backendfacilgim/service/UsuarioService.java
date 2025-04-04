package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.entity.Usuario;

import java.util.List;

public interface UsuarioService {

    //GET

    /**
     * Retorna un usuario por el Id;
     *
     * @param usuarioId
     * @return Un objeto Usuario
     */
    Usuario getUsuario(Integer usuarioId);


    /**
     * Retorna un usuario buscándolo por el nombre
     *
     * @param nombreUsuario
     * @return Un objeto Usuario
     */
    Usuario getUsuarioByUsername(String nombreUsuario);

    /**
     * Lista todos los usuarios
     *
     * @return List<Usuario>
     */
    List<Usuario> listarUsuarios();
    //POST

    /**
     * Retorna un objeto usuario que ha sido introduce mediante un Json
     *
     * @param usuario
     * @return objeto Usuario
     */
    Usuario crearUsuario(Usuario usuario);


    //PUT

    /**
     * Actualiza los datos de un usuario basándonos en su id
     *
     * @param usuarioId
     * @param datosNuevos
     * @return
     */
    Usuario actualizarUsuario(Integer usuarioId, Usuario datosNuevos);

    /**
     * Actualiza los datos de un usuario basándonos en su username
     *
     * @param username
     * @param datosNuevos
     * @return
     */
    Usuario actualizarUsuarioPorUsuario(String username, Usuario datosNuevos);


    //DELETE

    /**
     * Eliminar un usuario por su id
     *
     * @param id
     */
    void eliminarUsuario(Integer id);

    /**
     * Eliminar un usuario por su nombre
     * @param username
     */
    void eliminarUsuarioPorUsername(String username);

    /**
     * Verifica si existe un usuario con el username especificado.
     * @param username El nombre de usuario que se desea comprobar.
     * @return true si ya existe, false si no.
     */
    boolean existePorUsername(String username);





}
