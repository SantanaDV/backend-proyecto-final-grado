#  Backend FacilGim

Este repositorio contiene el backend del proyecto **FacilGim**, desarrollado con **Spring Boot y Java 17**. FacilGim permite gestionar entrenamientos personales, incluyendo ejercicios detallados con repeticiones y pesos utilizados, autenticación mediante JWT y administración por roles.

---

## 📌 Descripción del Proyecto

**FacilGim** es una aplicación destinada a simplificar el seguimiento de entrenamientos, permitiendo a los usuarios crear, editar y eliminar entrenamientos y ejercicios. El sistema es seguro y utiliza JWT (JSON Web Tokens) para autenticar usuarios y proteger el acceso a recursos.

---

## ⚙️ Tecnología Utilizada

- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="20" height="20"> **Java 17**  
- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="20" height="20"> **Spring Boot 3.x**  
- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="20" height="20"> **Spring Security**  
- <img src="https://jwt.io/img/pic_logo.svg" width="20" height="20"> **JWT (JSON Web Tokens)**  
- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/hibernate/hibernate-original.svg" width="20" height="20"> **Spring Data JPA**  
- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg" width="20" height="20"> **MySQL**  
- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/maven/maven-original.svg" width="20" height="20"> **Maven**


---

## 🗃️ Diseño de la Base de Datos

El backend utiliza MySQL con la siguiente estructura:

- `usuario`
- `roles`
- `usuarios_roles` *(tabla intermedia para relación N:M entre usuario y roles)*
- `entrenamiento`
- `ejercicio`

---

## 🔐 Autenticación y Seguridad (JWT)

El sistema protege recursos mediante JWT en los siguientes endpoints:

- **POST** `/registrar` - Registro
- **POST** `/login` - Inicio de sesión

Las peticiones protegidas necesitan incluir el siguiente encabezado:

```http
Authorization: Bearer <JWT Token>
```
## 📍 Endpoints Principales (REST API)

### 🔹 Autenticación

| Método | Endpoint       | Descripción                         |
|--------|----------------|-------------------------------------|
| POST   | `/registrar` | Registrar usuario                   |
| POST   | `/login`  | Autenticar usuario (retorna JWT)    |

### 🔹 Gestión de Entrenamientos (Requiere JWT)

| Método | Endpoint                  | Descripción                             |
|--------|---------------------------|-----------------------------------------|
| POST   | `/entrenamientos`         | Crear nuevo entrenamiento               |
| GET    | `/entrenamientos`         | Obtener entrenamientos del usuario      |
| GET    | `/entrenamientos/{id}`    | Detalle entrenamiento específico        |
| PUT    | `/entrenamientos/{id}`    | Actualizar entrenamiento                |
| DELETE | `/entrenamientos/{id}`    | Eliminar entrenamiento                  |

---

## 🛠️ Instrucciones para Ejecutar en Local

### 🔸 Requisitos

- Java JDK 17
- MySQL Server (recomendado XAMPP)
- Postman
- Git
- Maven

### 🔸 Ejecución Paso a Paso

**1. Clona el repositorio**

```bash
git clone <url-del-repositorio>
```

## 🛠️ Instrucciones de Configuración y Ejecución del Proyecto

### Crear Base de Datos en MySQL

Ejecuta el siguiente comando SQL para crear la base de datos:

```sql
CREATE DATABASE facilgim_db;
```

###  Configurar acceso MySQL

Edita el archivo `application.properties` ubicado en la ruta `/src/main/resources/` con la siguiente configuración:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/facilgim_db
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```


###  Ejecutar la aplicación

Desde la terminal situada en el directorio raíz del proyecto, ejecuta:

```bash
mvn spring-boot:run
```

### Probar la API con Postman
 * Registra un nuevo usuario:

```http
POST /login
```

 *Inicia sesión y obtén tu token JWT:
 ```http
POST /registrar
```


