# Use Eclipse Temurin OpenJDK 17
FROM eclipse-temurin:17-jdk-alpine

# Copy the built JAR from target folder
COPY target/*.jar app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app.jar"]