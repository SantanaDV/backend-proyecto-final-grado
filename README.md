# FacilGim Backend

FacilGim es un sistema para la gestion de rutinas de entrenamiento. Este proyecto proporciona la API REST que permite registrar usuarios, autenticar mediante JWT y administrar entrenamientos, ejercicios y series.

## Características principales

- Autenticación y autorización con JSON Web Tokens.
- CRUD de usuarios, entrenamientos y ejercicios.
- Roles (USER y ADMIN) con Spring Security.
- Documentación automática con Swagger/OpenAPI.
- Despliegue mediante Docker.

## Tecnologías

- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="20" height="20"> **Java 17**
- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="20" height="20"> **Spring Boot 3.x**
- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="20" height="20"> **Spring Security**
- <img src="https://jwt.io/img/pic_logo.svg" width="20" height="20"> **JWT (JSON Web Tokens)**
- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/hibernate/hibernate-original.svg" width="20" height="20"> **Spring Data JPA**
- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg" width="20" height="20"> **MySQL**
- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/maven/maven-original.svg" width="20" height="20"> **Maven**
- Docker y Docker Compose
- Swagger / OpenAPI (springdoc)

## Tablas de la base de datos

| Tabla                     | Descripción |
|---------------------------|-------------|
| **usuario**               | Datos de acceso y perfil del usuario |
| **role**                  | Roles disponibles (USER, ADMIN) |
| **usuarios_roles**        | Relación muchos a muchos entre usuario y role |
| **ejercicio**             | Catálogo de ejercicios |
| **tipo_entrenamiento**    | Tipos o categorías para los entrenamientos |
| **entrenamiento**         | Entrenamientos creados por los usuarios |
| **entrenamiento_ejercicio** | Asociación de ejercicios dentro de un entrenamiento |
| **serie**                 | Series con repeticiones y peso para cada ejercicio |

## Configuración y ejecución local

### Requisitos previos

- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="20" height="20"> **Java 17**
- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/maven/maven-original.svg" width="20" height="20"> **Maven**
- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg" width="20" height="20"> **MySQL**
- Git

### Pasos

1. Clona el repositorio.
   ```bash
   git clone <este-repositorio>
   ```
2. Crea la base de datos:
   ```sql
   CREATE DATABASE facilgim_db;
   ```
3. Crea `src/main/resources/application.properties` dentro de `facilgim-docker/backend/` con tus credenciales de MySQL:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/facilgim_db
   spring.datasource.username=<usuario>
   spring.datasource.password=<contraseña>
   spring.jpa.hibernate.ddl-auto=update
   ```
4. Ejecuta la aplicación:
   ```bash
   cd facilgim-docker/backend
   mvn spring-boot:run
   ```

## Ejecución con Docker

1. Dentro de `facilgim-docker/` crea un archivo `variables.env` con las variables de entorno que utilizará la aplicación (usuario, contraseña, etc.).
2. Inicia los contenedores:
   ```bash
   cd facilgim-docker
   docker compose up --build
   ```
   Se levantarán una base de datos MySQL, el backend y un proxy Nginx.

## Autenticación JWT

1. **Registro** – `POST /api/usuarios/registrar` permite crear un nuevo usuario.
2. **Login** – `POST /login` valida las credenciales. Si son correctas devuelve un token JWT.
3. Todas las peticiones protegidas deben incluir el encabezado:
   ```http
   Authorization: Bearer <token>
   ```

## Endpoints principales

### Autenticación

| Método | Endpoint                         | Descripción                           |
|-------|----------------------------------|---------------------------------------|
| POST  | `/login`                         | Iniciar sesión y obtener el JWT       |
| POST  | `/api/usuarios/registrar`        | Registrar nuevo usuario               |

### Entrenamientos (requiere JWT)

| Método | Endpoint                       | Acción                                   |
|--------|--------------------------------|-----------------------------------------|
| GET    | `/api/entrenamientos`          | Listar entrenamientos del usuario       |
| POST   | `/api/entrenamientos`          | Crear entrenamiento                     |
| GET    | `/api/entrenamientos/{id}`     | Detalle de un entrenamiento             |
| PUT    | `/api/entrenamientos/{id}`     | Actualizar entrenamiento                |
| DELETE | `/api/entrenamientos/{id}`     | Eliminar entrenamiento                  |

## Pruebas

Ejecuta todas las pruebas (si existen) con Maven:
```bash
mvn test
```

## Contribución

1. Haz un fork del repositorio y crea una rama para tu cambio.
2. Realiza tus modificaciones y envía un pull request.
3. Al no existir una licencia definida, cualquier contribución se aceptará bajo acuerdo explícito con el mantenedor.

## Licencia

Este proyecto actualmente no incluye una licencia pública.

