package com.fpmislata.back.domain.service;

import java.util.List;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.service.dto.CategoryDto;

public interface CategoryService {
    Page<CategoryDto> findAll(int pageNumber, int pageSize);

    CategoryDto findById(Long id);

    List<CategoryDto> findByName(String name);

    CategoryDto create(CategoryDto categoryDto);

    CategoryDto update(CategoryDto categoryDto);

    void delete(Long id);

}
