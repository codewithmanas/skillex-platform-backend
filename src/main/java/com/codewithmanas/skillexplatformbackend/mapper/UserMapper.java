package com.codewithmanas.skillexplatformbackend.mapper;

import com.codewithmanas.skillexplatformbackend.dto.UserSignupDTO;
import com.codewithmanas.skillexplatformbackend.entity.User;
import com.codewithmanas.skillexplatformbackend.repository.UserRepository;

public class UserMapper {
    private User user;

    public UserMapper(User user) {
        this.user = user;
    }

    public User toEntity(UserSignupDTO userSignupDTO) {
        user.setEmail(userSignupDTO.getEmail());
        user.setHashedPassword(userSignupDTO.getPassword());

        return user;
    }
}
