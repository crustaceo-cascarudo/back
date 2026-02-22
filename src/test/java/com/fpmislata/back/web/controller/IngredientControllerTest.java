package com.fpmislata.back.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.service.IngredientService;
import com.fpmislata.back.domain.service.dto.IngredientDto;

class IngredientControllerTest {

    private IngredientService ingredientService;
    private IngredientController ingredientController;

    @BeforeEach
    void setUp() {
        ingredientService = mock(IngredientService.class);
        ingredientController = new IngredientController(ingredientService);
    }

    @Test
    void findAll_returnsOkWithPage() {
        IngredientDto dto = new IngredientDto(1L, "Lettuce", 0.5, "lettuce.png");
        Page<IngredientDto> page = new Page<>(List.of(dto), 1, 10, 1);

        when(ingredientService.findAll(1, 10)).thenReturn(page);

        ResponseEntity<Page<IngredientDto>> response = ingredientController.findAll(1, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().data().size());
    }

    @Test
    void findById_returnsOkWithIngredient() {
        IngredientDto dto = new IngredientDto(1L, "Lettuce", 0.5, "lettuce.png");
        when(ingredientService.getById(1L)).thenReturn(dto);

        ResponseEntity<IngredientDto> response = ingredientController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Lettuce", response.getBody().name());
    }

    @Test
    void create_returnsCreated() {
        IngredientDto dto = new IngredientDto(null, "Cheese", 1.2, "cheese.png");
        IngredientDto created = new IngredientDto(1L, "Cheese", 1.2, "cheese.png");
        when(ingredientService.create(dto)).thenReturn(created);

        ResponseEntity<IngredientDto> response = ingredientController.create(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().id());
    }

    @Test
    void delete_returnsNoContent() {
        doNothing().when(ingredientService).deleteById(1L);

        ResponseEntity<Void> response = ingredientController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ingredientService).deleteById(1L);
    }
}
