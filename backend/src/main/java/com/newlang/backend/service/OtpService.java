package com.newlang.backend.service;

import com.newlang.backend.dto.requestDto.RequestResp;
import com.newlang.backend.entity.User;
import com.newlang.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Thread-safe storage for OTPs and verified emails
    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, String> verifiedEmails = new ConcurrentHashMap<>();

    // Inner class to store OTP data with timestamp
    private static class OtpData {
        private final String otp;
        private final LocalDateTime createdAt;
        private final long validityMinutes = 5; // OTP válido por 5 minutos

        public OtpData(String otp) {
            this.otp = otp;
            this.createdAt = LocalDateTime.now();
        }

        public String getOtp() {
            return otp;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(createdAt.plusMinutes(validityMinutes));
        }
    }

    public void sendOtp(String email) {
        // Validar que el email no sea nulo o vacío
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }

        Optional<User> existEmail = userRepository.findByEmail(email);

        if (existEmail.isEmpty()) {
            throw new IllegalArgumentException("El email no existe en la base de datos");
        }

        try {
            User user = existEmail.get();
            Random random = new Random();

            // Generar OTP de 6 dígitos
            String generatedOtp = String.valueOf(100000 + random.nextInt(900000));

            // Almacenar OTP con timestamp
            otpStorage.put(email, new OtpData(generatedOtp));

            // Crear y enviar el mensaje
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Confirmación de Código OTP - NewLang");
            message.setText(String.format(
                    "Buen día, %s,\n\n" +
                            "Su código OTP es: %s\n\n" +
                            "Este código es válido por 5 minutos.\n\n" +
                            "Si usted no solicitó este código, ignore este mensaje.\n\n" +
                            "Saludos,\nEquipo NewLang",
                    user.getNameUser(),
                    generatedOtp
            ));

            javaMailSender.send(message);

            // Log para debugging (remover en producción)
            System.out.println("OTP enviado exitosamente a: " + email);

        } catch (MailAuthenticationException e) {
            // Limpiar el OTP almacenado si falló el envío
            otpStorage.remove(email);
            throw new RuntimeException("Error de autenticación del correo: " + e.getMessage(), e);
        } catch (MailException e) {
            // Limpiar el OTP almacenado si falló el envío
            otpStorage.remove(email);
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        } catch (Exception e) {
            // Limpiar el OTP almacenado si falló el envío
            otpStorage.remove(email);
            throw new RuntimeException("Error inesperado al procesar la solicitud: " + e.getMessage(), e);
        }
    }

    // Valida que el OTP sea correcto y no haya expirado
    public boolean verifyOtp(String email, String otp) {
        if (email == null || email.trim().isEmpty() || otp == null || otp.trim().isEmpty()) {
            return false;
        }

        OtpData otpData = otpStorage.get(email);

        if (otpData == null) {
            return false; // No hay OTP para este email
        }

        if (otpData.isExpired()) {
            // Limpiar OTP expirado
            otpStorage.remove(email);
            return false;
        }

        boolean isValid = otpData.getOtp().equals(otp.trim());

        if (isValid) {
            // Marcar email como verificado
            verifiedEmails.put(email, email);
            // Limpiar el OTP usado
            otpStorage.remove(email);
        }

        return isValid;
    }

    public void updatePassword(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía");
        }

        Optional<User> userEmail = userRepository.findByEmail(email);

        if (userEmail.isEmpty()) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        try {
            User user = userEmail.get();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);

            // Log para debugging (remover en producción)
            System.out.println("Contraseña actualizada exitosamente para: " + email);

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la contraseña: " + e.getMessage(), e);
        }
    }

    public String getVerifiedEmail() {
        // Retorna el primer email verificado (en un escenario real,
        // deberías manejar esto de forma diferente)
        return verifiedEmails.isEmpty() ? null : verifiedEmails.keySet().iterator().next();
    }

    public void clearVerifiedUser() {
        verifiedEmails.clear();
        // También limpiar OTPs expirados
        clearExpiredOtps();
    }

    public void clearVerifiedUser(String email) {
        if (email != null) {
            verifiedEmails.remove(email);
            otpStorage.remove(email);
        }
    }

    // Método para limpiar OTPs expirados periódicamente
    private void clearExpiredOtps() {
        otpStorage.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    // Método para verificar si un email está verificado
    public boolean isEmailVerified(String email) {
        return verifiedEmails.containsKey(email);
    }

    // Método de utilidad para debugging
    public int getActiveOtpCount() {
        clearExpiredOtps();
        return otpStorage.size();
    }
}