package com.codewithmanas.skillexplatformbackend.exception;

import com.codewithmanas.skillexplatformbackend.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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
    public ResponseEntity<ApiResponse<Void>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, HttpServletRequest httpServletRequest) {

        String path = httpServletRequest.getRequestURI();
        String requestId = UUID.randomUUID().toString();

        log.warn("[requestId={}] Email already exists", requestId);

        ApiResponse<Void> apiResponse = new ApiResponse<>(
                400,
                "Email address already exists",
                null,
                ex.getMessage(),
                requestId,
                path
        );

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCredentialsException(InvalidCredentialsException ex) {

        ApiResponse<Void> apiResponse = new ApiResponse<>(
                400,
                "Invalid Credentials",
                null,
                ex.getMessage(),
                UUID.randomUUID().toString(),
                "/api/auth/login"
        );

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidTokenException(InvalidTokenException ex) {
        ApiResponse<Void> apiResponse = new ApiResponse<>(
                400,
                "Invalid or missing token",
                null,
                ex.getMessage(),
                UUID.randomUUID().toString(),
                "/api/auth/**"
        );

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(EmailAlreadyVerifiedException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailAlreadyVerifiedException(EmailAlreadyVerifiedException ex) {
        ApiResponse<Void> apiResponse = new ApiResponse<>(
                409,
                "Email Already Verified",
                null,
                ex.getMessage(),
                UUID.randomUUID().toString(),
                "/api/auth/verify-email"
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
    }



}
