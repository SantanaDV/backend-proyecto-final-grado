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
    @Column(name = "numero_serie", nullable = false)
    private int numeroSerie;

    @Min(value = 1, message = "Debe haber al menos una repetición")
    @Column(nullable = false)
    private int repeticiones;

    @Min(value = 0, message = "El peso no puede ser negativo")
    @Column(nullable = false)
    private double peso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entrenamiento_ejercicio", nullable = false)
    @JsonIgnore
    private EntrenamientoEjercicio entrenamientoEjercicio;
}
