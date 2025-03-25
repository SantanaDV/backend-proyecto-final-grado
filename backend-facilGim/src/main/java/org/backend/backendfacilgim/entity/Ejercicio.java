package org.backend.backendfacilgim.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ejercicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejercicio")
    private Integer idEjercicio;

    @NotBlank(message = "El nombre del ejercicio es obligatorio")
    private String nombre;

    @Min(value = 1, message = "El número de repeticiones debe ser al menos 1")
    private int repeticiones;

    @Min(value = 0, message = "El peso no puede ser negativo")
    private double peso;

    @ManyToOne
    @JoinColumn(name = "id_entrenamiento", nullable = false)
    @JsonIgnoreProperties("ejercicios")
    @NotNull(message = "El entrenamiento asociado es obligatorio")
    private Entrenamiento entrenamiento;
}
