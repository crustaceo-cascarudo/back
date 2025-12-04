package com.fpmislata.back.controller.webModel.request;

import jakarta.validation.constraints.Pattern;

public record RegisterUserRequest(
    String name,
    String password,
    @Pattern(regexp = "^(ADMIN|NORMAL)$", message = "Role must be either ADMIN or NORMAL")
    String role
) {

}
