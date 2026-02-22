package com.fpmislata.back.persistence.repository.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fpmislata.back.domain.repository.entity.CategoryEntity;
import com.fpmislata.back.persistence.dao.impl.entity.CategoryJpaEntity;

class CategoryMapperTest {

    private final CategoryMapper mapper = CategoryMapper.getInstance();

    @Test
    void fromCategoryEntityToJpaEntity_validInput_mapsAllFields() {
        CategoryEntity entity = new CategoryEntity(1L, "Burgers", "burgers", "Desc", true);

        CategoryJpaEntity jpa = mapper.fromCategoryEntityToJpaEntity(entity);

        assertEquals(1L, jpa.getId());
        assertEquals("Burgers", jpa.getName());
        assertEquals("burgers", jpa.getSlug());
        assertEquals("Desc", jpa.getDescription());
        assertTrue(jpa.getEstado());
    }

    @Test
    void fromCategoryEntityToJpaEntity_null_returnsNull() {
        assertNull(mapper.fromCategoryEntityToJpaEntity(null));
    }

    @Test
    void fromCategoryJpaEntityToEntity_validInput_mapsAllFields() {
        CategoryJpaEntity jpa = new CategoryJpaEntity(2L, "Pizzas", "pizzas", "Desc2", false);

        CategoryEntity entity = mapper.fromCategoryJpaEntityToEntity(jpa);

        assertEquals(2L, entity.id());
        assertEquals("Pizzas", entity.name());
        assertEquals("pizzas", entity.slug());
        assertEquals("Desc2", entity.description());
        assertFalse(entity.estado());
    }

    @Test
    void fromCategoryJpaEntityToEntity_null_returnsNull() {
        assertNull(mapper.fromCategoryJpaEntityToEntity(null));
    }

    @Test
    void roundTrip_preservesData() {
        CategoryEntity original = new CategoryEntity(5L, "Drinks", "drinks", "Bebidas", true);

        CategoryJpaEntity jpa = mapper.fromCategoryEntityToJpaEntity(original);
        CategoryEntity roundTripped = mapper.fromCategoryJpaEntityToEntity(jpa);

        assertEquals(original.id(), roundTripped.id());
        assertEquals(original.name(), roundTripped.name());
        assertEquals(original.slug(), roundTripped.slug());
        assertEquals(original.description(), roundTripped.description());
        assertEquals(original.estado(), roundTripped.estado());
    }
}
