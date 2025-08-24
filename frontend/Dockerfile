# Multi-stage build optimizado para espacio
FROM node:22-alpine AS builder

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install ONLY production dependencies initially
RUN npm ci --only=production --silent --no-audit --no-fund && \
    npm cache clean --force

# Install dev dependencies for build (temporarily)
RUN npm install --silent --no-audit --no-fund

# Copy source code
COPY . .

# Build with space optimizations
ENV GENERATE_SOURCEMAP=false
ENV DISABLE_ESLINT_PLUGIN=true
ENV NODE_OPTIONS="--max-old-space-size=1024"
RUN npm run build && \
    npm cache clean --force && \
    rm -rf node_modules

# Stage 2: Minimal Nginx
FROM nginx:alpine

# Install curl for healthcheck (minimal)
RUN apk add --no-cache curl && \
    rm -rf /var/cache/apk/*

# Copy nginx config
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

# Copy built files
COPY --from=builder /app/build /usr/share/nginx/html

# Set permissions
RUN chown -R nginx:nginx /usr/share/nginx/html

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD curl -f http://localhost/ || exit 1

CMD ["nginx", "-g", "daemon off;"]