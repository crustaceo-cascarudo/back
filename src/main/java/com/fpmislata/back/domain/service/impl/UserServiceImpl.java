package com.fpmislata.back.domain.service.impl;

import java.util.List;
import java.util.Optional;

import com.fpmislata.back.domain.mapper.UserMapper;
import com.fpmislata.back.domain.repository.UserRepository;
import com.fpmislata.back.domain.repository.entity.UserEntity;
import com.fpmislata.back.domain.service.PasswordEncoderService;
import com.fpmislata.back.domain.service.UserService;
import com.fpmislata.back.domain.service.dto.UserDto;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoderService passwordEncoderService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
    }

    @Override
    public UserDto create(UserDto userDto) {
        List<UserDto> existingUsersByName = findByName(userDto.name());
        if (!existingUsersByName.isEmpty()) {
            throw new IllegalArgumentException("User with name " + userDto.name() + " already exists.");
        }
        String hashedpassword = passwordEncoderService.encode(userDto.plainPassword());
        userDto = new UserDto(
                null,
                userDto.name(),
                userDto.plainPassword(),
                hashedpassword,
                userDto.role());
        UserEntity userEntity = UserMapper.getInstance()
                .fromUserToUserEntity(UserMapper.getInstance().fromUserDtoToUser(userDto));
        userEntity = userRepository.save(userEntity);
        return UserMapper.getInstance().fromUserToUserDto(UserMapper.getInstance().fromUserEntityToUser(userEntity));
    }

    @Override
    public UserDto update(UserDto userDto) {
        Optional<UserDto> existingUser = findById(userDto.id());
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User with id " + userDto.id() + " does not exist.");
        }
        UserEntity userEntity = UserMapper.getInstance()
                .fromUserToUserEntity(UserMapper.getInstance().fromUserDtoToUser(userDto));
        userRepository.save(userEntity);
        return UserMapper.getInstance().fromUserToUserDto(UserMapper.getInstance().fromUserEntityToUser(userEntity));
    }

    @Override
    public void delete(Long id) {
        Optional<UserDto> existingUser = findById(id);
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User with id " + id + " does not exist.");
        }
        if (existingUser.get().role().equals("ADMIN")) {
            throw new IllegalArgumentException("Cannot delete an ADMIN user.");
        }
        userRepository.delete(id);
    }

    @Override
    public String logByName(String name, String password) {
        List<UserDto> existingUsers = findByName(name);
        if (existingUsers.isEmpty()) {
            throw new IllegalArgumentException("User with name " + name + " does not exist.");
        }
        boolean passwordMatches = passwordEncoderService.verify(password, existingUsers.get(0).passwordHash());
        if (!passwordMatches) {
            throw new IllegalArgumentException("Incorrect password for user " + name + ".");
        }

        // Crear token de sesión
        String sessionToken = userRepository.createSessionToken(existingUsers.get(0).id());

        // Devolver DTO con usuario y token
        return sessionToken;
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper.getInstance()::fromUserEntityToUser)
                .map(UserMapper.getInstance()::fromUserToUserDto);
    }

    @Override
    public List<UserDto> findByName(String name) {
        return userRepository.findByName(name).stream()
                .map(UserMapper.getInstance()::fromUserEntityToUser)
                .map(UserMapper.getInstance()::fromUserToUserDto)
                .toList();
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper.getInstance()::fromUserEntityToUser)
                .map(UserMapper.getInstance()::fromUserToUserDto)
                .toList();
    }
}
