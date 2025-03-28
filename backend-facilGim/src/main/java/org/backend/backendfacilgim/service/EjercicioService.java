package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.entity.Ejercicio;

import java.util.List;

public interface EjercicioService {

    //GET

    /**
     * Retorna un Ejercicio por el Id;
     * @param ejercicioId
     * @return Un objeto Ejercicio
     */
    Ejercicio getEjercicio(int ejercicioId);


    /**
     * Retorna un ejercicio buscandolo por el nombre del ejercicio
     * @param nombreEjercicio
     * @return Un objeto Ejercicio
     */
    Ejercicio getEjercicio(String nombreEjercicio);

    /**
     *
     * @return
     */
    List<Ejercicio> listarEjercicios();
    //POST



    //PUT

    //DELETE
}
