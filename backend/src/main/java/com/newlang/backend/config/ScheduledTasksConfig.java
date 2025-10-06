package com.newlang.backend.config;

import com.newlang.backend.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduledTasksConfig {

    @Autowired
    private OtpService otpService;

    // Ejecutar cada 10 minutos para limpiar datos expirados
    @Scheduled(fixedRate = 600000)
    public void cleanupExpiredOtpData() {
        otpService.cleanupExpiredData();
    }
}