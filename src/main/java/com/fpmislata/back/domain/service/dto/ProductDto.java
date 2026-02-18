package com.fpmislata.back.domain.service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProductDto(
        Long id,
        @NotNull(message = "Name cant be null")
        String name,
        @NotNull
        List<IngredientDto> ingredients,
        @NotNull(message = "Base price cant be null")
        double basePrice,
        Integer discountPercentage,
        Double finalPrice,
        String image,
        @NotNull
        List<CategoryDto> categories
) { }
