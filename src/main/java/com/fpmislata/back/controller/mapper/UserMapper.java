package com.fpmislata.back.controller.mapper;

import com.fpmislata.back.controller.webModel.request.RegisterUserRequest;
import com.fpmislata.back.controller.webModel.response.UserResponse;
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

  public UserDto fromUserRequestToUserDto(RegisterUserRequest registerUserRequest) {
    if (registerUserRequest == null) {
      return null;
    }
    return new UserDto(
        null,
        registerUserRequest.name(),
        registerUserRequest.email(),
        registerUserRequest.password(),
        null,
        registerUserRequest.role());
  }

  public UserResponse fromUserDtoToUserResponse(UserDto userDto) {
    if (userDto == null) {
      return null;
    }
    return new UserResponse(
        userDto.id(),
        userDto.name(),
        userDto.email(),
        userDto.role());
  }

}
