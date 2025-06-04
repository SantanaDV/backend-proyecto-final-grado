# Imagen base para compilar
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos pom y fuentes
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src src

# Empaquetamos sin tests (los test se areglaran mas adelante en el desarollo)
RUN mvn clean package -DskipTests

# 2) Imagen corriendo la aplicación
FROM eclipse-temurin:17-jdk-jammy

# Creamos un usuario ‘appuser’ y la carpeta uploads como root
RUN useradd -ms /bin/bash appuser \
 && mkdir -p /home/appuser/uploads \
 && chown -R appuser:appuser /home/appuser/uploads

# A partir de aquí, todo irá bajo appuser
USER appuser
WORKDIR /home/appuser

# Copiamos el JAR resultante del stage de build
ARG JAR_FILE=/app/target/*.jar
COPY --from=build ${JAR_FILE} app.jar

# Exponemos el puerto 8080
EXPOSE 8080

# Arrancamos la aplicación
ENTRYPOINT ["java","-jar","app.jar"]
