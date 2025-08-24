# Multi-stage build optimizado para Railway
FROM node:22-alpine AS builder

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci --only=production --silent --no-audit --no-fund && \
    npm cache clean --force

# Install dev dependencies for build
RUN npm install --silent --no-audit --no-fund

# Copy source code
COPY . .

# Build with Railway optimizations
ENV GENERATE_SOURCEMAP=false
ENV DISABLE_ESLINT_PLUGIN=true
ENV NODE_OPTIONS="--max-old-space-size=1024"

# Use Railway environment variable for API URL during build
ARG REACT_APP_API_URL
ENV REACT_APP_API_URL=$REACT_APP_API_URL

RUN npm run build && \
    npm cache clean --force && \
    rm -rf node_modules

# Stage 2: Nginx for Railway
FROM nginx:alpine

# Copy nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy built files
COPY --from=builder /app/build /usr/share/nginx/html

# Railway uses PORT environment variable
EXPOSE $PORT

# Update nginx config to use Railway's PORT
CMD sed -i "s/listen 80/listen $PORT/g" /etc/nginx/conf.d/default.conf && \
    nginx -g "daemon off;"