# Use Eclipse Temurin JRE 17 (much smaller than JDK)
FROM eclipse-temurin:17-jre-alpine

# Copy the built JAR from target folder
COPY target/*.jar app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app.jar"]