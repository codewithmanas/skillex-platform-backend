package com.codewithmanas.skillexplatformbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id) {
        return ResponseEntity.ok().body("Update user successfully");
    }

    @PostMapping("/{id}/profile-image")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable String id) {
        return ResponseEntity.ok().body("Profile Picture Uploaded");
    }


}
