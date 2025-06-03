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
ARG JAR_FILE=/app/target/*.jar

# Creamos usuario no-root
RUN useradd -ms /bin/bash appuser
USER appuser

WORKDIR /home/appuser

# Copiamos el JAR generado
COPY --from=build ${JAR_FILE} app.jar

# Puerto expuesto
EXPOSE 8080

# Arranque
ENTRYPOINT ["java","-jar","app.jar"]
