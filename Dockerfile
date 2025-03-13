# Usa la imagen oficial de OpenJDK con JDK 17
FROM maven:3.9.6-eclipse-temurin-17 as builder
WORKDIR /app
# Copiar los archivos del proyecto al contenedor
COPY . .

# Construir la aplicación con Maven (sin ejecutar tests)
RUN mvn clean package -DskipTests

# Nueva imagen basada en OpenJDK 17 para ejecutar la aplicación
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el JAR generado en la fase de construcción
COPY --from=builder /app/target/api_meteorologica-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que corre la aplicación
EXPOSE 9090

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]