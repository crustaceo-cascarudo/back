package com.fpmislata.back.domain.service.impl;

import com.fpmislata.back.domain.Exception.BusinessException;
import com.fpmislata.back.domain.Exception.ResourceNotFoundException;
import com.fpmislata.back.domain.mapper.ProductMapper;
import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.model.Product;
import com.fpmislata.back.domain.repository.ProductRepository;
import com.fpmislata.back.domain.repository.entity.CategoryEntity;
import com.fpmislata.back.domain.repository.entity.IngredientEntity;
import com.fpmislata.back.domain.repository.entity.ProductEntity;
import com.fpmislata.back.domain.service.dto.CategoryDto;
import com.fpmislata.back.domain.service.dto.IngredientDto;
import com.fpmislata.back.domain.service.dto.ProductDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapperMock;

    @InjectMocks
    private ProductServiceImpl productService;

    private MockedStatic<ProductMapper> mockedStaticProductMapper;

    @BeforeEach
    void setUp() {
        mockedStaticProductMapper = mockStatic(ProductMapper.class);
        when(ProductMapper.getInstance()).thenReturn(productMapperMock);
    }

    @AfterEach
    void tearDown() {
        mockedStaticProductMapper.close();
    }

    private ProductDto createProductDto(Long id, String name) {
        return new ProductDto(id, name, List.of(new IngredientDto(1L, "Queso", 1.5, "q.jpg")),
                10.0, 0, 10.0, "img.jpg", List.of(new CategoryDto(1L, "Cat", "cat", "d", true)));
    }

    private ProductEntity createProductEntity(Long id, String name) {
        return new ProductEntity(id, name, List.of(new IngredientEntity(1L, "Queso", 1.5, "q.jpg")),
                10.0, 0, "img.jpg", List.of(new CategoryEntity(1L, "Cat", "cat", "d", true)));
    }

    private Product createProduct(Long id, String name) {
        return new Product(id, name, List.of(), 10.0, 0, "img.jpg", List.of());
    }

    // === findAll ===

    @Test
    void findAll_shouldReturnPagedProducts() {
        ProductEntity entity = createProductEntity(1L, "Pizza");
        Page<ProductEntity> entityPage = new Page<>(List.of(entity), 1, 10, 1);
        Product product = createProduct(1L, "Pizza");
        ProductDto dto = createProductDto(1L, "Pizza");

        when(productRepository.findAll(1, 10)).thenReturn(entityPage);
        when(productMapperMock.fromProductEntityToProduct(entity)).thenReturn(product);
        when(productMapperMock.fromProductToProductDto(product)).thenReturn(dto);

        Page<ProductDto> result = productService.findAll(1, 10);

        assertEquals(1, result.data().size());
        assertEquals("Pizza", result.data().get(0).name());
        verify(productRepository).findAll(1, 10);
    }

    // === findByCategory ===

    @Test
    void findByCategory_shouldReturnFilteredProducts() {
        ProductEntity entity = createProductEntity(1L, "Pizza");
        Page<ProductEntity> entityPage = new Page<>(List.of(entity), 1, 10, 1);
        Product product = createProduct(1L, "Pizza");
        ProductDto dto = createProductDto(1L, "Pizza");

        when(productRepository.findByCategory("pizzas", 1, 10)).thenReturn(entityPage);
        when(productMapperMock.fromProductEntityToProduct(entity)).thenReturn(product);
        when(productMapperMock.fromProductToProductDto(product)).thenReturn(dto);

        Page<ProductDto> result = productService.findByCategory("pizzas", 1, 10);

        assertEquals(1, result.data().size());
        verify(productRepository).findByCategory("pizzas", 1, 10);
    }

    // === getById ===

    @Test
    void getById_whenExists_shouldReturnProduct() {
        ProductEntity entity = createProductEntity(1L, "Pizza");
        Product product = createProduct(1L, "Pizza");
        ProductDto dto = createProductDto(1L, "Pizza");

        when(productRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(productMapperMock.fromProductEntityToProduct(entity)).thenReturn(product);
        when(productMapperMock.fromProductToProductDto(product)).thenReturn(dto);

        ProductDto result = productService.getById(1L);

        assertEquals("Pizza", result.name());
        verify(productRepository).findById(1L);
    }

    @Test
    void getById_whenNotExists_shouldThrowResourceNotFoundException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getById(99L));
    }

    // === create ===

    @Test
    void create_whenNameIsUnique_shouldCreateProduct() {
        ProductDto inputDto = createProductDto(null, "NuevaPizza");
        Product product = createProduct(null, "NuevaPizza");
        ProductEntity entityToSave = createProductEntity(null, "NuevaPizza");
        ProductEntity savedEntity = createProductEntity(1L, "NuevaPizza");
        Product savedProduct = createProduct(1L, "NuevaPizza");
        ProductDto expectedDto = createProductDto(1L, "NuevaPizza");

        when(productRepository.findByName("NuevaPizza")).thenReturn(Collections.emptyList());
        when(productMapperMock.fromProductDtoToProduct(inputDto)).thenReturn(product);
        when(productMapperMock.fromProductToProductEntity(product)).thenReturn(entityToSave);
        when(productRepository.save(entityToSave)).thenReturn(savedEntity);
        when(productMapperMock.fromProductEntityToProduct(savedEntity)).thenReturn(savedProduct);
        when(productMapperMock.fromProductToProductDto(savedProduct)).thenReturn(expectedDto);

        ProductDto result = productService.create(inputDto);

        assertEquals(1L, result.id());
        assertEquals("NuevaPizza", result.name());
        verify(productRepository).save(entityToSave);
    }

    @Test
    void create_whenNameExists_shouldThrowBusinessException() {
        ProductDto inputDto = createProductDto(null, "Pizza");
        ProductEntity existingEntity = createProductEntity(1L, "Pizza");
        Product existingProduct = createProduct(1L, "Pizza");
        ProductDto existingDto = createProductDto(1L, "Pizza");

        when(productRepository.findByName("Pizza")).thenReturn(List.of(existingEntity));
        when(productMapperMock.fromProductEntityToProduct(existingEntity)).thenReturn(existingProduct);
        when(productMapperMock.fromProductToProductDto(existingProduct)).thenReturn(existingDto);

        assertThrows(BusinessException.class, () -> productService.create(inputDto));
        verify(productRepository, never()).save(any());
    }

    // === update ===

    @Test
    void update_whenExistsAndNameIsUnique_shouldUpdateProduct() {
        ProductDto inputDto = createProductDto(1L, "PizzaUpdated");
        ProductEntity existingEntity = createProductEntity(1L, "Pizza");
        Product product = createProduct(1L, "PizzaUpdated");
        ProductEntity entityToSave = createProductEntity(1L, "PizzaUpdated");
        ProductEntity savedEntity = createProductEntity(1L, "PizzaUpdated");
        Product savedProduct = createProduct(1L, "PizzaUpdated");
        ProductDto expectedDto = createProductDto(1L, "PizzaUpdated");

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(productRepository.findByName("PizzaUpdated")).thenReturn(Collections.emptyList());
        when(productMapperMock.fromProductDtoToProduct(inputDto)).thenReturn(product);
        when(productMapperMock.fromProductToProductEntity(product)).thenReturn(entityToSave);
        when(productRepository.save(entityToSave)).thenReturn(savedEntity);
        when(productMapperMock.fromProductEntityToProduct(savedEntity)).thenReturn(savedProduct);
        when(productMapperMock.fromProductToProductDto(savedProduct)).thenReturn(expectedDto);

        ProductDto result = productService.update(inputDto);

        assertEquals("PizzaUpdated", result.name());
    }

    @Test
    void update_whenNotExists_shouldThrowResourceNotFoundException() {
        ProductDto inputDto = createProductDto(99L, "Pizza");
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.update(inputDto));
    }

    @Test
    void update_whenNameExistsOnDifferentProduct_shouldThrowBusinessException() {
        ProductDto inputDto = createProductDto(1L, "PizzaDuplicada");
        ProductEntity existingEntity = createProductEntity(1L, "PizzaOriginal");
        ProductEntity conflictEntity = createProductEntity(2L, "PizzaDuplicada");

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(productRepository.findByName("PizzaDuplicada")).thenReturn(List.of(conflictEntity));

        assertThrows(BusinessException.class, () -> productService.update(inputDto));
    }

    // === deleteById ===

    @Test
    void deleteById_whenExists_shouldDelete() {
        ProductEntity entity = createProductEntity(1L, "Pizza");
        when(productRepository.findById(1L)).thenReturn(Optional.of(entity));

        productService.deleteById(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteById_whenNotExists_shouldThrowResourceNotFoundException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteById(99L));
        verify(productRepository, never()).deleteById(any());
    }
}
