FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY . .

ENV DATABASE_URL=${DATABASE_URL}
ENV DB_USER=${DB_USER}
ENV DB_PASSWORD=${DB_PASSWORD}

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

EXPOSE 8080
CMD ["java", "-jar", "target/snakeapp-0.0.1-SNAPSHOT.jar"]
