package com.codewithmanas.skillexplatformbackend.exception;

import com.codewithmanas.skillexplatformbackend.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {

        ApiResponse<Void> apiResponse = new ApiResponse<>(
                400,
                "Email address already exists",
                null,
                ex.getMessage(),
                UUID.randomUUID().toString(),
                "/api/auth/register"
        );

        return ResponseEntity.badRequest().body(apiResponse);
    }
}
