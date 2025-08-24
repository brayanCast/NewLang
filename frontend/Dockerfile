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

# Stage 2: Nginx for Railway with dynamic port
FROM nginx:alpine

# Install envsubst for environment variable substitution
RUN apk add --no-cache gettext

# Copy nginx template
COPY nginx.conf.template /etc/nginx/templates/default.conf.template

# Copy built files
COPY --from=builder /app/build /usr/share/nginx/html

# Create startup script
RUN printf '#!/bin/sh\n\
echo "Starting Nginx on port $PORT"\n\
envsubst '"'"'$PORT'"'"' < /etc/nginx/templates/default.conf.template > /etc/nginx/conf.d/default.conf\n\
nginx -g "daemon off;"\n' > /start.sh && chmod +x /start.sh

EXPOSE $PORT

CMD ["/start.sh"]