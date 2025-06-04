package org.backend.backendfacilgim.mapper;

import lombok.experimental.UtilityClass;
import org.backend.backendfacilgim.dto.EjercicioDTO;
import org.backend.backendfacilgim.dto.SerieDTO;
import org.backend.backendfacilgim.entity.EntrenamientoEjercicio;
import org.backend.backendfacilgim.entity.Ejercicio;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper de utilidad para convertir entidades Ejercicio y su relación
 * EntrenamientoEjercicio a DTOs específicos.
 *
 * @autor Francisco Santana
 */
@UtilityClass
public class EjercicioMapper {

    /**
     * Convierte una entidad Ejercicio y su relación EntrenamientoEjercicio
     * en un EjercicioDTO, incluyendo las series asociadas.
     *
     * @param ejercicio            Entidad Ejercicio que contiene información básica
     *                             (id, nombre, URL de imagen).
     * @param entrenamientoEjercicio Relación EntrenamientoEjercicio que contiene las series
     *                              asociadas a este ejercicio dentro de un entrenamiento.
     * @return Un EjercicioDTO con los datos del ejercicio y la lista de SerieDTO correspondientes.
     */
    public static EjercicioDTO toDTO(Ejercicio ejercicio, EntrenamientoEjercicio entrenamientoEjercicio) {
        List<SerieDTO> seriesDTO = entrenamientoEjercicio.getSeries()
                .stream()
                .map(SerieMapper::toDTO)
                .collect(Collectors.toList());

        return new EjercicioDTO(
                ejercicio.getIdEjercicio(),
                ejercicio.getNombre(),
                ejercicio.getImagenUrl(),
                seriesDTO
        );
    }
}
