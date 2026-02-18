package com.fpmislata.back.infrastructure.payment.dto;

/**
 * DTO que representa un usuario del banco.
 * Coincide con UserDto del proyecto banco.
 */
public record BankUserDto(
    Long id,
    String name,
    String surname1,
    String surname2,
    String dni,
    String plainPassword,
    String passwordHash
) {
}
