package org.backend.backendfacilgim.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

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

    private String imagenUrl;

    @OneToMany(mappedBy = "ejercicio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<EntrenamientoEjercicio> entrenamientoEjercicios = new HashSet<>();
}
