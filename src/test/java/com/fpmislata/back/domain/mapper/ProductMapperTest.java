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
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private final ProductMapper mapper = ProductMapper.getInstance();

    @Test
    void fromProductToProductDto_shouldMapAllFields() {
        Ingredient ingredient = new Ingredient(1L, "Queso", 1.5, "queso.jpg");
        Category category = new Category(1L, "Pizzas", "pizzas", "Desc", true);
        Product product = new Product(1L, "Pizza", List.of(ingredient), 10.0, 5, "pizza.jpg", List.of(category));

        ProductDto dto = mapper.fromProductToProductDto(product);

        assertEquals(1L, dto.id());
        assertEquals("Pizza", dto.name());
        assertEquals(10.0, dto.basePrice());
        assertEquals(5, dto.discountPercentage());
        assertEquals("pizza.jpg", dto.image());
        assertEquals(1, dto.ingredients().size());
        assertEquals("Queso", dto.ingredients().get(0).name());
        assertEquals(1, dto.categories().size());
        assertEquals("Pizzas", dto.categories().get(0).name());
    }

    @Test
    void fromProductToProductDto_null_shouldReturnNull() {
        assertNull(mapper.fromProductToProductDto(null));
    }

    @Test
    void fromProductDtoToProduct_shouldMapAllFields() {
        IngredientDto ingredientDto = new IngredientDto(1L, "Queso", 1.5, "queso.jpg");
        CategoryDto categoryDto = new CategoryDto(1L, "Pizzas", "pizzas", "Desc", true);
        ProductDto dto = new ProductDto(1L, "Pizza", List.of(ingredientDto), 10.0, 5, 9.5, "pizza.jpg", List.of(categoryDto));

        Product product = mapper.fromProductDtoToProduct(dto);

        assertEquals(1L, product.getId());
        assertEquals("Pizza", product.getName());
        assertEquals(10.0, product.getBasePrice());
        assertEquals(5, product.getDiscountPercentage());
        assertEquals(1, product.getIngredients().size());
        assertEquals(1, product.getCategories().size());
    }

    @Test
    void fromProductDtoToProduct_null_shouldReturnNull() {
        assertNull(mapper.fromProductDtoToProduct(null));
    }

    @Test
    void fromProductToProductEntity_shouldMapAllFields() {
        Ingredient ingredient = new Ingredient(1L, "Queso", 1.5, "queso.jpg");
        Category category = new Category(1L, "Pizzas", "pizzas", "Desc", true);
        Product product = new Product(1L, "Pizza", List.of(ingredient), 10.0, 5, "pizza.jpg", List.of(category));

        ProductEntity entity = mapper.fromProductToProductEntity(product);

        assertEquals(1L, entity.id());
        assertEquals("Pizza", entity.name());
        assertEquals(10.0, entity.basePrice());
        assertEquals(5, entity.discountPercentage());
        assertEquals("pizza.jpg", entity.image());
        assertEquals(1, entity.ingredients().size());
        assertEquals(1, entity.categories().size());
    }

    @Test
    void fromProductToProductEntity_null_shouldReturnNull() {
        assertNull(mapper.fromProductToProductEntity(null));
    }

    @Test
    void fromProductEntityToProduct_shouldMapAllFields() {
        IngredientEntity ie = new IngredientEntity(1L, "Queso", 1.5, "queso.jpg");
        CategoryEntity ce = new CategoryEntity(1L, "Pizzas", "pizzas", "Desc", true);
        ProductEntity entity = new ProductEntity(1L, "Pizza", List.of(ie), 10.0, 5, "pizza.jpg", List.of(ce));

        Product product = mapper.fromProductEntityToProduct(entity);

        assertEquals(1L, product.getId());
        assertEquals("Pizza", product.getName());
        assertEquals(10.0, product.getBasePrice());
        assertEquals(5, product.getDiscountPercentage());
        assertEquals(1, product.getIngredients().size());
        assertEquals(1, product.getCategories().size());
    }

    @Test
    void fromProductEntityToProduct_null_shouldReturnNull() {
        assertNull(mapper.fromProductEntityToProduct(null));
    }
}
