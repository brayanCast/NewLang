package com.newlang.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

public class GlobalExceptionHandler {
    @ExceptionHandler(WordAlreadyExistException.class)
    public ResponseEntity<Map<String, Object>> handleWordAlreadyExistException(
            WordAlreadyExistException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflict");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}
