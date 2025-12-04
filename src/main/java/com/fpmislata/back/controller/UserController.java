package com.fpmislata.back.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.fpmislata.back.controller.mapper.UserMapper;
import com.fpmislata.back.controller.webModel.request.LoginUserRequest;
import com.fpmislata.back.controller.webModel.request.RegisterUserRequest;
import com.fpmislata.back.controller.webModel.response.LoginResponse;
import com.fpmislata.back.controller.webModel.response.UserResponse;
import com.fpmislata.back.domain.service.UserService;
import com.fpmislata.back.domain.service.dto.UserDto;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterUserRequest request) {
        UserDto userDto = UserMapper.getInstance().fromUserRequestToUserDto(request);
        UserDto createdUser = userService.create(userDto);
        UserResponse response = UserMapper.getInstance().fromUserDtoToUserResponse(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserRequest request) {
        String token = userService.logByName(request.name(), request.password());
        UserDto userDto = userService.findByName(request.name())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserResponse userResponse = UserMapper.getInstance().fromUserDtoToUserResponse(userDto);
        LoginResponse response = new LoginResponse(token, userResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        UserDto userDto = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        UserResponse response = UserMapper.getInstance().fromUserDtoToUserResponse(userDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody RegisterUserRequest request) {
        UserDto userDto = UserMapper.getInstance().fromUserRequestToUserDto(request);
        UserDto updatedUser = userService.update(new UserDto(
                id,
                userDto.name(),
                userDto.password(),
                userDto.passwordHash(),
                userDto.role()));
        UserResponse response = UserMapper.getInstance().fromUserDtoToUserResponse(updatedUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
