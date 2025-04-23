package org.backend.backendfacilgim.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tipo_entrenamiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoEntrenamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "tipoEntrenamiento")
    @JsonIgnore
    private List<Entrenamiento> entrenamientos;
//Constructor for testing purposes only
    public TipoEntrenamiento(int i, String fuerza) {
        this.id = (long) i;
        this.nombre = fuerza;
        this.entrenamientos = null;
    }
}
