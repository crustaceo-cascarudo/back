package com.fpmislata.back.domain.repository.entity;

public record UserEntity(
    Long id,
    String name,
    String passwordHash,
    String role
) {

}
