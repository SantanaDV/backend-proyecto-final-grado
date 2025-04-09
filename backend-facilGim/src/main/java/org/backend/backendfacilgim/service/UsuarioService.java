package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.entity.Usuario;
import org.backend.backendfacilgim.exception.CustomException;

import java.util.List;
import java.util.Optional;

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

    /**
     * Verifica si ya existe al menos un usuario con el rol de ADMIN.
     * @return true si hay al menos un ADMIN registrado, false si no.
     */
    boolean existeAdmin();


    /**
     * Busca un usuario en la base de datos a partir de su nombre de usuario (username).
     *
     * @param username Nombre de usuario único registrado en el sistema.
     * @return El objeto Usuario correspondiente al username, si existe.
     * @throws CustomException Si el usuario no es encontrado en la base de datos.
     */
    Usuario obtenerUsuarioPorUsername(String username);


}
