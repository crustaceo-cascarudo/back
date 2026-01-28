package com.fpmislata.back.domain.service;

import java.util.List;
import java.util.Optional;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.service.dto.UserDto;

public interface UserService {
  UserDto create(UserDto userDto);

  UserDto update(UserDto userDto);

  String logByEmail(String email, String password);

  void logout(String token);

  Optional<UserDto> findById(Long id);

  List<UserDto> findByName(String name);

  List<UserDto> findByEmail(String email);

  Page<UserDto> findAll(int page, int size);

  void delete(Long id);
}
