package com.newlang.backend.service;

import com.newlang.backend.entity.User;
import com.newlang.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Estructura thread-safe para almacenar OTPs con su tiempo de expiración
    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();

    // Estructura thread-safe para usuarios verificados (email -> timestamp de verificación)
    private final Map<String, LocalDateTime> verifiedUsers = new ConcurrentHashMap<>();

    private static final int OTP_EXPIRATION_MINUTES = 5;
    private static final int VERIFIED_USER_EXPIRATION_MINUTES = 10;

    private static class OtpData {
        String otp;
        LocalDateTime expirationTime;

        OtpData(String otp, LocalDateTime expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }
    }

    public void sendOtp(String email) {
        // Validar que el email exista
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("El email no está registrado"));

        // Generar OTP de 6 dígitos
        String otp = generateOtp();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES);

        // Almacenar OTP con su tiempo de expiración
        otpStorage.put(email, new OtpData(otp, expirationTime));

        // Enviar email
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Código de verificación");
            message.setText("Tu código OTP es: " + otp + "\nEste código expirará en " +
                    OTP_EXPIRATION_MINUTES + " minutos.");
            mailSender.send(message);
        } catch (Exception e) {
            otpStorage.remove(email); // Limpiar si falla el envío
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
        }
    }

    public boolean verifyOtp(String email, String otp) {
        OtpData otpData = otpStorage.get(email);

        if (otpData == null) {
            return false;
        }

        // Verificar si el OTP ha expirado
        if (LocalDateTime.now().isAfter(otpData.expirationTime)) {
            otpStorage.remove(email);
            return false;
        }

        // Verificar si el OTP coincide
        if (otpData.otp.equals(otp)) {
            // Marcar al usuario como verificado
            verifiedUsers.put(email, LocalDateTime.now().plusMinutes(VERIFIED_USER_EXPIRATION_MINUTES));
            // Limpiar el OTP usado
            otpStorage.remove(email);
            return true;
        }

        return false;
    }

    public void updatePassword(String email, String newPassword) {
        // Verificar que el usuario esté en la lista de verificados
        LocalDateTime verificationExpiry = verifiedUsers.get(email);

        if (verificationExpiry == null) {
            throw new RuntimeException("No se ha verificado el OTP para este usuario");
        }

        // Verificar que la verificación no haya expirado
        if (LocalDateTime.now().isAfter(verificationExpiry)) {
            verifiedUsers.remove(email);
            throw new RuntimeException("La verificación ha expirado. Por favor, solicita un nuevo OTP");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Limpiar la verificación después de actualizar la contraseña
        verifiedUsers.remove(email);
    }

    public boolean isUserVerified(String email) {
        LocalDateTime verificationExpiry = verifiedUsers.get(email);

        if (verificationExpiry == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(verificationExpiry)) {
            verifiedUsers.remove(email);
            return false;
        }

        return true;
    }

    public void clearVerifiedUser(String email) {
        verifiedUsers.remove(email);
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Método para limpiar OTPs y verificaciones expiradas (puede ejecutarse periódicamente)
    public void cleanupExpiredData() {
        LocalDateTime now = LocalDateTime.now();

        // Limpiar OTPs expirados
        otpStorage.entrySet().removeIf(entry -> now.isAfter(entry.getValue().expirationTime));

        // Limpiar verificaciones expiradas
        verifiedUsers.entrySet().removeIf(entry -> now.isAfter(entry.getValue()));
    }
}











/*package com.newlang.backend.service;

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
}*/