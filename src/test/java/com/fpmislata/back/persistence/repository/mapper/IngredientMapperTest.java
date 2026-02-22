package com.fpmislata.back.persistence.repository.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fpmislata.back.domain.repository.entity.IngredientEntity;
import com.fpmislata.back.persistence.dao.impl.entity.IngredientJpaEntity;

class IngredientMapperTest {

    private final IngredientMapper mapper = IngredientMapper.getInstance();

    @Test
    void fromIngredientEntityToJpaEntity_validInput_mapsAllFields() {
        IngredientEntity entity = new IngredientEntity(1L, "Lettuce", 0.5, "lettuce.png");

        IngredientJpaEntity jpa = mapper.fromIngredientEntityToIngredientJpaEntity(entity);

        assertEquals(1L, jpa.getId());
        assertEquals("Lettuce", jpa.getName());
        assertEquals(0.5, jpa.getPrice());
        assertEquals("lettuce.png", jpa.getImage());
    }

    @Test
    void fromIngredientEntityToJpaEntity_null_returnsNull() {
        assertNull(mapper.fromIngredientEntityToIngredientJpaEntity(null));
    }

    @Test
    void fromIngredientJpaEntityToEntity_validInput_mapsAllFields() {
        IngredientJpaEntity jpa = new IngredientJpaEntity(2L, "Tomato", 0.3, "tomato.png");

        IngredientEntity entity = mapper.fromIngredientJpaEntityToIngredientEntity(jpa);

        assertEquals(2L, entity.id());
        assertEquals("Tomato", entity.name());
        assertEquals(0.3, entity.price());
        assertEquals("tomato.png", entity.image());
    }

    @Test
    void fromIngredientJpaEntityToEntity_null_returnsNull() {
        assertNull(mapper.fromIngredientJpaEntityToIngredientEntity(null));
    }

    @Test
    void roundTrip_preservesData() {
        IngredientEntity original = new IngredientEntity(10L, "Cheese", 1.2, "cheese.png");

        IngredientJpaEntity jpa = mapper.fromIngredientEntityToIngredientJpaEntity(original);
        IngredientEntity roundTripped = mapper.fromIngredientJpaEntityToIngredientEntity(jpa);

        assertEquals(original.id(), roundTripped.id());
        assertEquals(original.name(), roundTripped.name());
        assertEquals(original.price(), roundTripped.price());
        assertEquals(original.image(), roundTripped.image());
    }
}
