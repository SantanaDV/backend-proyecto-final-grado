package org.backend.backendfacilgim.mapper;

import lombok.experimental.UtilityClass;
import org.backend.backendfacilgim.dto.EjercicioDTO;
import org.backend.backendfacilgim.dto.SerieDTO;
import org.backend.backendfacilgim.entity.EntrenamientoEjercicio;
import org.backend.backendfacilgim.entity.Ejercicio;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class EjercicioMapper {

    /**
     * Mapea la combinación de Ejercicio (catálogo) y su instancia en EntrenamientoEjercicio
     * a un DTO que incluye peso y repeticiones específicos de esa sesión.
     */
    public static EjercicioDTO toDTO(Ejercicio ejercicio, EntrenamientoEjercicio entrenamientoEjercicio) {
        // Extraemos las series asociadas
        List<SerieDTO> seriesDTO = entrenamientoEjercicio.getSeries()
                .stream()
                .map(SerieMapper::toDTO)
                .collect(Collectors.toList());

        // Obtenemos peso y repeticiones de la entidad intermedia (no del catálogo global)
        double peso = entrenamientoEjercicio.getPeso();
        int repeticiones = entrenamientoEjercicio.getRepeticiones();

        return new EjercicioDTO(
                ejercicio.getIdEjercicio(),
                ejercicio.getNombre(),
                repeticiones,
                peso,
                ejercicio.getImagenUrl(),
                seriesDTO
        );
    }
}
