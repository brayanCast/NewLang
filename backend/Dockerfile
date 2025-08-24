# Stage 1: Build the application with Maven
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy the Maven project file first (for dependency caching)
COPY pom.xml .

# Download dependencies to cache them
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests for faster Railway builds)
RUN mvn clean package -DskipTests -B

# Stage 2: Run the application
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the JAR file from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port that Railway will assign
EXPOSE $PORT

# Health check for Railway (opcional)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:$PORT/actuator/health || exit 1

# Use Railway's PORT variable
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=$PORT"]