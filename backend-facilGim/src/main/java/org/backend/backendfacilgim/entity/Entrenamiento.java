package org.backend.backendfacilgim.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un entrenamiento.
 * <p>
 * Cada entrenamiento tiene un identificador, nombre, fecha, descripción,
 * duración, un tipo asociado y un usuario propietario. Además, puede
 * contener múltiples relaciones a ejercicios ({@link EntrenamientoEjercicio}).
 * </p>
 *
 * Autor: Francisco Santana
 */
@Entity
@Table(name = "entrenamiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Entrenamiento {

    /**
     * Identificador único del entrenamiento.
     * <p>
     * Generado automáticamente por la base de datos.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrenamiento")
    @EqualsAndHashCode.Include
    private Integer idEntrenamiento;

    /**
     * Nombre descriptivo del entrenamiento.
     * <p>
     * No puede estar en blanco.
     * </p>
     */
    @NotBlank(message = "El nombre del entrenamiento es obligatorio")
    private String nombre;

    /**
     * Fecha en la que se realiza o planifica el entrenamiento.
     * <p>
     * Formato LocalDate. No puede ser nulo.
     * </p>
     */
    @NotNull(message = "La fecha del entrenamiento es obligatoria")
    private LocalDate fechaEntrenamiento;

    /**
     * Descripción opcional del entrenamiento.
     */
    private String descripcion;

    /**
     * Duración estimada del entrenamiento en minutos.
     * <p>
     * Debe ser al menos 1.
     * </p>
     */
    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private int duracion;

    /**
     * Tipo de entrenamiento asociado.
     * <p>
     * Relación ManyToOne con {@link TipoEntrenamiento}.
     * No puede ser nulo.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "id_tipo_entrenamiento", nullable = false)
    @NotNull(message = "El tipo de entrenamiento es obligatorio")
    private TipoEntrenamiento tipoEntrenamiento;

    /**
     * Usuario propietario del entrenamiento.
     * <p>
     * Relación ManyToOne con {@link Usuario}. Se ignora la propiedad
     * "entrenamientos" en la serialización JSON para evitar bucles.
     * No puede ser nulo.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnoreProperties("entrenamientos")
    @NotNull(message = "El usuario es obligatorio")
    private Usuario usuario;

    /**
     * Conjunto de relaciones {@link EntrenamientoEjercicio} que
     * asocian este entrenamiento con ejercicios específicos.
     * <p>
     * Cascade ALL y orphanRemoval true para gestionar automáticamente las
     * entidades relacionadas al persistir o eliminar el entrenamiento.
     * Se ignora en la serialización JSON.
     * </p>
     */
    @OneToMany(mappedBy = "entrenamiento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<EntrenamientoEjercicio> entrenamientoEjercicios = new HashSet<>();
}
