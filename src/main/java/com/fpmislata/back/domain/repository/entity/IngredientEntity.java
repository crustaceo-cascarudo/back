package com.fpmislata.back.domain.repository.entity;

public record IngredientEntity(
                Long id,
                String name,
                double price,
                String image) {
}
