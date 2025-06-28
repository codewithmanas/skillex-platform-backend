package com.codewithmanas.skillexplatformbackend.mapper;

import com.codewithmanas.skillexplatformbackend.dto.UserResponseDTO;
import com.codewithmanas.skillexplatformbackend.dto.UserSignupDTO;
import com.codewithmanas.skillexplatformbackend.entity.User;

public class UserMapper {
    private final User user;

    public UserMapper(User user) {
        this.user = user;
    }

    public User toEntity(UserSignupDTO userSignupDTO) {
        user.setEmail(userSignupDTO.getEmail());
        user.setHashedPassword(userSignupDTO.getPassword());

        return user;
    }

    public static UserResponseDTO toDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setFullName(user.getFullName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setProfileImageUrl(user.getProfileImageUrl());
        userResponseDTO.setSkills(user.getSkills());
        userResponseDTO.setInterests(user.getInterests());
        userResponseDTO.setBio(user.getBio());
        userResponseDTO.setRating(user.getRating());
        userResponseDTO.setRatingCount(user.getRatingCount());
        userResponseDTO.setRatingAvg(user.getRatingAvg());
        userResponseDTO.setVerified(user.isVerified());
        userResponseDTO.setPortfolioUrl(user.getPortfolioUrl());
        userResponseDTO.setCountry(user.getCountry());
        userResponseDTO.setCreatedAt(user.getCreatedAt());

        return userResponseDTO;
    }

}
