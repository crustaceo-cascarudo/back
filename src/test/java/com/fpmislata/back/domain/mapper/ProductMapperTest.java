package com.fpmislata.back.domain.mapper;

import com.fpmislata.back.domain.model.Category;
import com.fpmislata.back.domain.model.Ingredient;
import com.fpmislata.back.domain.model.Product;
import com.fpmislata.back.domain.repository.entity.CategoryEntity;
import com.fpmislata.back.domain.repository.entity.IngredientEntity;
import com.fpmislata.back.domain.repository.entity.ProductEntity;
import com.fpmislata.back.domain.service.dto.CategoryDto;
import com.fpmislata.back.domain.service.dto.IngredientDto;
import com.fpmislata.back.domain.service.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductMapperTest {
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = ProductMapper.getInstance();
    }

    @Test
    void testFromProductToProductDto() {
        Ingredient ingredient = new Ingredient(1L, "Tomato", 0.5, "img.png");
        Category category = new Category(2L, "Veg", "veg", "Vegetarian", true);
        Product product = new Product(10L, "Salad", List.of(ingredient), 5.0, 10, "prod.png", List.of(category));

        ProductDto dto = productMapper.fromProductToProductDto(product);

        assertNotNull(dto);
        assertEquals(product.getId(), dto.id());
        assertEquals(product.getName(), dto.name());
        assertEquals(product.getBasePrice(), dto.basePrice());
        assertEquals(product.getDiscountPercentage(), dto.discountPercentage());
        assertEquals(product.getImage(), dto.image());
        assertEquals(1, dto.ingredients().size());
        assertEquals(1, dto.categories().size());
        assertEquals(ingredient.getId(), dto.ingredients().get(0).id());
        assertEquals(category.getId(), dto.categories().get(0).id());
    }

    @Test
    void testFromProductDtoToProduct() {
        IngredientDto ingredientDto = new IngredientDto(1L, "Tomato", 0.5, "img.png");
        CategoryDto categoryDto = new CategoryDto(2L, "Veg", "veg", "Vegetarian", true);
        ProductDto productDto = new ProductDto(11L, "Soup", List.of(ingredientDto), 3.0,  null, null, "soup.png", List.of(categoryDto));

        Product product = productMapper.fromProductDtoToProduct(productDto);

        assertNotNull(product);
        assertEquals(productDto.id(), product.getId());
        assertEquals(productDto.name(), product.getName());
        assertEquals(productDto.basePrice(), product.getBasePrice());
        assertEquals(productDto.image(), product.getImage());
        assertEquals(1, product.getIngredients().size());
        assertEquals(1, product.getCategories().size());
        assertEquals(ingredientDto.id(), product.getIngredients().get(0).getId());
        assertEquals(categoryDto.id(), product.getCategories().get(0).getId());
    }

    @Test
    void testFromProductToProductEntity() {
        Ingredient ingredient = new Ingredient(3L, "Cheese", 1.0, "cheese.png");
        Category category = new Category(4L, "Dairy", "dairy", "Dairy products", true);
        Product product = new Product(12L, "Cheesy", List.of(ingredient), 6.0, 20, "cheesy.png", List.of(category));

        ProductEntity entity = productMapper.fromProductToProductEntity(product);

        assertNotNull(entity);
        assertEquals(product.getId(), entity.id());
        assertEquals(product.getName(), entity.name());
        assertEquals(product.getBasePrice(), entity.basePrice());
        assertEquals(product.getDiscountPercentage(), entity.discountPercentage());
        assertEquals(product.getImage(), entity.image());
        assertEquals(1, entity.ingredients().size());
        assertEquals(1, entity.categories().size());
        assertEquals(ingredient.getId(), entity.ingredients().get(0).id());
        assertEquals(category.getId(), entity.categories().get(0).id());
    }

    @Test
    void testFromProductEntityToProduct() {
        IngredientEntity ingredientEntity = new IngredientEntity(5L, "Ham", 2.0, "ham.png");
        CategoryEntity categoryEntity = new CategoryEntity(6L, "Meat", "meat", "Meat products", true);
        ProductEntity productEntity = new ProductEntity(13L, "HamSandwich", List.of(ingredientEntity), 4.0, 5, "ham.png", List.of(categoryEntity));

        Product product = productMapper.fromProductEntityToProduct(productEntity);

        assertNotNull(product);
        assertEquals(productEntity.id(), product.getId());
        assertEquals(productEntity.name(), product.getName());
        assertEquals(productEntity.basePrice(), product.getBasePrice());
        assertEquals(productEntity.image(), product.getImage());
        assertEquals(1, product.getIngredients().size());
        assertEquals(1, product.getCategories().size());
        assertEquals(ingredientEntity.id(), product.getIngredients().get(0).getId());
        assertEquals(categoryEntity.id(), product.getCategories().get(0).getId());
    }

    @Test
    void testNullMappings() {
        assertNull(productMapper.fromProductToProductDto(null));
        assertNull(productMapper.fromProductDtoToProduct(null));
        assertNull(productMapper.fromProductToProductEntity(null));
        assertNull(productMapper.fromProductEntityToProduct(null));
    }

}
