package org.backend.backendfacilgim.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "entrenamientoEjercicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Serie> series = new ArrayList<>();
}
