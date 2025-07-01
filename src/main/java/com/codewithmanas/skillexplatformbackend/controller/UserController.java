package com.codewithmanas.skillexplatformbackend.controller;

import com.codewithmanas.skillexplatformbackend.dto.ChangePasswordRequestDTO;
import com.codewithmanas.skillexplatformbackend.dto.UserResponseDTO;
import com.codewithmanas.skillexplatformbackend.entity.User;
import com.codewithmanas.skillexplatformbackend.exception.MissingAuthorizationHeaderException;
import com.codewithmanas.skillexplatformbackend.mapper.UserMapper;
import com.codewithmanas.skillexplatformbackend.service.UserService;
import com.codewithmanas.skillexplatformbackend.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {

        List<User> users = userService.getUsers();

        List<UserResponseDTO> userResponseDTOS = users.stream().map(user -> UserMapper.toDTO(user)).toList();

        return ResponseEntity.ok().body(userResponseDTOS);

     }

     @PutMapping("/change-password")
     public ResponseEntity<ApiResponse<String>> changePassword(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO, HttpServletRequest httpServletRequest) {

        String path = httpServletRequest.getRequestURI();
        String requestId = UUID.randomUUID().toString();

         log.info("[requestId={}] change password request", requestId);

         // Validate header
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new MissingAuthorizationHeaderException("Missing or invalid Authorization header");
        }

        // Extract token
        String token = authHeader.substring(7); // Remove "Bearer " prefix

        boolean response = userService.changePassword(changePasswordRequestDTO, token);

         ApiResponse<String> apiResponse = new ApiResponse<>(
                 200,
                 "Password changed successfully",
                 "",
                 null,
                 requestId,
                 path
         );

        return ResponseEntity.ok().body(apiResponse);

     }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id) {
        return ResponseEntity.ok().body("Update user successfully");
    }

    @PostMapping("/{id}/profile-image")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable String id) {
        return ResponseEntity.ok().body("Profile Picture Uploaded");
    }

}
