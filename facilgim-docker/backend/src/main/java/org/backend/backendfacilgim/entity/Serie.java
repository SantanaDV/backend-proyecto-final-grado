package org.backend.backendfacilgim.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una serie de un ejercicio dentro de un entrenamiento.
 * Una {@code Serie} está asociada a un {@link EntrenamientoEjercicio} y almacena
 * información de repeticiones, peso y número de serie.
 *
 * Autor: Francisco Santana
 */
@Entity
@Table(name = "serie")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Serie {

    /**
     * Identificador único de la serie.
     * <p>
     * Generado automáticamente por la base de datos (IDENTITY).
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Número secuencial de la serie dentro de un entrenamiento-ejercicio.
     * <p>
     * Debe ser al menos 1 (validado por @Min). Se mapea a la columna "numero_serie".
     * </p>
     */
    @Min(value = 1, message = "El número de serie debe ser al menos 1")
    @Column(name = "numero_serie", nullable = false)
    private int numeroSerie;

    /**
     * Cantidad de repeticiones realizadas en esta serie.
     * <p>
     * Debe ser al menos 1 (validado por @Min). No puede ser nulo.
     * </p>
     */
    @Min(value = 1, message = "Debe haber al menos una repetición")
    @Column(nullable = false)
    private int repeticiones;

    /**
     * Peso utilizado en esta serie (en kilogramos).
     * <p>
     * Debe ser un valor no negativo (validado por @Min). No puede ser nulo.
     * </p>
     */
    @Min(value = 0, message = "El peso no puede ser negativo")
    @Column(nullable = false)
    private double peso;

    /**
     * Relación ManyToOne hacia {@link EntrenamientoEjercicio}.
     * <p>
     * Indica a qué entrenamiento-ejercicio pertenece esta serie.
     * Es una relación perezosa (LAZY) y se ignora en la serialización JSON
     * para evitar ciclos infinitos.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entrenamiento_ejercicio", nullable = false)
    @JsonIgnore
    private EntrenamientoEjercicio entrenamientoEjercicio;
}
