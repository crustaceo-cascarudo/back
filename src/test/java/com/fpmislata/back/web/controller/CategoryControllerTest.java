package com.fpmislata.back.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.service.CategoryService;
import com.fpmislata.back.domain.service.dto.CategoryDto;
import com.fpmislata.back.web.mapper.CategoryMapper;
import com.fpmislata.back.web.webModel.request.InsertCategoryRequest;
import com.fpmislata.back.web.webModel.request.UpdateCategoryRequest;
import com.fpmislata.back.web.webModel.response.DetailCategoryResponse;
import com.fpmislata.back.web.webModel.response.SummaryCategoryResponse;

class CategoryControllerTest {

    private CategoryService categoryService;
    private CategoryController categoryController;
    private MockedStatic<CategoryMapper> categoryMapperStatic;
    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        categoryService = mock(CategoryService.class);
        categoryController = new CategoryController(categoryService);

        categoryMapper = mock(CategoryMapper.class);
        categoryMapperStatic = mockStatic(CategoryMapper.class);
        categoryMapperStatic.when(CategoryMapper::getInstance).thenReturn(categoryMapper);
    }

    @AfterEach
    void tearDown() {
        categoryMapperStatic.close();
    }

    @Test
    void getAll_returnsOkWithPage() {
        CategoryDto dto = new CategoryDto(1L, "Burgers", "burgers", "Desc", true);
        Page<CategoryDto> page = new Page<>(List.of(dto), 1, 10, 1);

        when(categoryService.findAll(1, 10)).thenReturn(page);
        categoryMapperStatic.when(() -> CategoryMapper.fromCategoryDtoToSummaryCategoryResponse(dto))
                .thenReturn(new SummaryCategoryResponse(1L, "Burgers", "burgers", true));

        ResponseEntity<Page<SummaryCategoryResponse>> response = categoryController.getAll(1, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().data().size());
    }

    @Test
    void getById_returnsOkWithDetail() {
        CategoryDto dto = new CategoryDto(1L, "Burgers", "burgers", "Desc", true);
        DetailCategoryResponse detail = new DetailCategoryResponse(1L, "Burgers", "burgers", "Desc", true);

        when(categoryService.findById(1L)).thenReturn(dto);
        categoryMapperStatic.when(() -> CategoryMapper.fromCategoryDtoToDetailCategoryResponse(dto))
                .thenReturn(detail);

        ResponseEntity<DetailCategoryResponse> response = categoryController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Burgers", response.getBody().name());
    }

    @Test
    void create_returnsCreated() {
        InsertCategoryRequest request = new InsertCategoryRequest("New", "new", "Desc", true);
        CategoryDto created = new CategoryDto(1L, "New", "new", "Desc", true);
        DetailCategoryResponse detail = new DetailCategoryResponse(1L, "New", "new", "Desc", true);

        when(categoryMapper.fromInsertRequestToCategoryDto(request)).thenReturn(
                new CategoryDto(null, "New", "new", "Desc", true));
        when(categoryService.create(any())).thenReturn(created);
        categoryMapperStatic.when(() -> CategoryMapper.fromCategoryDtoToDetailCategoryResponse(created))
                .thenReturn(detail);

        ResponseEntity<DetailCategoryResponse> response = categoryController.create(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void delete_returnsNoContent() {
        doNothing().when(categoryService).delete(1L);

        ResponseEntity<Void> response = categoryController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(categoryService).delete(1L);
    }
}
