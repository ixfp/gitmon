# Base image for building the application
FROM gradle:latest AS build

# Set the working directory
WORKDIR /app

# Declare build arguments
ARG OAUTH2_CLIENT_GITHUB_ID
ARG OAUTH2_CLIENT_GITHUB_SECRET
ARG JWT_SECRET

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

# Declare environment variables for runtime
ENV OAUTH2_CLIENT_GITHUB_ID=$OAUTH2_CLIENT_GITHUB_ID
ENV OAUTH2_CLIENT_GITHUB_SECRET=$OAUTH2_CLIENT_GITHUB_SECRET
ENV JWT_SECRET=$JWT_SECRET

# Expose the application port
EXPOSE 8180

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
