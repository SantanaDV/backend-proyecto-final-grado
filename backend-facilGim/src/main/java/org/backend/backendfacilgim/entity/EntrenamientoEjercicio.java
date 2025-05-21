package org.backend.backendfacilgim.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "entrenamiento_ejercicio")
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





    /** para ordenar dentro del entrenamiento */
    private Integer orden;

    @OneToMany(mappedBy = "entrenamientoEjercicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Serie> series = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntrenamientoEjercicio that = (EntrenamientoEjercicio) o;

        // Comparamos por claves compuestas lógicas
        if (!ejercicio.equals(that.ejercicio)) return false;
        return entrenamiento.equals(that.entrenamiento);
    }

    @Override
    public int hashCode() {
        int result = ejercicio.hashCode();
        result = 31 * result + entrenamiento.hashCode();
        return result;
    }
}
