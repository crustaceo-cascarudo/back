package com.fpmislata.back.domain.mapper;

import com.fpmislata.back.domain.model.Ingredient;
import com.fpmislata.back.domain.repository.entity.IngredientEntity;
import com.fpmislata.back.domain.service.dto.IngredientDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IngredientMapperTest {

    private final IngredientMapper mapper = IngredientMapper.getInstance();

    @Test
    void fromIngredientToIngredientDto_shouldMapAllFields() {
        Ingredient ingredient = new Ingredient(1L, "Queso", 1.5, "queso.jpg");
        IngredientDto dto = mapper.fromIngredientToIngredientDto(ingredient);

        assertEquals(1L, dto.id());
        assertEquals("Queso", dto.name());
        assertEquals(1.5, dto.basePrice());
        assertEquals("queso.jpg", dto.image());
    }

    @Test
    void fromIngredientToIngredientDto_null_shouldReturnNull() {
        assertNull(mapper.fromIngredientToIngredientDto(null));
    }

    @Test
    void fromIngredientDtoToIngredient_shouldMapAllFields() {
        IngredientDto dto = new IngredientDto(1L, "Queso", 1.5, "queso.jpg");
        Ingredient ingredient = mapper.fromIngredientDtoToIngredient(dto);

        assertEquals(1L, ingredient.getId());
        assertEquals("Queso", ingredient.getName());
        assertEquals(1.5, ingredient.getPrice());
        assertEquals("queso.jpg", ingredient.getImage());
    }

    @Test
    void fromIngredientDtoToIngredient_null_shouldReturnNull() {
        assertNull(mapper.fromIngredientDtoToIngredient(null));
    }

    @Test
    void fromIngredientToIngredientEntity_shouldMapAllFields() {
        Ingredient ingredient = new Ingredient(1L, "Queso", 1.5, "queso.jpg");
        IngredientEntity entity = mapper.fromIngredientToIngredientEntity(ingredient);

        assertEquals(1L, entity.id());
        assertEquals("Queso", entity.name());
        assertEquals(1.5, entity.price());
        assertEquals("queso.jpg", entity.image());
    }

    @Test
    void fromIngredientToIngredientEntity_null_shouldReturnNull() {
        assertNull(mapper.fromIngredientToIngredientEntity(null));
    }

    @Test
    void fromIngredientEntityToIngredient_shouldMapAllFields() {
        IngredientEntity entity = new IngredientEntity(1L, "Queso", 1.5, "queso.jpg");
        Ingredient ingredient = mapper.fromIngredientEntityToIngredient(entity);

        assertEquals(1L, ingredient.getId());
        assertEquals("Queso", ingredient.getName());
        assertEquals(1.5, ingredient.getPrice());
        assertEquals("queso.jpg", ingredient.getImage());
    }

    @Test
    void fromIngredientEntityToIngredient_null_shouldReturnNull() {
        assertNull(mapper.fromIngredientEntityToIngredient(null));
    }
}
