# Stage 1: Build the application with Node.js
FROM node:22-alpine AS builder

WORKDIR /app

# Copy package files for better caching
COPY package*.json ./

# Install dependencies with cache optimization
RUN npm ci --only=production --silent

# Copy the rest of the application code
COPY . .

# Build the app for production with optimizations
ENV GENERATE_SOURCEMAP=false
ENV DISABLE_ESLINT_PLUGIN=true
RUN npm run build

# Stage 2: Serve the application with Nginx
FROM nginx:alpine

# Install curl for healthchecks (opcional)
RUN apk add --no-cache curl

# Copy custom Nginx configuration
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

# Copy the static build files from the builder stage
COPY --from=builder /app/build /usr/share/nginx/html

# Create nginx user and set permissions
RUN chown -R nginx:nginx /usr/share/nginx/html && \
    chmod -R 755 /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Add healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost/ || exit 1

# Command to start Nginx
CMD ["nginx", "-g", "daemon off;"]