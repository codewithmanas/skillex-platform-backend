package com.codewithmanas.skillexplatformbackend.controller;

import com.codewithmanas.skillexplatformbackend.dto.UserResponseDTO;
import com.codewithmanas.skillexplatformbackend.entity.User;
import com.codewithmanas.skillexplatformbackend.mapper.UserMapper;
import com.codewithmanas.skillexplatformbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {

        List<User> users = userService.getUsers();

        List<UserResponseDTO> userResponseDTOS = users.stream().map(user -> UserMapper.toDTO(user)).toList();

        return ResponseEntity.ok().body(userResponseDTOS);

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
