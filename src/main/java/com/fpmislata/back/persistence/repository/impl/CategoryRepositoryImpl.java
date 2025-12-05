package com.fpmislata.back.persistence.repository.impl;

import java.util.List;
import java.util.Optional;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.repository.CategoryRepository;
import com.fpmislata.back.domain.repository.entity.CategoryEntity;
import com.fpmislata.back.persistence.dao.CategoryDao;
import com.fpmislata.back.persistence.dao.impl.entity.CategoryJpaEntity;
import com.fpmislata.back.persistence.repository.mapper.CategoryMapper;

public class CategoryRepositoryImpl implements CategoryRepository{

    private final CategoryDao categoryDao;

    public CategoryRepositoryImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public void delete(Long id) {
        categoryDao.delete(id);
    }

    @Override
    public Page<CategoryEntity> findAllPaged(int pageNumber, int pageSize) {
        List<CategoryEntity> content = categoryDao.findAll(pageNumber, pageSize).stream()
                .map(CategoryMapper.getInstance()::fromCategoryJpaEntityToEntity)
                .toList();
        long totalElements = categoryDao.count();
        return new Page<>(content, pageNumber, pageSize, totalElements);
    }

    @Override
    public Optional<CategoryEntity> findById(Long id) {
        return categoryDao.findById(id)
            .map(CategoryMapper.getInstance()::fromCategoryJpaEntityToEntity);
    }

    

    @Override
    public Optional<CategoryEntity> findByName(String name) {
        CategoryJpaEntity jpaEntity = categoryDao.findByName(name).orElse(null);
        CategoryEntity entity = CategoryMapper.getInstance().fromCategoryJpaEntityToEntity(jpaEntity);
        return Optional.ofNullable(entity);
    }

    @Override
    public CategoryEntity save(CategoryEntity categoryEntity) {
        CategoryJpaEntity categoryJpaEntity = CategoryMapper.getInstance().fromCategoryEntityToJpaEntity(categoryEntity);
        if (categoryEntity.id() != null) {
            CategoryJpaEntity existingEntity = categoryDao.findById(categoryEntity.id()).orElse(null);
            if (existingEntity != null) {
                categoryDao.update(categoryJpaEntity);
                return categoryEntity;
            }
        }
        return CategoryMapper.getInstance().fromCategoryJpaEntityToEntity(
            categoryDao.insert(categoryJpaEntity)
        );
    }
    

}
