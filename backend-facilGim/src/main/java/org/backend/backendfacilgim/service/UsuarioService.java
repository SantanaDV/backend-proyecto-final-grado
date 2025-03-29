package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.entity.Usuario;

import java.util.List;

public interface UsuarioService {

    //GET

    /**
     * Retorna un usuario por el Id;
     *
     * @param ejercicioId
     * @return Un objeto Ejercicio
     */
    Usuario getEjercicio(Integer ejercicioId);


    /**
     * Retorna un usuario buscandolo por el nombre
     *
     * @param nombreEjercicio
     * @return Un objeto Ejercicio
     */
    Usuario getEjercicioByNombre(String nombreEjercicio);

    /**
     * Lista todos los usuario
     *
     * @return List<Ejercicio>
     */
    List<Usuario> listarEjercicios();
    //POST

    /**
     * Retorna un objeto usuario que ha sido introduce mediante un Json
     *
     * @param usuario
     * @return objeto Ejercicio
     */
    Usuario crearEjercicio(Usuario usuario);


    //PUT

    /**
     * Actualiza los datos de un usuario basándonos en su id
     *
     * @param ejercicioId
     * @param datosNuevos
     * @return
     */
    Usuario actualizarEjercicio(Integer ejercicioId, Usuario datosNuevos);

    /**
     * Actualiza los datos de un usuario basándonos en su nombre
     *
     * @param nombreEjercicio
     * @param datosNuevos
     * @return
     */
    Usuario actualizarEjercicioPorNombre(String nombreEjercicio, Usuario datosNuevos);


    //DELETE

    /**
     * Eliminar un usuario por su id
     *
     * @param id
     */
    void eliminarEjercicio(Integer id);

    /**
     * Eliminar un usuario por su nombre
     * @param nombre
     */
    void eliminarEjercicioPorNombre(String nombre);





}
