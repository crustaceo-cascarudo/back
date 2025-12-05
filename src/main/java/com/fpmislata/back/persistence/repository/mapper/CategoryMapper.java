package com.fpmislata.back.persistence.repository.mapper;

import com.fpmislata.back.domain.repository.entity.CategoryEntity;
import com.fpmislata.back.persistence.dao.impl.entity.CategoryJpaEntity;

public class CategoryMapper {

    public static CategoryMapper instance;

    private CategoryMapper() {
    }

    public static CategoryMapper getInstance() {
        if (instance == null) {
            instance = new CategoryMapper();
        }
        return instance;
    }

    public CategoryJpaEntity fromCategoryEntityToJpaEntity(CategoryEntity categoryEntity) {
        if (categoryEntity == null) {
            return null;
        }
        return new CategoryJpaEntity(
                categoryEntity.id(),
                categoryEntity.name(),
                categoryEntity.slug(),
                categoryEntity.description(),
                categoryEntity.estado()
        );
    }

    public CategoryEntity fromCategoryJpaEntityToEntity(CategoryJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        return new CategoryEntity(
                jpaEntity.getId(),
                jpaEntity.getName(),
                jpaEntity.getSlug(),
                jpaEntity.getDescription(),
                jpaEntity.getEstado()
        );
    }
}
