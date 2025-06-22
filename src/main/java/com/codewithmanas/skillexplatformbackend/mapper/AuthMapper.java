package com.codewithmanas.skillexplatformbackend.mapper;

import com.codewithmanas.skillexplatformbackend.dto.AuthRequestDTO;
import com.codewithmanas.skillexplatformbackend.dto.AuthResponseDTO;
import com.codewithmanas.skillexplatformbackend.entity.User;

public class AuthMapper {

    public static AuthResponseDTO toDTO(User user) {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();

        authResponseDTO.setEmail(user.getEmail());
        authResponseDTO.setPassword(user.getHashedPassword());

        return  authResponseDTO;
    }

    public static User toEntity(AuthRequestDTO authRequestDTO) {
        User user = new User();
        user.setEmail(authRequestDTO.getEmail());
        user.setHashedPassword(authRequestDTO.getPassword());

        return user;
    }


}
