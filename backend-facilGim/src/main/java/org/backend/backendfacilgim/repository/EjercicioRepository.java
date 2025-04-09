package org.backend.backendfacilgim.repository;

import jakarta.validation.constraints.NotBlank;
import org.backend.backendfacilgim.entity.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, Integer> {
    /**
     * Devuelve un ejercicio por su nombre, no permitiendo que el campor nombre este vacio
     * @param nombre
     * @return
     */
    Ejercicio getEjercicioByNombre(String nombre);


    Ejercicio findByNombreAndEntrenamiento_Usuario_Username(String nombre, String username);


    /**
     * Elimina un ejercicio por nombre no permitiendo que el nombre este vacio
     * @param nombre
     */
    void deleteEjercicioByNombre(String nombre);



}
