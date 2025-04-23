package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.entity.EntrenamientoEjercicio;

import java.util.List;

public interface EntrenamientoEjercicioService {

    EntrenamientoEjercicio crear(EntrenamientoEjercicio entity);
    List<EntrenamientoEjercicio> obtenerTodos();

    List<EntrenamientoEjercicio> obtenerPorIdEntrenamiento(Integer idEntrenamiento);

    EntrenamientoEjercicio obtenerPorId(Integer id);

    void eliminarPorId(Integer id);
}
