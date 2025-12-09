package com.fpmislata.back.domain.service;

import java.util.List;
import java.util.Optional;

import com.fpmislata.back.domain.service.dto.UserDto;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto);

    String logByName(String name, String password);

    Optional<UserDto> findById(Long id);

    List<UserDto> findByName(String name);

    List<UserDto> findAll();

    void delete(Long id);
}
