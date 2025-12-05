package com.fpmislata.back.domain.mapper;

import com.fpmislata.back.domain.model.Category;
import com.fpmislata.back.domain.repository.entity.CategoryEntity;
import com.fpmislata.back.domain.service.dto.CategoryDto;

public class CategoryMapper {

    private static CategoryMapper instance;

    private CategoryMapper() {
    }

    public static CategoryMapper getInstance() {
        if (instance == null) {
            instance = new CategoryMapper();
        }
        return instance;
    }

    public CategoryDto fromCategoryToCategoryDto(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getEstado()
        );
    }

    public Category fromCategoryDtoToCategory(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }
        return new Category(
                categoryDto.id(),
                categoryDto.name(),
                categoryDto.slug(),
                categoryDto.description(),
                categoryDto.estado()
        );
    }

    public CategoryEntity fromCategoryToCategoryEntity(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryEntity(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getEstado()
        );
    }

    public Category fromCategoryEntityToCategory(CategoryEntity categoryEntity) {
        if (categoryEntity == null) {
            return null;
        }
        return new Category(
                categoryEntity.id(),
                categoryEntity.name(),
                categoryEntity.slug(),
                categoryEntity.description(),
                categoryEntity.estado()
        );
    }

}
