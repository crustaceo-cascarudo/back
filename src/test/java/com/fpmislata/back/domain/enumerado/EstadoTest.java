package com.fpmislata.back.domain.enumerado;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoTest {

    @Test
    void fromString_validUpperCase_shouldReturnEstado() {
        assertEquals(Estado.CARRITO, Estado.fromString("CARRITO"));
        assertEquals(Estado.PENDIENTE, Estado.fromString("PENDIENTE"));
        assertEquals(Estado.PAGADO, Estado.fromString("PAGADO"));
        assertEquals(Estado.ENVIADO, Estado.fromString("ENVIADO"));
        assertEquals(Estado.CANCELADO, Estado.fromString("CANCELADO"));
    }

    @Test
    void fromString_validLowerCase_shouldReturnEstado() {
        assertEquals(Estado.CARRITO, Estado.fromString("carrito"));
        assertEquals(Estado.PENDIENTE, Estado.fromString("pendiente"));
    }

    @Test
    void fromString_validMixedCase_shouldReturnEstado() {
        assertEquals(Estado.PAGADO, Estado.fromString("Pagado"));
    }

    @Test
    void fromString_invalidValue_shouldThrowIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Estado.fromString("INVALIDO"));
        assertTrue(ex.getMessage().contains("Estado inválido"));
    }

    @Test
    void fromString_null_shouldThrowIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Estado.fromString(null));
        assertTrue(ex.getMessage().contains("no puede ser nulo"));
    }
}
