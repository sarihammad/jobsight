FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY backend/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]