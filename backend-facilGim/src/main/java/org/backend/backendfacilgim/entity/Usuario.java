package org.backend.backendfacilgim.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data //Genera mediante lombok getters, setters, toString, equals y hasCode
@NoArgsConstructor //Genera un constructor sin argumentos mediante lombok
@AllArgsConstructor //Genera un constructor con todos los argumentos
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @NotBlank(message = "El usuario no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String username;

    // Contraseña encriptada (bcrypt) => se guarda cifrada en la BD
    @NotBlank(message = "La contraseña es obligatoria")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude //Para más seguridad ignora en el to string el password
    private String password;

    @Email(message = "Debe ser un correo valido")
    @NotBlank(message = "El correo no puede estar vacio")
    @Column(nullable = false, unique = true)
    private String correo;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false)
    private String apellido;

    @Column
    private String direccion;

    // // Utilizado por Spring Security para saber si el usuario está activo
    private boolean enabled = true;

    // Campo transitorio para indicar si se crea como admin
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "usuarios_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    // Se indica que ignore la propiedad 'usuarios' en Role para evitar recursividad
    @JsonIgnore
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Entrenamiento> entrenamientos = new ArrayList<>();

    public Usuario(int i, String user, String pass, String mail) {
        this.idUsuario = i;
        this.username = user;
        this.password = pass;
        this.correo = mail;
    }


    //  Método para limpiar las relaciones ANTES de eliminar el usuario
    @PreRemove
    private void removeRoles() {
        this.roles.clear();
    }

}
