package com.fpmislata.back.domain.enumerado;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void fromString_validUpperCase_shouldReturnRole() {
        assertEquals(Role.ADMIN, Role.fromString("ADMIN"));
        assertEquals(Role.NORMAL, Role.fromString("NORMAL"));
    }

    @Test
    void fromString_validLowerCase_shouldReturnRole() {
        assertEquals(Role.ADMIN, Role.fromString("admin"));
        assertEquals(Role.NORMAL, Role.fromString("normal"));
    }

    @Test
    void fromString_validMixedCase_shouldReturnRole() {
        assertEquals(Role.ADMIN, Role.fromString("Admin"));
    }

    @Test
    void fromString_invalidValue_shouldThrowIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Role.fromString("SUPERUSER"));
        assertTrue(ex.getMessage().contains("Invalid role"));
    }

    @Test
    void fromString_null_shouldThrowIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Role.fromString(null));
        assertTrue(ex.getMessage().contains("cannot be null"));
    }
}
