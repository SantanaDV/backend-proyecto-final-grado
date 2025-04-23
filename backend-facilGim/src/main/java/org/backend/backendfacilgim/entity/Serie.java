package org.backend.backendfacilgim.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "serie")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Min(value = 1, message = "El número de serie debe ser al menos 1")
    @Column(name = "numero_serie")
    private int numeroSerie;

    @Min(value = 1, message = "Debe haber al menos una repetición")
    private int repeticiones;

    @Min(value = 0, message = "El peso no puede ser negativo")
    private double peso;

    @ManyToOne
    @JoinColumn(name = "id_entrenamiento_ejercicio", nullable = false)
    @JsonIgnore
    private EntrenamientoEjercicio entrenamientoEjercicio;
}
