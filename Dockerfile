# Use an official OpenJDK 17 slim image
FROM openjdk:17.0.9-jdk-slim

# Copy the built JAR from target folder
COPY target/*.jar app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app.jar"]
