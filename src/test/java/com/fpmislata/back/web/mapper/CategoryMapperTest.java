package com.fpmislata.back.web.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fpmislata.back.domain.service.dto.CategoryDto;
import com.fpmislata.back.web.webModel.request.InsertCategoryRequest;
import com.fpmislata.back.web.webModel.request.UpdateCategoryRequest;
import com.fpmislata.back.web.webModel.response.DetailCategoryResponse;
import com.fpmislata.back.web.webModel.response.SummaryCategoryResponse;

class CategoryMapperTest {

    private final CategoryMapper mapper = CategoryMapper.getInstance();

    @Test
    void fromInsertRequestToCategoryDto_validInput_mapsAllFields() {
        InsertCategoryRequest request = new InsertCategoryRequest("Burgers", "burgers", "Desc", true);

        CategoryDto dto = mapper.fromInsertRequestToCategoryDto(request);

        assertNull(dto.id());
        assertEquals("Burgers", dto.name());
        assertEquals("burgers", dto.slug());
        assertEquals("Desc", dto.description());
        assertTrue(dto.estado());
    }

    @Test
    void fromInsertRequestToCategoryDto_null_returnsNull() {
        assertNull(mapper.fromInsertRequestToCategoryDto(null));
    }

    @Test
    void fromUpdateRequestToCategoryDto_validInput_mapsAllFieldsWithId() {
        UpdateCategoryRequest request = new UpdateCategoryRequest(5L, "Pizzas", "pizzas", "Pizza desc", false);

        CategoryDto dto = mapper.fromUpdateRequestToCategoryDto(request);

        assertEquals(5L, dto.id());
        assertEquals("Pizzas", dto.name());
        assertEquals("pizzas", dto.slug());
        assertEquals("Pizza desc", dto.description());
        assertFalse(dto.estado());
    }

    @Test
    void fromUpdateRequestToCategoryDto_null_returnsNull() {
        assertNull(mapper.fromUpdateRequestToCategoryDto(null));
    }

    @Test
    void fromCategoryDtoToSummaryCategoryResponse_validInput_mapsFields() {
        CategoryDto dto = new CategoryDto(1L, "Burgers", "burgers", "Desc", true);

        SummaryCategoryResponse response = CategoryMapper.fromCategoryDtoToSummaryCategoryResponse(dto);

        assertEquals(1L, response.id());
        assertEquals("Burgers", response.name());
        assertEquals("burgers", response.slug());
        assertTrue(response.estado());
    }

    @Test
    void fromCategoryDtoToSummaryCategoryResponse_null_returnsNull() {
        assertNull(CategoryMapper.fromCategoryDtoToSummaryCategoryResponse(null));
    }

    @Test
    void fromCategoryDtoToDetailCategoryResponse_validInput_mapsAllFields() {
        CategoryDto dto = new CategoryDto(2L, "Drinks", "drinks", "Bebidas", true);

        DetailCategoryResponse response = CategoryMapper.fromCategoryDtoToDetailCategoryResponse(dto);

        assertEquals(2L, response.id());
        assertEquals("Drinks", response.name());
        assertEquals("drinks", response.slug());
        assertEquals("Bebidas", response.description());
        assertTrue(response.estado());
    }

    @Test
    void fromCategoryDtoToDetailCategoryResponse_null_returnsNull() {
        assertNull(CategoryMapper.fromCategoryDtoToDetailCategoryResponse(null));
    }
}
