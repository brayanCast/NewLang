# Stage 1: Build the application with Maven
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Set the working directory
WORKDIR /app

# Copy the Maven project file first
COPY pom.xml .

# Download dependencies without building the app to cache them
RUN mvn dependency:go-offline

# Copy the rest of the application code
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-slim
# Using the -slim version keeps the image smaller.

# Set the working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port the app will run on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]