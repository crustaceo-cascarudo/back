package com.fpmislata.back.domain.service;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.service.dto.CategoryDto;

public interface CategoryService {
    Page<CategoryDto> findAll(int pageNumber, int pageSize);
    CategoryDto findById(Long id);
    CategoryDto findByName(String name);
    CategoryDto create(CategoryDto categoryDto);
    CategoryDto update(CategoryDto categoryDto);
    void delete(Long id);

}
