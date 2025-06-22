package com.codewithmanas.skillexplatformbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @PostMapping
    public ResponseEntity<String> createProjectListing(@RequestBody String body) {
        return ResponseEntity.ok().body("Project created successfully");
    }

    @GetMapping
    public ResponseEntity<String> getProjectListing() {
        return ResponseEntity.ok().body("Project fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProjectDetails() {
        return ResponseEntity.ok().body("Project details fetched successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProjectDetails(@RequestBody String body) {
        return ResponseEntity.ok().body("Project details updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProjectDetails() {
        return ResponseEntity.ok().body("Project details deleted successfully");
    }

}
