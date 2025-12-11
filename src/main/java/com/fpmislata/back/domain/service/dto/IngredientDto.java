package com.fpmislata.back.domain.service.dto;

import jakarta.validation.constraints.NotNull;

public record IngredientDto(
                Long id,
                @NotNull String name,
                @NotNull Double price,
                String image) {
}
