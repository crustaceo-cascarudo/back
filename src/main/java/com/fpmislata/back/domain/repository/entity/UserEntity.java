package com.fpmislata.back.domain.repository.entity;

import com.fpmislata.back.domain.enumerado.Role;

public record UserEntity(
    Long id,
    String name,
    String email,
    String passwordHash,
    Role role) {

}
