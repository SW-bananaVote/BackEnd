# Base image
FROM openjdk:17-jdk-slim

# Working directory
WORKDIR /app

# Copy JAR file
COPY target/vote-info-service-0.0.1-SNAPSHOT.jar /app/vote-info-service.jar

# Expose port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/vote-info-service.jar"]
