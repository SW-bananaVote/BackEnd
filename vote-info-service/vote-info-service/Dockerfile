# Base image
FROM openjdk:17-jdk-slim

# Working directory inside the container
WORKDIR /app

# Copy the JAR file from the specified path on your host machine
COPY out/artifacts/vote_info_service_jar/vote-info-service.jar /app/vote-info-service.jar

# Expose the application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/vote-info-service.jar"]
