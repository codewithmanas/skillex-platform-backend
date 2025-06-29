package com.codewithmanas.skillexplatformbackend.mapper;

import com.codewithmanas.skillexplatformbackend.dto.RegisterResponseDTO;
import com.codewithmanas.skillexplatformbackend.dto.RegisterRequestDTO;
import com.codewithmanas.skillexplatformbackend.entity.User;

public class AuthMapper {

    public static RegisterResponseDTO toDTO(User user) {
        RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO();

        registerResponseDTO.setEmail(user.getEmail());

        return  registerResponseDTO;
    }

    public static User toEntity(RegisterRequestDTO registerRequestDTO, String encodedPassword) {
        User user = new User();
        user.setEmail(registerRequestDTO.getEmail());
        user.setHashedPassword(encodedPassword);

        return user;
    }

}
