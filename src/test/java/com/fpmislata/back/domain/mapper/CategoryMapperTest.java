package com.fpmislata.back.domain.mapper;

import com.fpmislata.back.domain.model.Category;
import com.fpmislata.back.domain.repository.entity.CategoryEntity;
import com.fpmislata.back.domain.service.dto.CategoryDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    private final CategoryMapper mapper = CategoryMapper.getInstance();

    @Test
    void fromCategoryToCategoryDto_shouldMapAllFields() {
        Category category = new Category(1L, "Pizzas", "pizzas", "Desc", true);
        CategoryDto dto = mapper.fromCategoryToCategoryDto(category);

        assertEquals(1L, dto.id());
        assertEquals("Pizzas", dto.name());
        assertEquals("pizzas", dto.slug());
        assertEquals("Desc", dto.description());
        assertTrue(dto.estado());
    }

    @Test
    void fromCategoryToCategoryDto_null_shouldReturnNull() {
        assertNull(mapper.fromCategoryToCategoryDto(null));
    }

    @Test
    void fromCategoryDtoToCategory_shouldMapAllFields() {
        CategoryDto dto = new CategoryDto(1L, "Pizzas", "pizzas", "Desc", true);
        Category category = mapper.fromCategoryDtoToCategory(dto);

        assertEquals(1L, category.getId());
        assertEquals("Pizzas", category.getName());
        assertEquals("pizzas", category.getSlug());
        assertEquals("Desc", category.getDescription());
        assertTrue(category.getEstado());
    }

    @Test
    void fromCategoryDtoToCategory_null_shouldReturnNull() {
        assertNull(mapper.fromCategoryDtoToCategory(null));
    }

    @Test
    void fromCategoryToCategoryEntity_shouldMapAllFields() {
        Category category = new Category(1L, "Pizzas", "pizzas", "Desc", true);
        CategoryEntity entity = mapper.fromCategoryToCategoryEntity(category);

        assertEquals(1L, entity.id());
        assertEquals("Pizzas", entity.name());
        assertEquals("pizzas", entity.slug());
        assertEquals("Desc", entity.description());
        assertTrue(entity.estado());
    }

    @Test
    void fromCategoryToCategoryEntity_null_shouldReturnNull() {
        assertNull(mapper.fromCategoryToCategoryEntity(null));
    }

    @Test
    void fromCategoryEntityToCategory_shouldMapAllFields() {
        CategoryEntity entity = new CategoryEntity(1L, "Pizzas", "pizzas", "Desc", true);
        Category category = mapper.fromCategoryEntityToCategory(entity);

        assertEquals(1L, category.getId());
        assertEquals("Pizzas", category.getName());
        assertEquals("pizzas", category.getSlug());
        assertEquals("Desc", category.getDescription());
        assertTrue(category.getEstado());
    }

    @Test
    void fromCategoryEntityToCategory_null_shouldReturnNull() {
        assertNull(mapper.fromCategoryEntityToCategory(null));
    }
}
