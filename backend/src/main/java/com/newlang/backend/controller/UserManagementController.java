package com.newlang.backend.controller;

import com.newlang.backend.dto.RequestResp;
import com.newlang.backend.entity.User;
import com.newlang.backend.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserManagementController {

    @Autowired
    private UsersManagementService usersManagementService;

    @PostMapping ("/auth/register")
    public ResponseEntity<RequestResp> register(@RequestBody RequestResp regist) {
        return ResponseEntity.ok(usersManagementService.register(regist));
    }

    @PostMapping ("/auth/login")
    public ResponseEntity<RequestResp> login(@RequestBody RequestResp request) {
        return ResponseEntity.ok(usersManagementService.login(request));
    }

    @PostMapping ("/auth/refresh")
    public ResponseEntity<RequestResp> refreshToken(@RequestBody RequestResp request) {
        return ResponseEntity.ok(usersManagementService.refreshToken(request));
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
