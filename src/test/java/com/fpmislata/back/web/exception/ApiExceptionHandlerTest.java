package com.fpmislata.back.web.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fpmislata.back.domain.Exception.ResourceNotFoundException;

import jakarta.validation.ValidationException;

class ApiExceptionHandlerTest {

    private ApiExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ApiExceptionHandler();
    }

    @Test
    void handleResourceNotFoundException_returnsCorrectErrorMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Producto no encontrado");

        ErrorMessage errorMessage = handler.handleResourceNotFoundException(ex);

        assertEquals("ResourceNotFoundException", errorMessage.getError());
        assertEquals("Producto no encontrado", errorMessage.getMessage());
    }

    @Test
    void handleValidationException_returnsCorrectErrorMessage() {
        ValidationException ex = new ValidationException("Campo obligatorio");

        ErrorMessage errorMessage = handler.handleValidationException(ex);

        assertEquals("ValidationException", errorMessage.getError());
        assertEquals("Campo obligatorio", errorMessage.getMessage());
    }

    @Test
    void handleValidationException_withIllegalArgumentException_returnsCorrectErrorMessage() {
        IllegalArgumentException ex = new IllegalArgumentException("Id no válido");

        ErrorMessage errorMessage = handler.handleValidationException(ex);

        assertEquals("IllegalArgumentException", errorMessage.getError());
        assertEquals("Id no válido", errorMessage.getMessage());
    }

    @Test
    void handleGeneralException_returnsCorrectErrorMessage() {
        Exception ex = new RuntimeException("Error interno");

        ErrorMessage errorMessage = handler.handleGeneralException(ex);

        assertEquals("RuntimeException", errorMessage.getError());
        assertEquals("Error interno", errorMessage.getMessage());
    }
}
