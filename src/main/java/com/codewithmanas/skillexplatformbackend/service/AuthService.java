package com.codewithmanas.skillexplatformbackend.service;

import com.codewithmanas.skillexplatformbackend.dto.AuthRequestDTO;
import com.codewithmanas.skillexplatformbackend.dto.AuthResponseDTO;
import com.codewithmanas.skillexplatformbackend.entity.User;
import com.codewithmanas.skillexplatformbackend.exception.EmailAlreadyExistsException;
import com.codewithmanas.skillexplatformbackend.mapper.AuthMapper;
import com.codewithmanas.skillexplatformbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponseDTO registerUser(AuthRequestDTO authRequestDTO) {

        boolean userExist = userRepository.existsByEmail(authRequestDTO.getEmail());

        if(userExist) {
            throw new EmailAlreadyExistsException("Email is already in use.");
        }

        User user = userRepository.save(AuthMapper.toEntity(authRequestDTO));
        AuthResponseDTO authResponseDTO = AuthMapper.toDTO(user);

        return authResponseDTO;
    }

    public boolean loginUser(AuthRequestDTO authRequestDTO) {
        boolean userExist = userRepository.existsByEmail(authRequestDTO.getEmail());
        Optional<User> user =  userRepository.findByEmail(authRequestDTO.getEmail());

        if(!userRepository.existsByEmail(authRequestDTO.getEmail())) {
            System.out.println("Email not found.");
        }

        if(user.isPresent()) {
            System.out.println("PRESENT");
        }

        System.out.println("User exist: " + userExist);

        return userExist;
    }

}
