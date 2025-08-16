# Stage 1: Build the application with Node.js
FROM node:22-alpine AS builder

WORKDIR /app

# The following two lines copy package.json and install dependencies.
# This is done before copying all the code to leverage Docker's build cache.
COPY package.json ./
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the app for production
RUN npm run build

# Stage 2: Serve the application with Nginx
# This stage uses a lightweight Nginx image to serve the compiled files.
FROM nginx:alpine

# Copia el archivo de configuración de Nginx
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

# Copy the static build files from the builder stage to Nginx's public directory
COPY --from=builder /app/build /usr/share/nginx/html

# Expose port 80, the default port for Nginx
EXPOSE 80

# Command to start Nginx in the foreground
CMD ["nginx", "-g", "daemon off;"]