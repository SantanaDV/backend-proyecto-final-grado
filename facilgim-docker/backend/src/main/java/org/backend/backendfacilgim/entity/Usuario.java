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

/**
 * Entidad que representa a un usuario del sistema.
 * <p>
 * Contiene información de autenticación, datos personales, roles y la lista de entrenamientos
 * asociados. La contraseña se almacena cifrada y no se expone en las respuestas JSON.
 * </p>
 *
 * @autor Francisco Santana
 */
@Entity
@Table(name = "usuario")
@Data // Genera mediante Lombok getters, setters, toString, equals y hashCode
@NoArgsConstructor // Genera un constructor sin argumentos mediante Lombok
@AllArgsConstructor // Genera un constructor con todos los argumentos
public class Usuario {

    /**
     * Identificador único del usuario.
     * Se genera automáticamente en la base de datos (IDENTITY).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    /**
     * Nombre de usuario para el inicio de sesión.
     * <p>
     * No puede estar vacío, debe ser único y no nulo en la base de datos.
     * </p>
     */
    @NotBlank(message = "El usuario no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Contraseña cifrada (bcrypt).
     * <p>
     * Se almacena cifrada en la base de datos. No se incluye en las respuestas JSON
     * (WRITE_ONLY). Se excluye del método toString() para mayor seguridad.
     * </p>
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude // Para más seguridad ignora en el toString el password
    private String password;

    /**
     * Correo electrónico del usuario.
     * <p>
     * Debe tener un formato válido de correo y ser único en la base de datos.
     * </p>
     */
    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El correo no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String correo;

    /**
     * Nombre real del usuario.
     * <p>
     * No puede estar vacío.
     * </p>
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    /**
     * Apellido del usuario.
     * <p>
     * No puede estar vacío.
     * </p>
     */
    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false)
    private String apellido;

    /**
     * Dirección física del usuario.
     * <p>
     * Es opcional y puede quedar null.
     * </p>
     */
    @Column
    private String direccion;

    /**
     * Indica si el usuario está activo (habilitado) para autenticarse.
     * <p>
     * Utilizado por Spring Security.
     * </p>
     */
    private boolean enabled = true;

    /**
     * Campo transitorio que indica si el usuario
     * debe crearse con rol de administrador.
     * <p>
     * No se persiste en la base de datos y solo se recibe en el JSON de entrada.
     * </p>
     */
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    /**
     * Roles asignados al usuario.
     * <p>
     * Relación ManyToMany con la entidad {@link Role}. Se almacenan en la tabla intermedia
     * "usuarios_roles". Se ignora en la serialización JSON para evitar recursividad.
     * </p>
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "usuarios_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    @JsonIgnore
    private List<Role> roles = new ArrayList<>();

    /**
     * Lista de entrenamientos creados por este usuario.
     * <p>
     * Relación OneToMany con la entidad {@link Entrenamiento}. Se ignora en la serialización JSON
     * para evitar recursividad.
     * </p>
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Entrenamiento> entrenamientos = new ArrayList<>();

    /**
     * Constructor auxiliar utilizado para pruebas.
     *
     * @param i     Identificador a asignar.
     * @param user  Nombre de usuario.
     * @param pass  Contraseña cifrada.
     * @param mail  Correo electrónico.
     */
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
