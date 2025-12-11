package com.fpmislata.back.domain.mapper;

import com.fpmislata.back.domain.model.Product;
import com.fpmislata.back.domain.repository.entity.ProductEntity;
import com.fpmislata.back.domain.service.dto.ProductDto;

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

    public ProductDto fromProductToProductDto(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getIngredients().stream().map(IngredientMapper.getInstance()::fromIngredientToIngredientDto).toList(),
                product.getBasePrice(),
                product.getDiscountPercentage(),
                product.getFinalPrice(),
                product.getImage(),
                product.getCategories().stream().map(CategoryMapper.getInstance()::fromCategoryToCategoryDto).toList()
        );
    }

    public Product fromProductDtoToProduct(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }
        return new Product(
                productDto.id(),
                productDto.name(),
                productDto.ingredients().stream().map(IngredientMapper.getInstance()::fromIngredientDtoToIngredient).toList(),
                productDto.basePrice(),
                productDto.discountPercentage(),
                productDto.image(),
                productDto.categories().stream().map(CategoryMapper.getInstance()::fromCategoryDtoToCategory).toList()
        );
    }

    public ProductEntity fromProductToProductEntity(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getIngredients().stream().map(IngredientMapper.getInstance()::fromIngredientToIngredientEntity).toList(),
                product.getBasePrice(),
                product.getDiscountPercentage(),
                product.getImage(),
                product.getCategories().stream().map(CategoryMapper.getInstance()::fromCategoryToCategoryEntity).toList()
        );
    }

    public Product fromProductEntityToProduct(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }
        return new Product(
                productEntity.id(),
                productEntity.name(),
                productEntity.ingredients().stream().map(IngredientMapper.getInstance()::fromIngredientEntityToIngredient).toList(),
                productEntity.basePrice(),
                productEntity.discountPercentage(),
                productEntity.image(),
                productEntity.categories().stream().map(CategoryMapper.getInstance()::fromCategoryEntityToCategory).toList()
        );
    }
}
