package com.fpmislata.back.domain.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserDto(
        Long id,
        @NotNull(message = "Name cannot be null") 
        String name,
        @NotNull(message = "Password cannot be null") 
        String password,
        String passwordHash,
        @NotNull(message = "Role cannot be null") 
        @Pattern(regexp = "^(ADMIN|NORMAL)$", message = "Role must be either ADMIN or NORMAL") 
        String role
    ) {

}
