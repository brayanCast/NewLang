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
# ENV REACT_APP_API_URL=$REACT_APP_API_URL
ENV REACT_APP_API_URL="http://localhost:8080"

RUN npm run build && \
    npm cache clean --force && \
    rm -rf node_modules

# Stage 2: Nginx for Railway with dynamic port
FROM nginx:alpine

# Install envsubst for environment variable substitution
RUN apk add --no-cache gettext

# Copy built files
COPY --from=builder /app/build /usr/share/nginx/html

# Remove default nginx config
RUN rm /etc/nginx/conf.d/default.conf

# Create nginx config that works with Railway
RUN echo 'server {\
    listen 0.0.0.0:$PORT;\
    server_name _;\
    root /usr/share/nginx/html;\
    index index.html index.htm;\
    location / {\
        try_files $uri $uri/ /index.html;\
    }\
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2)$ {\
        expires 1y;\
        add_header Cache-Control "public, immutable";\
        try_files $uri =404;\
    }\
    error_page 404 /index.html;\
}' > /etc/nginx/conf.d/default.conf.template

# Debugging startup
CMD sh -c "echo 'PORT is: '$PORT && \
envsubst '\$PORT' < /etc/nginx/conf.d/default.conf.template > /etc/nginx/conf.d/default.conf && \
echo 'Generated nginx config:' && \
cat /etc/nginx/conf.d/default.conf && \
echo 'Testing nginx config:' && \
nginx -t && \
echo 'Files in html directory:' && \
ls -la /usr/share/nginx/html/ && \
echo 'Starting nginx...' && \
nginx -g 'daemon off;'"