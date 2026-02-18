package com.fpmislata.back.web.webModel.response;

import com.fpmislata.back.domain.enumerado.Role;

public record UserResponse(
    Long id,
    String name,
    String email,
    Role role) {

}
