package com.newlang.backend.service;

import com.newlang.backend.dto.requestDto.RequestResp;
import com.newlang.backend.entity.User;
import com.newlang.backend.enums.Role;

import com.newlang.backend.exceptions.EmailAlreadyExistException;
import com.newlang.backend.exceptions.EmailNotFoundException;
import com.newlang.backend.exceptions.IDNumberCannotBeVoidException;
import com.newlang.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UsersManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersDetailsService usersDetailsService;


    public RequestResp register(RequestResp registrationRequest) {
        RequestResp resp = new RequestResp();
        Role role = registrationRequest.getRole();
        String email = registrationRequest.getEmail();

        Optional<User> existEmail = userRepository.findByEmail(email);
        if (existEmail.isPresent()) {
            throw new EmailAlreadyExistException("El email ya existe");
        }

        User user = new User();
        user.setEmail(registrationRequest.getEmail());
        user.setNameUser(registrationRequest.getNameUser());
        if (registrationRequest.getRole() == Role.ADMIN) {
            if(registrationRequest.getIdNumber() == null || registrationRequest.getIdNumber().equals(" ")) {
                throw new IDNumberCannotBeVoidException("El número de identificación del usuario no puede esta vacío");
            }
            user.setIdNumber(registrationRequest.getIdNumber());
        }
        user.setRole(registrationRequest.getRole());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        User userResult = userRepository.save(user);

        if (userResult.getIdUser() > 0) {
            resp.setUsers((userResult));
            resp.setMessage("User Saved Successfully");
            resp.setStatusCode(200);
        }
        return resp;
    }

    public RequestResp login(RequestResp loginRequest) {
        RequestResp response = new RequestResp();
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("El email " + email + " no fue encontrado"));

        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24 Hrs");
            response.setMessage("Successful Logged In");

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Error de autenticación. Verifique su usuario y contraseña. " + e.getMessage());
        }
        return  response;
    }

    public RequestResp refreshToken(RequestResp refreshTokenRequest) {
        RequestResp response = new RequestResp();
        try {

            String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            User users = userRepository.findByEmail(email).orElseThrow();

            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(users);

                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24 hours");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return  response;
        }

    }

    public RequestResp getAllUsers() {
        RequestResp requestResp = new RequestResp();
        try {

            List<User> result = userRepository.findAll();
            if (!result.isEmpty()) {
                requestResp.setUserList(result);
                requestResp.setStatusCode(200);
                requestResp.setMessage("Successful");
            } else {
                requestResp.setStatusCode(404);
                requestResp.setMessage("No users found");
            }
            return  requestResp;
        } catch (Exception e) {
            requestResp.setStatusCode(500);
            requestResp.setMessage("Error occurred: " + e.getMessage());
            return requestResp;
        }
    }

    public RequestResp getUserById(Long id) {
        RequestResp requestResp = new RequestResp();

        try {
            User userById = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not Found"));
            requestResp.setUsers(userById);
            requestResp.setStatusCode(200);
            requestResp.setMessage("User with id: " + id + " found successfully");
        } catch (Exception e) {
            requestResp.setStatusCode(500);
            requestResp.setMessage("Error occurred: " + e.getMessage());
        }
        return requestResp;
    }

    public RequestResp deleteUser(Long userId) {
        RequestResp requestResp = new RequestResp();

        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                userRepository.deleteById(userId);
                requestResp.setStatusCode(200);
                requestResp.setMessage("User deleted successfully");
            } else {
                requestResp.setStatusCode(404);
                requestResp.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            requestResp.setStatusCode(500);
            requestResp.setMessage("Error ocurred while deleting user: " + e.getMessage());
        }
        return requestResp;
    }

    public RequestResp updateUser(Long userId, User updateUser) {
        RequestResp requestResp = new RequestResp();

        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();
                existingUser.setEmail(updateUser.getEmail());
                existingUser.setNameUser(updateUser.getNameUser());

                //revisa si la contraseña está presente en la solicitud
                if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
                    //Decodifica la contraseña y la actualiza
                    existingUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
                }

                User savdUser = userRepository.save(existingUser);
                requestResp.setUsers(savdUser);
                requestResp.setStatusCode(200);
                requestResp.setMessage("User updated successfully");

            } else {
                requestResp.setStatusCode(404);
                requestResp.setMessage("User not found for update");
            }
        } catch (Exception e) {
            requestResp.setStatusCode(500);
            requestResp.setMessage("Error ocurred while updating user: " + e.getMessage());
        }
        return requestResp;
    }

    public RequestResp getMyInfo(String email) {
        RequestResp requestResp = new RequestResp();
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                requestResp.setUsers(userOptional.get());
                requestResp.setStatusCode(200);
                requestResp.setMessage("successful");
            } else {
                requestResp.setStatusCode(404);
                requestResp.setMessage("User with email " + email + " not found");

            }
        } catch (Exception e) {
            requestResp.setStatusCode(500);
            requestResp.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return requestResp;
    }

    public RequestResp updateMyProfile(String email, RequestResp updateProfile ) {
        RequestResp resp = new RequestResp();

        try {
            Optional<User> updateUserOptional = userRepository.findByEmail(email);

            if (updateUserOptional.isPresent()) {
                User userToUpdate = updateUserOptional.get();

                if (updateProfile.getEmail() != null && !updateProfile.getEmail().isEmpty()) {
                    userToUpdate.setEmail(updateProfile.getEmail());
                }

                if (updateProfile.getNameUser() != null && !updateProfile.getNameUser().isEmpty()) {
                    userToUpdate.setNameUser(updateProfile.getNameUser());
                }

                if (updateProfile.getIdNumber() != null && !updateProfile.getIdNumber().isEmpty()) {
                    userToUpdate.setIdNumber(updateProfile.getIdNumber());
                }

                User savedUser = userRepository.save(userToUpdate);
                resp.setStatusCode(200);
                resp.setMessage("User updated successfully");
            }

        } catch (EmailNotFoundException e){
            resp.setStatusCode(403);
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage(e.getMessage());
        }

        return resp;
    }

    public RequestResp deleteMyProfile(String email) {
        RequestResp resp = new RequestResp();

        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                userRepository.delete(userOptional.get());
                resp.setStatusCode(200);
                resp.setMessage("Perfil eliminado exitosamente");
            } else {
                resp.setStatusCode(404);
                resp.setMessage("No se encontró el perfil para eliminar");
            }

        } catch (Exception e){
            resp.setStatusCode(500);
            resp.setMessage("Error al eliminar el perfil " + e.getMessage());
        }

        return resp;
    }
}
