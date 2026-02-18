package com.fpmislata.back.web.webModel.request;

import com.fpmislata.back.domain.enumerado.Role;

public record RegisterUserRequest(
    String name,
    String email,
    String password,
    Role role) {

}
