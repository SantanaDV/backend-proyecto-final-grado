package org.backend.backendfacilgim.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un ejercicio en el sistema.
 * <p>
 * Cada ejercicio tiene un identificador, un nombre único,
 * opcionalmente una URL de imagen, y puede estar asociado a múltiples
 * relaciones {@link EntrenamientoEjercicio} que vinculan el ejercicio
 * a entrenamientos específicos.
 * </p>
 *
 * Autor: Francisco Santana
 */
@Entity
@Table(name = "ejercicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ejercicio {

    /**
     * Identificador único del ejercicio.
     * <p>
     * Generado automáticamente por la base de datos.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejercicio")
    @EqualsAndHashCode.Include
    private Integer idEjercicio;

    /**
     * Nombre descriptivo del ejercicio.
     * <p>
     * Obligatorio y único en la tabla. No puede estar en blanco.
     * </p>
     */
    @NotBlank(message = "El nombre del ejercicio es obligatorio")
    @Column(nullable = false, unique = true)
    private String nombre;

    /**
     * URL de la imagen representativa del ejercicio.
     * <p>
     * Este campo es opcional y puede usarse para mostrar una imagen
     * en el frontend.
     * </p>
     */
    private String imagenUrl;

    /**
     * Conjunto de relaciones {@link EntrenamientoEjercicio} que vinculan este ejercicio
     * con entrenamientos. Se ignora en la serialización JSON para evitar bucles de referencia.
     * <p>
     * Cascade ALL y orphanRemoval true para que las relaciones se gestionen automáticamente
     * al persistir o eliminar el ejercicio.
     * </p>
     */
    @OneToMany(mappedBy = "ejercicio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<EntrenamientoEjercicio> entrenamientoEjercicios = new HashSet<>();
}
