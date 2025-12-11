package com.fpmislata.back.domain.mapper;

import com.fpmislata.back.domain.model.Ingredient;
import com.fpmislata.back.domain.repository.entity.IngredientEntity;
import com.fpmislata.back.domain.service.dto.IngredientDto;

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

    public IngredientDto fromIngredientToIngredientDto(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }
        return new IngredientDto(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getPrice(),
                ingredient.getImage()
        );
    }

    public Ingredient fromIngredientDtoToIngredient(IngredientDto ingredientDto) {
        if (ingredientDto == null) {
            return null;
        }
        return new Ingredient(
                ingredientDto.id(),
                ingredientDto.name(),
                ingredientDto.price(),
                ingredientDto.image()
        );
    }

    public IngredientEntity fromIngredientToIngredientEntity(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }
        return new IngredientEntity(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getPrice(),
                ingredient.getImage()
        );
    }

    public Ingredient fromIngredientEntityToIngredient(IngredientEntity ingredientEntity) {
        if (ingredientEntity == null) {
            return null;
        }
        return new Ingredient(
                ingredientEntity.id(),
                ingredientEntity.name(),
                ingredientEntity.price(),
                ingredientEntity.image()
        );
    }
}
