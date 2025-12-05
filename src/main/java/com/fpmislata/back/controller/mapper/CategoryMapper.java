package com.fpmislata.back.controller.mapper;

import com.fpmislata.back.controller.webModel.request.InsertCategoryRequest;
import com.fpmislata.back.controller.webModel.request.UpdateCategoryRequest;
import com.fpmislata.back.controller.webModel.response.DetailCategoryResponse;
import com.fpmislata.back.controller.webModel.response.SummaryCategoryResponse;
import com.fpmislata.back.domain.service.dto.CategoryDto;

public class CategoryMapper {

    private static CategoryMapper instance;

    private CategoryMapper() {
    }

    public static CategoryMapper getInstance() {
        if (instance == null) {
            instance = new CategoryMapper();
        }
        return instance;
    }

    public CategoryDto fromInsertRequestToCategoryDto(InsertCategoryRequest insertCategoryRequest) {
        if (insertCategoryRequest == null) {
            return null;
        }
        return new CategoryDto(
                null,
                insertCategoryRequest.name(),
                insertCategoryRequest.slug(),
                insertCategoryRequest.description(),
                insertCategoryRequest.estado()
        );
    }

    public CategoryDto fromUpdateRequestToCategoryDto(UpdateCategoryRequest updateCategoryRequest) {
        if (updateCategoryRequest == null) {
            return null;
        }
        return new CategoryDto(
                updateCategoryRequest.id(),
                updateCategoryRequest.name(),
                updateCategoryRequest.slug(),
                updateCategoryRequest.description(),
                updateCategoryRequest.estado()
        );
    }

    public static SummaryCategoryResponse fromCategoryDtoToSummaryCategoryResponse(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }
        return new SummaryCategoryResponse(
                categoryDto.id(),
                categoryDto.name(),
                categoryDto.slug(),
                categoryDto.estado()
        );
    }

    public static DetailCategoryResponse fromCategoryDtoToDetailCategoryResponse(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }
        return new DetailCategoryResponse(
                categoryDto.id(),
                categoryDto.name(),
                categoryDto.slug(),
                categoryDto.description(),
                categoryDto.estado()
        );
    }



    

}
