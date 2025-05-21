package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.dto.EjercicioDTO;
import org.backend.backendfacilgim.dto.SerieDTO;
import org.backend.backendfacilgim.entity.Ejercicio;

import java.util.List;
import java.util.Optional;

public interface EjercicioService {

    // —— Catálogo global ——

    /** Devuelve todos los ejercicios del catálogo */
    List<Ejercicio> listarEjercicios();

    /** Busca un ejercicio por su id */
    Optional<Ejercicio> getEjercicio(Integer ejercicioId);

    /** Crea un ejercicio en el catálogo */
    Ejercicio crearEjercicio(Ejercicio ejercicio);

    /** Actualiza un ejercicio existente */
    Ejercicio actualizarEjercicio(Integer ejercicioId, Ejercicio datosNuevos);

    /** Elimina un ejercicio por su id */
    void eliminarEjercicio(Integer ejercicioId);

    /** Elimina un ejercicio por su nombre, validando propietario */
    void eliminarEjercicioPorNombre(String nombreEjercicio, String username);

    // —— Instancias en entrenamientos ——

    public EjercicioDTO asignarEjercicioConSeriesAEntrenamiento(
            Integer idEntrenamiento,
            Integer idEjercicio,
            List<SerieDTO> seriesDTO,
            Integer orden
    );




    void eliminarInstancia(Integer relId);

    /**
     * Lista los ejercicios (instancias) para un entrenamiento y usuario dados.
     */
    List<EjercicioDTO> listarEjerciciosPorEntrenamientoYUsuario(Integer idEntrenamiento, String username);

    EjercicioDTO actualizarInstanciaConSeries(Integer relId, List<SerieDTO> series, Integer orden);
}
