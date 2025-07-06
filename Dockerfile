FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copiar todos los archivos del proyecto
COPY . .

# Establecer variables de entorno en tiempo de build
ENV DATABASE_URL=${DATABASE_URL}
ENV DB_USER=${DB_USER}
ENV DB_PASSWORD=${DB_PASSWORD}

# Compilar (si usas Maven Wrapper)
RUN ./mvnw clean package -DskipTests

EXPOSE 8080
CMD ["java", "-jar", "target/snakeapp-0.0.1-SNAPSHOT.jar"]
