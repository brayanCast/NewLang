package com.newlang.backend.service;

import com.newlang.backend.dto.RequestResp;
import com.newlang.backend.entity.User;
import com.newlang.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    public RequestResp register(RequestResp registrationRequest) {
        RequestResp resp = new RequestResp();

        try {
            User user = new User();
            user.setEmail(registrationRequest.getEmail());
            user.setNameUser(registrationRequest.getNameUser());
            user.setRole(registrationRequest.getRole());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            User userResult = userRepository.save(user);

            if (userResult.getIdUser()>0) {
                resp.setUsers((userResult));
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public RequestResp login(RequestResp loginRequest) {
        RequestResp response = new RequestResp();
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getPassword()));
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("60 min");
            response.setMessage("Successful Logged In");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
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

                //revisa si la contraseña está presente en la solcitud
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


}
