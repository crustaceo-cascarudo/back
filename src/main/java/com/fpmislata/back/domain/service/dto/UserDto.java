package com.fpmislata.back.domain.service.dto;

import com.fpmislata.back.domain.enumerado.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserDto(
    Long id,
    @NotNull(message = "Name cannot be null") String name,
    @NotNull(message = "Email cannot be null") @Email String email,
    @NotNull(message = "Password cannot be null") String plainPassword,
    String passwordHash,
    @NotNull(message = "Role cannot be null") Role role) {

}
