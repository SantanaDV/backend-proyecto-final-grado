package org.backend.backendfacilgim.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa la relación entre un entrenamiento y un ejercicio,
 * incluyendo el orden en que aparece y las series asociadas.
 *
 * Un {@code EntrenamientoEjercicio} conecta un {@link Entrenamiento} con un {@link Ejercicio},
 * y puede tener múltiples {@link Serie} que definen repeticiones y pesos específicos.
 *
 * Autor: Francisco Santana
 */
@Setter
@Getter
@Entity
@Table(name = "entrenamiento_ejercicio")
@NoArgsConstructor
@AllArgsConstructor
public class EntrenamientoEjercicio {

    /**
     * Identificador único de la relación entrenamiento–ejercicio.
     * <p>
     * Generado automáticamente por la base de datos.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Entrenamiento al que pertenece esta relación.
     * <p>
     * ManyToOne hacia {@link Entrenamiento}, perezoso. No puede ser nulo.
     * Se ignoran propiedades de Hibernate en la serialización JSON.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entrenamiento", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @NotNull(message = "El entrenamiento es obligatorio")
    private Entrenamiento entrenamiento;

    /**
     * Ejercicio asociado a este entrenamiento.
     * <p>
     * ManyToOne hacia {@link Ejercicio}, perezoso. No puede ser nulo.
     * Se ignoran propiedades de Hibernate en la serialización JSON.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ejercicio", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @NotNull(message = "El ejercicio es obligatorio")
    private Ejercicio ejercicio;

    /**
     * Orden en que este ejercicio aparece dentro del entrenamiento.
     * <p>
     * Es un número que permite definir la secuencia,
     * por ejemplo primero, segundo, etc.
     * </p>
     */
    private Integer orden;

    /**
     * Lista de series asociadas a esta relación.
     * <p>
     * OneToMany hacia {@link Serie}. Cascade ALL y orphanRemoval true
     * para que las series se guarden/eliminar según se modifique esta entidad.
     * </p>
     */
    @OneToMany(mappedBy = "entrenamientoEjercicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Serie> series = new ArrayList<>();

    /**
     * Compara dos instancias de {@code EntrenamientoEjercicio} por sus claves lógicas:
     * ejercicio y entrenamiento. No considera el campo {@code id} para igualdad,
     * sino únicamente la combinación de ejercicio y entrenamiento.
     *
     * @param o objeto a comparar
     * @return {@code true} si ejercicio y entrenamiento coinciden, {@code false} en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntrenamientoEjercicio that = (EntrenamientoEjercicio) o;

        if (!ejercicio.equals(that.ejercicio)) return false;
        return entrenamiento.equals(that.entrenamiento);
    }

    /**
     * Calcula el hash code basándose en la combinación de ejercicio y entrenamiento,
     * coherente con {@link #equals(Object)}.
     *
     * @return valor hash
     */
    @Override
    public int hashCode() {
        int result = ejercicio.hashCode();
        result = 31 * result + entrenamiento.hashCode();
        return result;
    }
}
