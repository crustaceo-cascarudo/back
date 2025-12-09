package com.fpmislata.back.domain.mapper;

import com.fpmislata.back.domain.model.User;
import com.fpmislata.back.domain.repository.entity.UserEntity;
import com.fpmislata.back.domain.service.dto.UserDto;

public class UserMapper {
    private static UserMapper instance;

    private UserMapper() {
    }

    public static UserMapper getInstance() {
        if (instance == null) {
            instance = new UserMapper();
        }
        return instance;
    }

    public UserDto fromUserToUserDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getId(),
                user.getName(),
                null,
                user.getPasswordHash(),
                user.getRole());
    }

    public User fromUserDtoToUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return new User(
                userDto.id(),
                userDto.name(),
                userDto.passwordHash(),
                userDto.role());
    }

    public UserEntity fromUserToUserEntity(User user) {
        if (user == null) {
            return null;
        }
        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getPasswordHash(),
                user.getRole());
    }

    public User fromUserEntityToUser(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return new User(
                userEntity.id(),
                userEntity.name(),
                userEntity.passwordHash(),
                userEntity.role());
    }
}
