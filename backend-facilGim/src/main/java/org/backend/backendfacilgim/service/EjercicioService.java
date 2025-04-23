package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.dto.EjercicioDTO;
import org.backend.backendfacilgim.entity.Ejercicio;

import java.util.List;
import java.util.Optional;

public interface EjercicioService {

    Optional<Ejercicio> getEjercicio(Integer ejercicioId);

    Optional<Ejercicio> getEjercicioByNombre(String nombreEjercicio);

    List<Ejercicio> listarEjercicios();

    Ejercicio crearEjercicio(Ejercicio ejercicio);

    Ejercicio actualizarEjercicio(Integer ejercicioId, Ejercicio datosNuevos);

    Ejercicio actualizarEjercicioPorNombre(String nombreEjercicio, Ejercicio datosNuevos, String username);

    void eliminarEjercicio(Integer id);

    void eliminarEjercicioPorNombre(String nombre, String username);

    /**
     * Lista los ejercicios que pertenecen a un entrenamiento y a un usuario,
     * utilizando la entidad intermedia EntrenamientoEjercicio.
     */
    List<EjercicioDTO> listarEjerciciosPorEntrenamientoYUsuario(Integer idEntrenamiento, String username);
}
