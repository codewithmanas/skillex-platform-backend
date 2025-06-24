package com.codewithmanas.skillexplatformbackend.exception;

import com.codewithmanas.skillexplatformbackend.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleValidationException(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ":" + error.getDefaultMessage()).toList();

        ApiResponse<List<String>> response = new ApiResponse<>(
          400,
          "Invalid Request",
          null,
          errors,
          UUID.randomUUID().toString(),
          ""
        );

        return ResponseEntity.badRequest().body(response);
    }


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
