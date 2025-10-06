package com.newlang.backend.controller;

import com.newlang.backend.dto.requestDto.RequestResp;
import com.newlang.backend.entity.User;
import com.newlang.backend.exceptions.EmailAlreadyExistException;
import com.newlang.backend.exceptions.EmailNotFoundException;
import com.newlang.backend.exceptions.UserNotFoundByIdException;
import com.newlang.backend.service.OtpService;
import com.newlang.backend.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security .core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RestController
public class UserManagementController {

    @Autowired
    private UsersManagementService usersManagementService;

    @Autowired
    private OtpService otpService;

    @PostMapping ("/register-admin")
    public ResponseEntity<RequestResp> registerAdmin(@RequestBody RequestResp registerUser) {
        RequestResp errorResponse = new RequestResp();
        try {
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
            return ResponseEntity.ok(response);

        } catch (EmailAlreadyExistException e) {
            errorResponse.setStatusCode(409);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            errorResponse.setStatusCode(500);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping ("/login")
    public ResponseEntity<RequestResp> login(@RequestBody RequestResp request) throws Exception{
        RequestResp errorResponse = new RequestResp();
        try {
            RequestResp response = usersManagementService.login(request);
            return ResponseEntity.ok(response);
        } catch (EmailNotFoundException e) {
            errorResponse.setStatusCode(404);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (BadCredentialsException e) {
            errorResponse.setStatusCode(401);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (Exception e) {
            errorResponse.setStatusCode(500);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping ("/auth/refresh")
    public ResponseEntity<RequestResp> refreshToken(@RequestBody RequestResp request) {
        return ResponseEntity.ok(usersManagementService.refreshToken(request));
    }

    @PostMapping("/send-otp")
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

    @PostMapping("/verify-otp")
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

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> request) {
        String password = request.get("password");
        String email = request.get("email"); // Ahora recibimos el email en el request

        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El email es requerido");
        }

        if (!otpService.isUserVerified(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("No se ha verificado el OTP para este email");
        }

        try {
            otpService.updatePassword(email, password);
            otpService.clearVerifiedUser(email);
            return ResponseEntity.ok("La contraseña se actualizó correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la contraseña: " + e.getMessage());
        }
    }
    /*
    @PutMapping("/update-password")
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
    }*/

    @GetMapping("/auth/get-profile")
    public ResponseEntity<RequestResp> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        RequestResp response = usersManagementService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<RequestResp> getAllUsers() {
        return ResponseEntity.ok(usersManagementService.getAllUsers());
    }

    @GetMapping("/admin/get-user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        try {
            RequestResp user = usersManagementService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundByIdException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<RequestResp> updateUserById(@PathVariable Long userId, @RequestBody User requestResp) {
        return ResponseEntity.ok(usersManagementService.updateUser(userId, requestResp));
    }

    @PutMapping("/auth/update-profile")
    public ResponseEntity<RequestResp> updateMyProfile(@RequestBody RequestResp requestResp) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        RequestResp resp = usersManagementService.updateMyProfile(email, requestResp);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<RequestResp> deleteUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(usersManagementService.deleteUser(userId));
    }

    @DeleteMapping("/auth/delete-profile")
    public ResponseEntity<RequestResp> deleteMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        RequestResp resp = usersManagementService.deleteMyProfile(email);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }
}
