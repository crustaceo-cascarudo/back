package com.fpmislata.back.domain.repository.entity;

public record CategoryEntity(
        Long id,
        String name,
        String slug,
        String description,
        Boolean estado
) {

}
