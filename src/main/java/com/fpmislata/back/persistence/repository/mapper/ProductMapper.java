package com.fpmislata.back.persistence.repository.mapper;

import com.fpmislata.back.domain.repository.entity.ProductEntity;
import com.fpmislata.back.persistence.dao.impl.entity.ProductJpaEntity;

public class ProductMapper {
    private static ProductMapper instance;

    private ProductMapper() {
    }

    public static ProductMapper getInstance() {
        if (instance == null) {
            instance = new ProductMapper();
        }
        return instance;
    }

    public ProductJpaEntity fromProductEntityToProductJpaEntity(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }
        ProductJpaEntity productJpaEntity = new ProductJpaEntity(
                productEntity.id(),
                productEntity.name(),
                null,
                productEntity.basePrice(),
                productEntity.discountPercentage(),
                productEntity.image(),
                null
        );

        productJpaEntity.setProductIngredients(productEntity.ingredients().stream().map(IngredientMapper.getInstance()::fromIngredientEntityToIngredientJpaEntity).toList());
        productJpaEntity.setProductCategories(productEntity.categories().stream().map(CategoryMapper.getInstance()::fromCategoryEntityToJpaEntity).toList());

        return productJpaEntity;
    }

    public ProductEntity fromProductJpaEntityToProductEntity(ProductJpaEntity productJpaEntity) {
        if (productJpaEntity == null) {
            return null;
        }
        return new ProductEntity(
                productJpaEntity.getId(),
                productJpaEntity.getName(),
                productJpaEntity.getProductIngredients().stream().map(IngredientMapper.getInstance()::fromIngredientJpaEntityToIngredientEntity).toList(),
                productJpaEntity.getBasePrice(),
                productJpaEntity.getDiscountPercentage(),
                productJpaEntity.getImage(),
                productJpaEntity.getProductCategories().stream().map(CategoryMapper.getInstance()::fromCategoryJpaEntityToEntity).toList()
        );
    }
}
