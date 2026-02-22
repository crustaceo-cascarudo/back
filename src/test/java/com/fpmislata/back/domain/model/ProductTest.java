package com.fpmislata.back.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void calcFinalPrice_withZeroDiscount_shouldReturnBasePrice() {
        Product product = new Product(1L, "Pizza", List.of(), 10.0, 0, "img.jpg", List.of());
        assertEquals(10.0, product.calcFinalPrice(), 0.001);
        assertEquals(10.0, product.getFinalPrice(), 0.001);
    }

    @Test
    void calcFinalPrice_with50PercentDiscount_shouldReturnHalfPrice() {
        Product product = new Product(1L, "Pizza", List.of(), 20.0, 50, "img.jpg", List.of());
        assertEquals(10.0, product.calcFinalPrice(), 0.001);
    }

    @Test
    void calcFinalPrice_with100PercentDiscount_shouldReturnZero() {
        Product product = new Product(1L, "Pizza", List.of(), 15.0, 100, "img.jpg", List.of());
        assertEquals(0.0, product.calcFinalPrice(), 0.001);
    }

    @Test
    void calcFinalPrice_withNullDiscount_shouldReturnBasePrice() {
        Product product = new Product(1L, "Pizza", List.of(), 12.0, null, "img.jpg", List.of());
        assertEquals(12.0, product.calcFinalPrice(), 0.001);
    }

    @Test
    void constructor_shouldSetAllFields() {
        Ingredient ingredient = new Ingredient(1L, "Queso", 1.5, "queso.jpg");
        Category category = new Category(1L, "Pizzas", "pizzas", "Desc", true);
        Product product = new Product(1L, "Pizza", List.of(ingredient), 10.0, 5, "pizza.jpg", List.of(category));

        assertEquals(1L, product.getId());
        assertEquals("Pizza", product.getName());
        assertEquals(1, product.getIngredients().size());
        assertEquals(10.0, product.getBasePrice());
        assertEquals(5, product.getDiscountPercentage());
        assertEquals("pizza.jpg", product.getImage());
        assertEquals(1, product.getCategories().size());
    }
}
