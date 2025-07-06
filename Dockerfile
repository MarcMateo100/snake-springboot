# Usamos imagen oficial de Java 17
FROM eclipse-temurin:17-jdk

# Crea el directorio de la app en el contenedor
WORKDIR /app

# Copia el archivo .jar generado por Maven al contenedor
COPY target/snakeapp-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto que usará la app
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
