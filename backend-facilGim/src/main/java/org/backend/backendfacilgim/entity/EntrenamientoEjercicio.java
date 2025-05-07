package org.backend.backendfacilgim.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entrenamiento_ejercicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrenamientoEjercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entrenamiento", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Entrenamiento entrenamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ejercicio", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Ejercicio ejercicio;

    @NotNull
    @Min(value = 0, message = "El peso no puede ser negativo")
    @Column(nullable = false)
    private Double peso;

    @NotNull
    @Min(value = 1, message = "Debe haber al menos una repetición")
    @Column(nullable = false)
    private Integer repeticiones;

    /** para ordenar dentro del entrenamiento */
    private Integer orden;

    @OneToMany(mappedBy = "entrenamientoEjercicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Serie> series = new ArrayList<>();
}
