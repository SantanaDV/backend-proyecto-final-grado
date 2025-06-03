package org.backend.backendfacilgim.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entidad que representa un tipo de entrenamiento.
 * Cada {@code TipoEntrenamiento} agrupa varios entrenamientos con características similares.
 *
 * Autor: Francisco Santana
 */
@Entity
@Table(name = "tipo_entrenamiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoEntrenamiento {

    /**
     * Identificador único del tipo de entrenamiento.
     * <p>
     * Se genera automáticamente en la base de datos (IDENTITY).
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre descriptivo del tipo de entrenamiento.
     * <p>
     * Debe ser único y no nulo en la base de datos.
     * </p>
     */
    @Column(unique = true, nullable = false)
    private String nombre;

    /**
     * Lista de entrenamientos asociados a este tipo.
     * <p>
     * Relación OneToMany con {@link Entrenamiento}. Se ignora en la serialización JSON
     * para evitar ciclos de referencia.
     * </p>
     */
    @OneToMany(mappedBy = "tipoEntrenamiento")
    @JsonIgnore
    private List<Entrenamiento> entrenamientos;

    /**
     * Constructor utilizado únicamente con fines de prueba.
     * <p>
     * Permite crear una instancia con un id y nombre específicos, sin inicializar la lista de entrenamientos.
     * </p>
     *
     * @param i      Valor entero que se convierte en {@code id} del tipo de entrenamiento.
     * @param nombre Nombre del tipo de entrenamiento.
     */
    public TipoEntrenamiento(int i, String nombre) {
        this.id = (long) i;
        this.nombre = nombre;
        this.entrenamientos = null;
    }
}
