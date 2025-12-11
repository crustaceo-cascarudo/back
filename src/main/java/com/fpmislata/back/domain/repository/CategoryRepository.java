package com.fpmislata.back.domain.repository;

import java.util.List;
import java.util.Optional;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.repository.entity.CategoryEntity;

public interface CategoryRepository {
    CategoryEntity save(CategoryEntity categoryEntity);
    Optional<CategoryEntity> findById(Long id);
    List<CategoryEntity> findByName(String name);
    Page<CategoryEntity> findAllPaged(int pageNumber, int pageSize);
    void delete(Long id);

}
