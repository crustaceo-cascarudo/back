package com.fpmislata.back.domain.service.impl;

import com.fpmislata.back.domain.mapper.CategoryMapper;
import com.fpmislata.back.domain.model.Category;
import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.repository.CategoryRepository;
import com.fpmislata.back.domain.repository.entity.CategoryEntity;
import com.fpmislata.back.domain.service.dto.CategoryDto;
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
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapperMock;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private MockedStatic<CategoryMapper> mockedStaticCategoryMapper;

    @BeforeEach
    void setUp() {
        mockedStaticCategoryMapper = mockStatic(CategoryMapper.class);
        when(CategoryMapper.getInstance()).thenReturn(categoryMapperMock);
    }

    @AfterEach
    void tearDown() {
        mockedStaticCategoryMapper.close();
    }

    private CategoryDto createCategoryDto(Long id, String name) {
        return new CategoryDto(id, name, name != null ? name.toLowerCase() : null, "Desc", true);
    }

    private CategoryEntity createCategoryEntity(Long id, String name) {
        return new CategoryEntity(id, name, name != null ? name.toLowerCase() : null, "Desc", true);
    }

    private Category createCategory(Long id, String name) {
        return new Category(id, name, name != null ? name.toLowerCase() : null, "Desc", true);
    }

    // === create ===

    @Test
    void create_whenNameIsUnique_shouldCreateCategory() {
        CategoryDto inputDto = createCategoryDto(null, "Pizzas");
        Category category = createCategory(null, "Pizzas");
        CategoryEntity entityToSave = createCategoryEntity(null, "Pizzas");
        Category savedCategory = createCategory(1L, "Pizzas");
        CategoryDto expectedDto = createCategoryDto(1L, "Pizzas");

        when(categoryRepository.findByName("Pizzas")).thenReturn(Collections.emptyList());
        when(categoryMapperMock.fromCategoryDtoToCategory(inputDto)).thenReturn(category);
        when(categoryMapperMock.fromCategoryToCategoryEntity(category)).thenReturn(entityToSave);
        when(categoryMapperMock.fromCategoryEntityToCategory(entityToSave)).thenReturn(savedCategory);
        when(categoryMapperMock.fromCategoryToCategoryDto(savedCategory)).thenReturn(expectedDto);

        CategoryDto result = categoryService.create(inputDto);

        assertEquals("Pizzas", result.name());
        verify(categoryRepository).save(entityToSave);
    }

    @Test
    void create_whenNameExists_shouldThrowIllegalArgumentException() {
        CategoryDto inputDto = createCategoryDto(null, "Pizzas");
        CategoryEntity existingEntity = createCategoryEntity(1L, "Pizzas");
        Category existingCategory = createCategory(1L, "Pizzas");
        CategoryDto existingDto = createCategoryDto(1L, "Pizzas");

        when(categoryRepository.findByName("Pizzas")).thenReturn(List.of(existingEntity));
        when(categoryMapperMock.fromCategoryEntityToCategory(existingEntity)).thenReturn(existingCategory);
        when(categoryMapperMock.fromCategoryToCategoryDto(existingCategory)).thenReturn(existingDto);

        assertThrows(IllegalArgumentException.class, () -> categoryService.create(inputDto));
        verify(categoryRepository, never()).save(any());
    }

    // === findById ===

    @Test
    void findById_whenExists_shouldReturnCategory() {
        CategoryEntity entity = createCategoryEntity(1L, "Pizzas");
        Category category = createCategory(1L, "Pizzas");
        CategoryDto dto = createCategoryDto(1L, "Pizzas");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(categoryMapperMock.fromCategoryEntityToCategory(entity)).thenReturn(category);
        when(categoryMapperMock.fromCategoryToCategoryDto(category)).thenReturn(dto);

        CategoryDto result = categoryService.findById(1L);
        assertEquals("Pizzas", result.name());
    }

    @Test
    void findById_whenNotExists_shouldThrowIllegalArgumentException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> categoryService.findById(99L));
    }

    // === update ===

    @Test
    void update_whenExists_shouldUpdateCategory() {
        CategoryDto inputDto = createCategoryDto(1L, "PizzasUpdated");
        CategoryEntity existingEntity = createCategoryEntity(1L, "Pizzas");
        Category existingCategory = createCategory(1L, "Pizzas");
        CategoryDto existingDto = createCategoryDto(1L, "Pizzas");
        Category updatedCategory = createCategory(1L, "PizzasUpdated");
        CategoryEntity updatedEntity = createCategoryEntity(1L, "PizzasUpdated");
        CategoryDto expectedDto = createCategoryDto(1L, "PizzasUpdated");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(categoryMapperMock.fromCategoryEntityToCategory(existingEntity)).thenReturn(existingCategory);
        when(categoryMapperMock.fromCategoryToCategoryDto(existingCategory)).thenReturn(existingDto);
        when(categoryMapperMock.fromCategoryDtoToCategory(inputDto)).thenReturn(updatedCategory);
        when(categoryMapperMock.fromCategoryToCategoryEntity(updatedCategory)).thenReturn(updatedEntity);
        when(categoryMapperMock.fromCategoryEntityToCategory(updatedEntity)).thenReturn(updatedCategory);
        when(categoryMapperMock.fromCategoryToCategoryDto(updatedCategory)).thenReturn(expectedDto);

        CategoryDto result = categoryService.update(inputDto);

        assertEquals("PizzasUpdated", result.name());
        verify(categoryRepository).save(updatedEntity);
    }

    @Test
    void update_whenNotExists_shouldThrowIllegalArgumentException() {
        CategoryDto inputDto = createCategoryDto(99L, "NoExiste");
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryService.update(inputDto));
    }

    // === delete ===

    @Test
    void delete_whenExists_shouldDeleteCategory() {
        CategoryEntity entity = createCategoryEntity(1L, "Pizzas");
        Category category = createCategory(1L, "Pizzas");
        CategoryDto dto = createCategoryDto(1L, "Pizzas");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(categoryMapperMock.fromCategoryEntityToCategory(entity)).thenReturn(category);
        when(categoryMapperMock.fromCategoryToCategoryDto(category)).thenReturn(dto);

        categoryService.delete(1L);

        verify(categoryRepository).delete(1L);
    }

    @Test
    void delete_whenNotExists_shouldThrowIllegalArgumentException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> categoryService.delete(99L));
    }

    // === findAll ===

    @Test
    void findAll_shouldReturnPagedCategories() {
        CategoryEntity entity = createCategoryEntity(1L, "Pizzas");
        Page<CategoryEntity> entityPage = new Page<>(List.of(entity), 1, 10, 1);
        Category category = createCategory(1L, "Pizzas");
        CategoryDto dto = createCategoryDto(1L, "Pizzas");

        when(categoryRepository.findAllPaged(1, 10)).thenReturn(entityPage);
        when(categoryMapperMock.fromCategoryEntityToCategory(entity)).thenReturn(category);
        when(categoryMapperMock.fromCategoryToCategoryDto(category)).thenReturn(dto);

        Page<CategoryDto> result = categoryService.findAll(1, 10);

        assertEquals(1, result.data().size());
        assertEquals("Pizzas", result.data().get(0).name());
    }

    @Test
    void findAll_withInvalidPage_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.findAll(0, 10));
    }

    @Test
    void findAll_withInvalidSize_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.findAll(1, 0));
    }
}
