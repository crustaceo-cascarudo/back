package com.fpmislata.back.domain.service.dto;

import jakarta.validation.constraints.NotNull;

public record CategoryDto(
        Long id,
        @NotNull(message = "Name cannot be null")
        String name,
        @NotNull(message = "Slug cannot be null")
        String slug,
        String description,
        @NotNull(message = "Estado cannot be null")
        Boolean estado
) {

}
