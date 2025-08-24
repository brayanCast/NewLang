# Stage 1: Build the application with Maven
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy the Maven project file first (for dependency caching)
COPY pom.xml .

# Download dependencies to cache them
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests for faster builds)
RUN mvn clean package -DskipTests -B

# Stage 2: Run the application
FROM eclipse-temurin:21-jre

# Install curl for healthchecks (opcional)
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/*

# Create app user for security
RUN groupadd -r appuser && useradd -r -g appuser appuser

WORKDIR /app

# Copy the JAR file from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Change ownership to app user
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Add healthcheck (opcional - requiere Spring Boot Actuator)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Start application
ENTRYPOINT ["java", "-jar", "app.jar"]