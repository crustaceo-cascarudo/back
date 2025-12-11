package com.fpmislata.back.domain.repository.entity;
import java.util.List;

public record ProductEntity(
        Long id,
        String name,
        List<IngredientEntity> ingredients,
        double basePrice,
        Integer discountPercentage,
        String image,
        List<CategoryEntity> categories
) {
}
