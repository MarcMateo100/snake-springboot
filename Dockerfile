FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia todos los archivos del proyecto
COPY . .

# Compila el proyecto
RUN ./mvnw clean package -DskipTests

# Ejecuta el jar generado
EXPOSE 8080
CMD ["java", "-jar", "target/snakeapp-0.0.1-SNAPSHOT.jar"]
