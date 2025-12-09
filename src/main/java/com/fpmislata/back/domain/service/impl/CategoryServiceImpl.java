package com.fpmislata.back.domain.service.impl;

import java.util.List;
import java.util.Optional;

import com.fpmislata.back.domain.mapper.CategoryMapper;
import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.repository.CategoryRepository;
import com.fpmislata.back.domain.repository.entity.CategoryEntity;
import com.fpmislata.back.domain.service.CategoryService;
import com.fpmislata.back.domain.service.dto.CategoryDto;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        List<CategoryDto> existingCategoryByName = findByName(categoryDto.name());
        if (!existingCategoryByName.isEmpty()) {
            throw new IllegalArgumentException("Category with name " + categoryDto.name() + " already exists.");
        }
        CategoryEntity categoryEntity = CategoryMapper.getInstance()
                .fromCategoryToCategoryEntity(CategoryMapper.getInstance().fromCategoryDtoToCategory(categoryDto));
        categoryRepository.save(categoryEntity);
        return CategoryMapper.getInstance()
                .fromCategoryToCategoryDto(CategoryMapper.getInstance().fromCategoryEntityToCategory(categoryEntity));
    }

    @Override
    public List<CategoryDto> findByName(String name) {
        List<CategoryEntity> categoryEntities = categoryRepository.findByName(name);
        return categoryEntities.stream()
                .map(CategoryMapper.getInstance()::fromCategoryEntityToCategory)
                .map(CategoryMapper.getInstance()::fromCategoryToCategoryDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        Optional<CategoryDto> existingCategory = Optional.of(findById(id));
        if (existingCategory.isEmpty()) {
            throw new IllegalArgumentException("Category with id " + id + " does not exist.");
        }
        categoryRepository.delete(id);
    }

    @Override
    public Page<CategoryDto> findAll(int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Invalid page number or page size.");
        }
        Page<CategoryEntity> CategoryPageEntity = categoryRepository.findAllPaged(pageNumber, pageSize);

        List<CategoryDto> content = CategoryPageEntity.data()
                .stream()
                .map(CategoryMapper.getInstance()::fromCategoryEntityToCategory)
                .map(CategoryMapper.getInstance()::fromCategoryToCategoryDto)
                .toList();
        return new Page<>(content, CategoryPageEntity.pageNumber(), CategoryPageEntity.pageSize(),
                CategoryPageEntity.totalElements());
    }

    @Override
    public CategoryDto findById(Long id) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(id);
        if (categoryEntity.isEmpty()) {
            throw new IllegalArgumentException("Category with id " + id + " does not exist.");
        }
        return CategoryMapper.getInstance().fromCategoryToCategoryDto(
                CategoryMapper.getInstance().fromCategoryEntityToCategory(categoryEntity.get()));
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        Optional<CategoryDto> existingCategory = Optional.of(findById(categoryDto.id()));
        if (existingCategory.isEmpty()) {
            throw new IllegalArgumentException("Category with id " + categoryDto.id() + " does not exist.");
        }
        CategoryEntity categoryEntity = CategoryMapper.getInstance()
                .fromCategoryToCategoryEntity(CategoryMapper.getInstance().fromCategoryDtoToCategory(categoryDto));
        categoryRepository.save(categoryEntity);
        return CategoryMapper.getInstance()
                .fromCategoryToCategoryDto(CategoryMapper.getInstance().fromCategoryEntityToCategory(categoryEntity));
    }

}
