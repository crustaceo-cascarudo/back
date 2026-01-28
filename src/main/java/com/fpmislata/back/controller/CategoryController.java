package com.fpmislata.back.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpmislata.back.controller.mapper.CategoryMapper;
import com.fpmislata.back.controller.webModel.request.InsertCategoryRequest;
import com.fpmislata.back.controller.webModel.request.UpdateCategoryRequest;
import com.fpmislata.back.controller.webModel.response.DetailCategoryResponse;
import com.fpmislata.back.controller.webModel.response.SummaryCategoryResponse;
import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.service.CategoryService;
import com.fpmislata.back.domain.service.dto.CategoryDto;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<Page<SummaryCategoryResponse>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CategoryDto> categoryPage = categoryService.findAll(page, size);

        List<SummaryCategoryResponse> categoryResponses = categoryPage.data().stream()
                .map(CategoryMapper::fromCategoryDtoToSummaryCategoryResponse).toList();

        Page<SummaryCategoryResponse> response = new Page<>(
                categoryResponses,
                categoryPage.pageNumber(),
                categoryPage.pageSize(),
                categoryPage.totalElements(),
                categoryPage.totalPages());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailCategoryResponse> getById(@PathVariable Long id) {
        CategoryDto categoryDto = categoryService.findById(id);
        DetailCategoryResponse response = CategoryMapper.fromCategoryDtoToDetailCategoryResponse(categoryDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DetailCategoryResponse>> findByName(@RequestParam String name) {
        List<CategoryDto> category = categoryService.findByName(name);
        List<DetailCategoryResponse> response = category.stream()
                .map(CategoryMapper::fromCategoryDtoToDetailCategoryResponse)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("")
    public ResponseEntity<DetailCategoryResponse> create(
            @RequestBody @Validated InsertCategoryRequest request) {

        CategoryDto categoryDto = CategoryMapper.getInstance()
                .fromInsertRequestToCategoryDto(request);
        CategoryDto createdCategory = categoryService.create(categoryDto);
        DetailCategoryResponse response = CategoryMapper.fromCategoryDtoToDetailCategoryResponse(createdCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetailCategoryResponse> update(
            @PathVariable Long id,
            @RequestBody @Validated UpdateCategoryRequest request) {

        UpdateCategoryRequest updatedRequest = new UpdateCategoryRequest(
                id,
                request.name(),
                request.slug(),
                request.description(),
                request.estado());

        CategoryDto categoryDto = CategoryMapper.getInstance()
                .fromUpdateRequestToCategoryDto(updatedRequest);
        CategoryDto updatedCategory = categoryService.update(categoryDto);
        DetailCategoryResponse response = CategoryMapper.fromCategoryDtoToDetailCategoryResponse(updatedCategory);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
