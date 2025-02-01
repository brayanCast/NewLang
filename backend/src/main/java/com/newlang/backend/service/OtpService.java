package com.newlang.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender javaMailSender;

    private String generatedOtp;

    public void sendOtp(String email) {
        generatedOtp = String.valueOf((Math.random() * (999999 - 100000) + 1));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Confirmación de Código OTP");
        message.setText("Su código OTP es: " + generatedOtp);
        javaMailSender.send(message);
    }

    //Valida que el otp generado no esté vacío y que sea igual al otp digitado
    public boolean verifyOtp(String otp) {
        return generatedOtp != null && generatedOtp.equals(otp);
    }
}
