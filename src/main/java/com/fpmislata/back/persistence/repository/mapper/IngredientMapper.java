package com.fpmislata.back.persistence.repository.mapper;

import com.fpmislata.back.domain.repository.entity.IngredientEntity;
import com.fpmislata.back.persistence.dao.impl.entity.IngredientJpaEntity;

public class IngredientMapper {
    private static IngredientMapper instance;

    private IngredientMapper() {
    }

    public static IngredientMapper getInstance() {
        if (instance == null) {
            instance = new IngredientMapper();
        }
        return instance;
    }

    public IngredientJpaEntity fromIngredientEntityToIngredientJpaEntity(IngredientEntity ingredientEntity) {
        if (ingredientEntity == null) {
            return null;
        }
        return new IngredientJpaEntity(
                ingredientEntity.id(),
                ingredientEntity.name(),
                ingredientEntity.price(),
                ingredientEntity.image()
        );
    }

    public IngredientEntity fromIngredientJpaEntityToIngredientEntity(IngredientJpaEntity ingredientJpaEntity) {
        if (ingredientJpaEntity == null) {
            return null;
        }
        return new IngredientEntity(
                ingredientJpaEntity.getId(),
                ingredientJpaEntity.getName(),
                ingredientJpaEntity.getPrice(),
                ingredientJpaEntity.getImage()
        );
    }

}
