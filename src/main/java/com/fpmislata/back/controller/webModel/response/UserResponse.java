package com.fpmislata.back.controller.webModel.response;

import com.fpmislata.back.domain.enumerado.Role;

public record UserResponse(
    Long id,
    String name,
    Role role
) {

}
