package com.codewithmanas.skillexplatformbackend.service;

import com.codewithmanas.skillexplatformbackend.dto.RegisterRequestDTO;
import com.codewithmanas.skillexplatformbackend.dto.RegisterResponseDTO;
import com.codewithmanas.skillexplatformbackend.entity.User;
import com.codewithmanas.skillexplatformbackend.exception.EmailAlreadyExistsException;
import com.codewithmanas.skillexplatformbackend.mapper.AuthMapper;
import com.codewithmanas.skillexplatformbackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponseDTO registerUser(RegisterRequestDTO registerRequestDTO) {

        // Sanitize and Normalize email and password
        String email = registerRequestDTO.getEmail().trim().toLowerCase();

        // Check if email already exists
        boolean userExist = userRepository.existsByEmail(registerRequestDTO.getEmail());

        if(userExist) {
            throw new EmailAlreadyExistsException("Email is already in use.");
        }

        String encodedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());

        User user = userRepository.save(AuthMapper.toEntity(registerRequestDTO, encodedPassword));
        RegisterResponseDTO registerResponseDTO = AuthMapper.toDTO(user);

        // Send email verification
        // TODO: To be implemented



        return registerResponseDTO;
    }

    public boolean loginUser(RegisterRequestDTO registerRequestDTO) {
        boolean userExist = userRepository.existsByEmail(registerRequestDTO.getEmail());
        Optional<User> user =  userRepository.findByEmail(registerRequestDTO.getEmail());

        if(!userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            System.out.println("Email not found.");
        }

        if(user.isPresent()) {
            System.out.println("PRESENT");
        }

        System.out.println("User exist: " + userExist);

        return userExist;
    }

}
