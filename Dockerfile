# Base image for building the application
FROM gradle:8.2.0-jdk21 AS build

# Set the working directory
WORKDIR /app

# Copy the build.gradle.kts and settings.gradle.kts files
COPY build.gradle.kts settings.gradle.kts ./

# Copy the source code
COPY src ./src

# Build the application
RUN gradle build --no-daemon

# Base image for running the application
FROM openjdk:21

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8180

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
