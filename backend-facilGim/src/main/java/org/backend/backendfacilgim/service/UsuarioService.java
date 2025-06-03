package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.entity.Usuario;
import org.backend.backendfacilgim.exception.CustomException;

import java.util.List;

/**
 * Servicio para la gestión de usuarios en la aplicación.
 * <p>
 * Definición de operaciones CRUD y validaciones específicas
 * para la entidad {@link Usuario}.
 * </p>
 *
 * @author Francisco Santana
 */
public interface UsuarioService {

    // GET

    /**
     * Retorna un usuario por su ID.
     *
     * @param usuarioId ID del usuario buscado.
     * @return Objeto {@link Usuario} correspondiente al ID.
     * @throws CustomException si no se encuentra ningún usuario con ese ID.
     */
    Usuario getUsuario(Integer usuarioId);

    /**
     * Lista todos los usuarios registrados en el sistema.
     *
     * @return Lista con todos los objetos {@link Usuario}.
     */
    List<Usuario> listarUsuarios();

    /**
     * Busca un usuario por su nombre de usuario (username).
     *
     * @param username Nombre de usuario a buscar.
     * @return Objeto {@link Usuario} si existe.
     * @throws CustomException si no se encuentra ningún usuario con ese username.
     */
    Usuario obtenerUsuarioPorUsername(String username);

    /**
     * Actualiza la contraseña de un usuario a partir de su ID.
     * <p>
     * Se encarga de encriptar la nueva contraseña y persistir el cambio.
     * </p>
     *
     * @param id                 ID del usuario cuya contraseña se actualizará.
     * @param nuevaContraseña    Nueva contraseña en texto plano.
     * @return Objeto {@link Usuario} con la contraseña actualizada.
     * @throws CustomException   si el usuario no existe o la actualización falla.
     */
    Usuario actualizarContraseña(Integer id, String nuevaContraseña);

    /**
     * Verifica si existe un usuario con el username especificado.
     *
     * @param username Nombre de usuario a comprobar.
     * @return {@code true} si ya existe un {@link Usuario} registrado con ese username, {@code false} en caso contrario.
     */
    boolean existePorUsername(String username);

    /**
     * Verifica si ya existe al menos un usuario con rol de ADMIN en la base de datos.
     *
     * @return {@code true} si hay al menos un administrador, {@code false} en caso contrario.
     */
    boolean existeAdmin();

    // POST

    /**
     * Crea un nuevo usuario a partir de un objeto {@link Usuario} recibido.
     * <p>
     * Se encarga de asignar roles según se indique y encriptar la contraseña antes de guardar.
     * </p>
     *
     * @param usuario Objeto {@link Usuario} con los datos a persistir.
     * @return El {@link Usuario} recién creado, con ID asignado y roles actualizados.
     * @throws CustomException si ya existe un usuario con el mismo username o correo.
     */
    Usuario crearUsuario(Usuario usuario);

    // PUT

    /**
     * Actualiza los datos de un usuario existente identificado por su ID.
     *
     * @param usuarioId  ID del {@link Usuario} a actualizar.
     * @param datosNuevos Objeto {@link Usuario} con los nuevos datos (username, correo, nombre, apellido, etc.).
     * @return El {@link Usuario} actualizado.
     * @throws CustomException si no existe el usuario o la actualización falla.
     */
    Usuario actualizarUsuario(Integer usuarioId, Usuario datosNuevos);

    /**
     * Actualiza los datos de un usuario existente identificado por su nombre de usuario.
     *
     * @param username   Nombre de usuario del {@link Usuario} a actualizar.
     * @param datosNuevos Objeto {@link Usuario} con los nuevos datos.
     * @return El {@link Usuario} actualizado.
     * @throws CustomException si no existe el usuario con ese username o la actualización falla.
     */
    Usuario actualizarUsuarioPorUsuario(String username, Usuario datosNuevos);

    // DELETE

    /**
     * Elimina un usuario del sistema a partir de su ID.
     *
     * @param id ID del {@link Usuario} a eliminar.
     * @throws CustomException si no existe el usuario o la eliminación falla.
     */
    void eliminarUsuario(Integer id);

    /**
     * Elimina un usuario del sistema a partir de su nombre de usuario.
     *
     * @param username Nombre de usuario del {@link Usuario} a eliminar.
     * @throws CustomException si no existe el usuario o la eliminación falla.
     */
    void eliminarUsuarioPorUsername(String username);

    /**
     * Valida que la contraseña proporcionada coincida con la almacenada para el usuario indicado.
     *
     * @param username Nombre de usuario del {@link Usuario}.
     * @param password Contraseña en texto plano a validar.
     * @return {@code true} si la contraseña coincide, {@code false} en caso contrario.
     */
    boolean validarPasswordPorUsername(String username, String password);
}
