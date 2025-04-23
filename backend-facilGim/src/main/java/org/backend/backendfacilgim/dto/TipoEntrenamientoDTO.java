package org.backend.backendfacilgim.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoEntrenamientoDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
}
