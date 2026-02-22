package com.fpmislata.back.persistence.repository.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.fpmislata.back.domain.repository.entity.CategoryEntity;
import com.fpmislata.back.domain.repository.entity.IngredientEntity;
import com.fpmislata.back.domain.repository.entity.ProductEntity;
import com.fpmislata.back.persistence.dao.impl.entity.CategoryJpaEntity;
import com.fpmislata.back.persistence.dao.impl.entity.IngredientJpaEntity;
import com.fpmislata.back.persistence.dao.impl.entity.ProductJpaEntity;

class ProductMapperTest {

    private MockedStatic<IngredientMapper> ingredientMapperStatic;
    private MockedStatic<CategoryMapper> categoryMapperStatic;
    private IngredientMapper ingredientMapper;
    private CategoryMapper categoryMapper;
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        ingredientMapper = mock(IngredientMapper.class);
        categoryMapper = mock(CategoryMapper.class);

        ingredientMapperStatic = mockStatic(IngredientMapper.class);
        categoryMapperStatic = mockStatic(CategoryMapper.class);

        ingredientMapperStatic.when(IngredientMapper::getInstance).thenReturn(ingredientMapper);
        categoryMapperStatic.when(CategoryMapper::getInstance).thenReturn(categoryMapper);

        productMapper = ProductMapper.getInstance();
    }

    @AfterEach
    void tearDown() {
        ingredientMapperStatic.close();
        categoryMapperStatic.close();
    }

    @Test
    void fromProductEntityToProductJpaEntity_validInput_mapsAllFields() {
        IngredientEntity ie = new IngredientEntity(1L, "Lettuce", 0.5, "lettuce.png");
        CategoryEntity ce = new CategoryEntity(1L, "Burgers", "burgers", "Desc", true);
        ProductEntity pe = new ProductEntity(1L, "Classic Burger",
                List.of(ie), 10.0, 10, "burger.png", List.of(ce));

        IngredientJpaEntity ije = new IngredientJpaEntity(1L, "Lettuce", 0.5, "lettuce.png");
        CategoryJpaEntity cje = new CategoryJpaEntity(1L, "Burgers", "burgers", "Desc", true);

        when(ingredientMapper.fromIngredientEntityToIngredientJpaEntity(ie)).thenReturn(ije);
        when(categoryMapper.fromCategoryEntityToJpaEntity(ce)).thenReturn(cje);

        ProductJpaEntity result = productMapper.fromProductEntityToProductJpaEntity(pe);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Classic Burger", result.getName());
        assertEquals(10.0, result.getBasePrice());
        assertEquals(10, result.getDiscountPercentage());
        assertEquals("burger.png", result.getImage());
    }

    @Test
    void fromProductEntityToProductJpaEntity_null_returnsNull() {
        assertNull(productMapper.fromProductEntityToProductJpaEntity(null));
    }

    @Test
    void fromProductJpaEntityToProductEntity_validInput_mapsAllFields() {
        IngredientJpaEntity ije = new IngredientJpaEntity(2L, "Tomato", 0.3, "tomato.png");
        CategoryJpaEntity cje = new CategoryJpaEntity(2L, "Pizzas", "pizzas", "Desc2", false);

        ProductJpaEntity jpa = new ProductJpaEntity(2L, "Margherita",
                new java.util.ArrayList<>(), 12.0, 5, "pizza.png", new java.util.ArrayList<>());
        jpa.setProductIngredients(List.of(ije));
        jpa.setProductCategories(List.of(cje));

        IngredientEntity ie = new IngredientEntity(2L, "Tomato", 0.3, "tomato.png");
        CategoryEntity ce = new CategoryEntity(2L, "Pizzas", "pizzas", "Desc2", false);

        when(ingredientMapper.fromIngredientJpaEntityToIngredientEntity(ije)).thenReturn(ie);
        when(categoryMapper.fromCategoryJpaEntityToEntity(cje)).thenReturn(ce);

        ProductEntity result = productMapper.fromProductJpaEntityToProductEntity(jpa);

        assertNotNull(result);
        assertEquals(2L, result.id());
        assertEquals("Margherita", result.name());
        assertEquals(12.0, result.basePrice());
        assertEquals(5, result.discountPercentage());
        assertEquals("pizza.png", result.image());
        assertEquals(1, result.ingredients().size());
        assertEquals(1, result.categories().size());
    }

    @Test
    void fromProductJpaEntityToProductEntity_null_returnsNull() {
        assertNull(productMapper.fromProductJpaEntityToProductEntity(null));
    }
}
