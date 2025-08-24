package com.newlang.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:3001}")
    private String allowedOrigins;

    @Value("${spring.profiles.active:development}")
    private String activeProfile;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins;

        // En desarrollo, permitir localhost en varios puertos
        if ("development".equals(activeProfile)) {
            origins = new String[]{
                    "http://localhost:3000",
                    "http://localhost:3001",
                    "http://127.0.0.1:3000",
                    "http://127.0.0.1:3001"
            };
        } else {
            // En producción, usar las URLs configuradas
            origins = allowedOrigins.split(",");
        }

        registry.addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}