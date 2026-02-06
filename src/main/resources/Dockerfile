# Imagen base con Java 21
FROM eclipse-temurin:21-jdk-jammy

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el jar generado por Maven
COPY target/meeting-scheduler-*.jar app.jar

# Exponer el puerto de la app
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","app.jar"]
