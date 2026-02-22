package com.fpmislata.back.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.service.ProductService;
import com.fpmislata.back.domain.service.dto.ProductDto;

class ProductControllerTest {

    private ProductService productService;
    private ProductController productController;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        productController = new ProductController(productService);
    }

    @Test
    void findAll_returnsOkWithPage() {
        ProductDto dto = new ProductDto(1L, "Burger", List.of(), 10.0, null, 10.0, "img.png", List.of());
        Page<ProductDto> page = new Page<>(List.of(dto), 1, 10, 1);

        when(productService.findAll(1, 10)).thenReturn(page);

        ResponseEntity<Page<ProductDto>> response = productController.findAll(1, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().data().size());
    }

    @Test
    void findById_returnsOkWithProduct() {
        ProductDto dto = new ProductDto(1L, "Burger", List.of(), 10.0, null, 10.0, "img.png", List.of());
        when(productService.getById(1L)).thenReturn(dto);

        ResponseEntity<ProductDto> response = productController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Burger", response.getBody().name());
    }

    @Test
    void create_returnsCreatedWithProduct() {
        ProductDto dto = new ProductDto(null, "New Burger", List.of(), 11.0, null, 11.0, "new.png", List.of());
        ProductDto created = new ProductDto(1L, "New Burger", List.of(), 11.0, null, 11.0, "new.png", List.of());
        when(productService.create(dto)).thenReturn(created);

        ResponseEntity<ProductDto> response = productController.create(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().id());
    }

    @Test
    void delete_returnsNoContent() {
        doNothing().when(productService).deleteById(1L);

        ResponseEntity<Void> response = productController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService).deleteById(1L);
    }
}
