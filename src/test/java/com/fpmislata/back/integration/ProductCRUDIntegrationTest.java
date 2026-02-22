package com.fpmislata.back.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.fpmislata.back.domain.Exception.BusinessException;
import com.fpmislata.back.domain.Exception.ResourceNotFoundException;
import com.fpmislata.back.domain.mapper.ProductMapper;
import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.model.Product;
import com.fpmislata.back.domain.repository.ProductRepository;
import com.fpmislata.back.domain.repository.entity.CategoryEntity;
import com.fpmislata.back.domain.repository.entity.IngredientEntity;
import com.fpmislata.back.domain.repository.entity.ProductEntity;
import com.fpmislata.back.domain.service.dto.ProductDto;
import com.fpmislata.back.domain.service.impl.ProductServiceImpl;

/**
 * Integration-style tests for Product CRUD flows across
 * service ↔ repository layers with real mapper cooperation.
 */
class ProductCRUDIntegrationTest {

    private ProductRepository productRepository;
    private ProductServiceImpl productService;

    private MockedStatic<ProductMapper> productMapperStatic;
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);

        productMapper = mock(ProductMapper.class);
        productMapperStatic = mockStatic(ProductMapper.class);
        productMapperStatic.when(ProductMapper::getInstance).thenReturn(productMapper);
    }

    @AfterEach
    void tearDown() {
        productMapperStatic.close();
    }

    @Test
    void createProduct_thenFindById_returnsCreatedProduct() {
        CategoryEntity category = new CategoryEntity(1L, "Food", "food", "Food category", true);
        List<IngredientEntity> ingredients = List.of(
                new IngredientEntity(1L, "Lettuce", 0.5, "lettuce.png")
        );
        ProductEntity toSave = new ProductEntity(null, "Burger", ingredients, 10.0, null, "burger.png", List.of(category));
        Product toSaveProduct = new Product(null, "Burger", List.of(), 10.0, null, "burger.png", List.of());
        ProductDto toSaveDto = new ProductDto(null, "Burger", List.of(), 10.0, null, 10.0, "burger.png", List.of());

        when(productRepository.findByName("Burger")).thenReturn(List.of());
        when(productMapper.fromProductDtoToProduct(toSaveDto)).thenReturn(toSaveProduct);
        when(productMapper.fromProductToProductEntity(toSaveProduct)).thenReturn(toSave);
        when(productRepository.save(toSave)).thenReturn(
                new ProductEntity(1L, "Burger", ingredients, 10.0, null, "burger.png", List.of(category))
        );
        ProductEntity savedEntity = new ProductEntity(1L, "Burger", ingredients, 10.0, null, "burger.png", List.of(category));
        Product savedProduct = new Product(1L, "Burger", List.of(), 10.0, null, "burger.png", List.of());
        ProductDto savedDto = new ProductDto(1L, "Burger", List.of(), 10.0, null, 10.0, "burger.png", List.of());
        when(productMapper.fromProductEntityToProduct(savedEntity)).thenReturn(savedProduct);
        when(productMapper.fromProductToProductDto(savedProduct)).thenReturn(savedDto);

        ProductDto createResult = productService.create(toSaveDto);
        verify(productRepository).save(toSave);
        assertEquals(1L, createResult.id());

        // Now find it
        when(productRepository.findById(1L)).thenReturn(Optional.of(savedEntity));

        ProductDto found = productService.getById(1L);
        assertEquals(1L, found.id());
        assertEquals("Burger", found.name());
    }

    @Test
    void createProduct_duplicateName_throwsBusinessException() {
        ProductEntity existing = new ProductEntity(1L, "Burger", List.of(), 10.0, null, "burger.png", List.of());
        when(productRepository.findByName("Burger")).thenReturn(List.of(existing));

        ProductDto productDto = new ProductDto(null, "Burger", List.of(), 10.0, null, 10.0, "burger.png", List.of());
        assertThrows(BusinessException.class, () -> productService.create(productDto));
        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_nameConflict_throwsBusinessException() {
        ProductEntity otherProduct = new ProductEntity(2L, "Pizza", List.of(), 12.0, null, "pizza.png", List.of());
        when(productRepository.findByName("Pizza")).thenReturn(List.of(otherProduct));
        when(productRepository.findById(1L)).thenReturn(Optional.of(
                new ProductEntity(1L, "Burger", List.of(), 10.0, null, "burger.png", List.of())
        ));

        ProductDto productDto = new ProductDto(1L, "Pizza", List.of(), 10.0, null, 10.0, "burger.png", List.of());
        assertThrows(BusinessException.class, () -> productService.update(productDto));
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_notFound_throwsResourceNotFoundException() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteById(999L));
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    void findAll_returnsConvertedProducts() {
        ProductEntity pe = new ProductEntity(1L, "Burger", List.of(), 10.0, null, "burger.png", List.of());
        Product p = new Product(1L, "Burger", List.of(), 10.0, null, "burger.png", List.of());
        ProductDto dto = new ProductDto(1L, "Burger", List.of(), 10.0, null, 10.0, "burger.png", List.of());

        Page<ProductEntity> entityPage = new Page<>(List.of(pe), 1, 10, 1);
        when(productRepository.findAll(1, 10)).thenReturn(entityPage);
        when(productMapper.fromProductEntityToProduct(pe)).thenReturn(p);
        when(productMapper.fromProductToProductDto(p)).thenReturn(dto);

        Page<ProductDto> result = productService.findAll(1, 10);
        assertEquals(1, result.data().size());
        assertEquals("Burger", result.data().get(0).name());
    }
}
