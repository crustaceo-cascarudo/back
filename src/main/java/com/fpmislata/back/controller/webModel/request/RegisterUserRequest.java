package com.fpmislata.back.controller.webModel.request;

import com.fpmislata.back.domain.enumerado.Role;

public record RegisterUserRequest(
    String name,
    String password,
    Role role
) {

}
