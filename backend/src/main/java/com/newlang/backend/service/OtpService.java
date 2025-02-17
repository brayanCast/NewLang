package com.newlang.backend.service;

import com.newlang.backend.dto.RequestResp;
import com.newlang.backend.entity.User;
import com.newlang.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private String generatedOtp;
    private String verifiedUserEmail;

    public void sendOtp(String email) {
        RequestResp resp = new RequestResp();
        Optional<User> existEmail = userRepository.findByEmail(email);
        Random random = new Random();

        if (existEmail.isEmpty()) {
            throw new IllegalArgumentException("El email no existe");

        }
            User user = existEmail.get();
            SimpleMailMessage message = new SimpleMailMessage();
            generatedOtp = String.valueOf(random.nextInt(999999)+ 100000);
            message.setTo(email);
            message.setSubject("Confirmación de Código OTP");
            message.setText("Buen día, "+ user.getNameUser() + ", \n\n" +
                    "Su código OTP es: " + generatedOtp);

        try {
            javaMailSender.send(message);

        } catch (MailAuthenticationException e) {
            resp.setStatusCode(401);
            resp.setMessage("Error de autenticación: " + e.getMessage());

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage("Error al enviar el correo: " + e.getMessage());
        }
    }

    //Valida que el otp generado no esté vacío y que sea igual al otp digitado
    public boolean verifyOtp(String email, String otp) {
        boolean isValid = generatedOtp != null && generatedOtp.equals(otp);
        if (isValid) {
            verifiedUserEmail = email;
        }
        return isValid;
    }


    public void updatePassword(String email, String password) {
        Optional<User> userEmail = userRepository.findByEmail(email);

        if(userEmail.isEmpty()) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        User user = userEmail.get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

    }

    public String getVerifiedEmail() {
        return verifiedUserEmail;
    }

    public void clearVerifiedUser(){
        verifiedUserEmail = null;
        generatedOtp = null;
    }
}
