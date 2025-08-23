# Stage 1: Build the application with Maven
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Set the working directory
WORKDIR /app

# Copy the Maven project file first (for dependency caching)
COPY pom.xml .

# Download dependencies to cache them
RUN mvn dependency:go-offline -B

# Copy the rest of the application code
COPY src ./src

# Build the application (skip tests for faster builds)
RUN mvn clean package -DskipTests -B

# Stage 2: Run the application
FROM eclipse-temurin:21-jre

# Install netcat for database connection waiting and curl for healthchecks
RUN apt-get update && \
    apt-get install -y --no-install-recommends netcat-traditional curl && \
    rm -rf /var/lib/apt/lists/*

# Create app user for security
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Set the working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Create wait-for-it script for database connectivity
COPY <<EOF wait-for-db.sh
#!/bin/bash
set -e

host="\$1"
port="\$2"
shift 2
cmd="\$@"

until nc -z "\$host" "\$port"; do
  echo "Waiting for database at \$host:\$port..."
  sleep 2
done

echo "Database is ready! Starting application..."
exec \$cmd
EOF

# Make script executable
RUN chmod +x wait-for-db.sh

# Change ownership to app user
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose the port
EXPOSE 8080

# Add healthcheck
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Use wait script to ensure database is ready
ENTRYPOINT ["./wait-for-db.sh", "db", "3306", "java", "-jar", "app.jar"]