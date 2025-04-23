package org.backend.backendfacilgim.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "entrenamiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entrenamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrenamiento")
    private Integer idEntrenamiento;

    @NotBlank(message = "El nombre del entrenamiento es obligatorio")
    private String nombre;

    @NotNull(message = "La fecha del entrenamiento es obligatoria")
    private LocalDate fechaEntrenamiento;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private int duracion;

    @ManyToOne
    @JoinColumn(name = "id_tipo_entrenamiento", nullable = false)
    @NotNull(message = "El tipo de entrenamiento es obligatorio")
    private TipoEntrenamiento tipoEntrenamiento;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnoreProperties("entrenamientos")
    @NotNull(message = "El usuario es obligatorio")
    private Usuario usuario;

    @OneToMany(mappedBy = "entrenamiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EntrenamientoEjercicio> entrenamientoEjercicios = new HashSet<>();
}
