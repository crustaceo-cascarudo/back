package com.fpmislata.back.domain.enumerado;

public enum Role {
    ADMIN,
    NORMAL;

    public static Role fromString(String role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role + ". Must be ADMIN or NORMAL");
        }
    }
}