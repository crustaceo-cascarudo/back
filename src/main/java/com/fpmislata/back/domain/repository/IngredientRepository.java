package com.fpmislata.back.domain.repository;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.repository.entity.IngredientEntity;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository {
    Page<IngredientEntity> findAll(int page, int size);
    Optional<IngredientEntity> findById(Long id);
    List<IngredientEntity> findByName(String name);
    IngredientEntity save(IngredientEntity ingredientEntity);
    void deleteById(Long id);
}
