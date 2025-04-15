package com.newlang.backend.controller;

import com.newlang.backend.dto.RequestResp;
import com.newlang.backend.entity.User;
import com.newlang.backend.exceptions.EmailAlreadyExistException;
import com.newlang.backend.service.OtpService;
import com.newlang.backend.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RestController
public class UserManagementController {

    @Autowired
    private UsersManagementService usersManagementService;

    @Autowired
    private OtpService otpService;

    @PostMapping ("/auth/register")
    public ResponseEntity<RequestResp> registerAdmin(@RequestBody RequestResp registerUser) {
        RequestResp errorResponse = new RequestResp();
        try{
            RequestResp response = usersManagementService.register(registerUser);
            return ResponseEntity.ok(response);

        } catch (EmailAlreadyExistException e) {
            errorResponse.setStatusCode(400);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            errorResponse.setStatusCode(500);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping ("/register")
    public ResponseEntity<RequestResp> registerUser(@RequestBody RequestResp registerUser) {
        RequestResp errorResponse = new RequestResp();
        
        try{
            RequestResp response = usersManagementService.register(registerUser);
            return ResponseEntity.ok(usersManagementService.register(response));

        } catch (EmailAlreadyExistException e) {
            errorResponse.setStatusCode(400);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            errorResponse.setStatusCode(500);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping ("/auth/login")
    public ResponseEntity<RequestResp> login(@RequestBody RequestResp request) throws Exception{
        return ResponseEntity.ok(usersManagementService.login(request));
    }

    @PostMapping ("/auth/refresh")
    public ResponseEntity<RequestResp> refreshToken(@RequestBody RequestResp request) {
        return ResponseEntity.ok(usersManagementService.refreshToken(request));
    }

    @PostMapping("/auth/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request)  {
        String email = request.get("email");

        try {
            otpService.sendOtp(email); //Llama al servicio para realizar el envío del otp al correo
            return ResponseEntity.ok("OTP enviado a " + email);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error enviando el correo: " + e.getMessage());
        }
    }

    @PostMapping("/auth/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        if (otpService.verifyOtp(email, otp)){
            return ResponseEntity.ok().body("OTP verificado con exito");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OTP inválido");
        }
    }

    //Metodo para la actualización de la contraseña siempre que el email esté verificado
    @PutMapping("/auth/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> request) {
        String password = request.get("password");
        String verifiedEmail = otpService.getVerifiedEmail();

        if(verifiedEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se ha verificado el OTP");
        }

        try{
            otpService.updatePassword(verifiedEmail, password);
            otpService.clearVerifiedUser();
            return ResponseEntity.ok("La contraseña se actualizó correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la contraseña " + e.getMessage());
        }
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<RequestResp> getAllUsers() {
        return ResponseEntity.ok(usersManagementService.getAllUsers());
    }

    @GetMapping("/admin/get-user/{userId}")
    public ResponseEntity<RequestResp> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(usersManagementService.getUserById(userId));
    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<RequestResp> updateUserById(@PathVariable Long userId, @RequestBody User requestResp) {
        return ResponseEntity.ok(usersManagementService.updateUser(userId, requestResp));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<RequestResp> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        RequestResp response = usersManagementService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<RequestResp> deleteUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(usersManagementService.deleteUser(userId));
    }
}
