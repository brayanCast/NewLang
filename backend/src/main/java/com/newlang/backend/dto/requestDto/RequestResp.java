package com.newlang.backend.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.newlang.backend.dto.responseDto.LearningRoutineResponseDTO;
import com.newlang.backend.entity.User;
import com.newlang.backend.enums.Role;
import java.util.*;

@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestResp {

    private Long idUser;
    private String nameUser;
    private String idNumber;
    private String email;
    private Role role;
    private User users;
    private RequestResp user;
    private List<LearningRoutineResponseDTO> routines;
    private List<User> userList;
    private String password;
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;

    public RequestResp() {
    }

    public RequestResp(Long idUser, String nameUser, String idNumber, String email, Role role, User users, List<User> userList, String password, int statusCode, String error, String message, String token, String expirationTime, String refreshToken) {
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.idNumber = idNumber;
        this.email = email;
        this.role = role;
        this.users = users;
        this.userList = userList;
        this.password = password;
        this.statusCode = statusCode;
        this.error = error;
        this.message = message;
        this.token = token;
        this.expirationTime = expirationTime;
        this.refreshToken = refreshToken;
    }

    public RequestResp(Long idUser, String nameUser, String idNumber, String email, Role role) {
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.idNumber = idNumber;
        this.email = email;
        this.role = role;
    }

    public RequestResp(RequestResp user, List<LearningRoutineResponseDTO> routines) {
        this.user = user;
        this.routines = routines;
    }



    public Long getIdUser() {return idUser;}

    public void setIdUser(Long idUser) {this.idUser = idUser;}

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    public RequestResp getUser() {return user;}

    public void setUser(RequestResp user) {this.user = user;}

    public List<LearningRoutineResponseDTO> getRoutines() {return routines;}

    public void setRoutines(List<LearningRoutineResponseDTO> routines) {this.routines = routines;}

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
