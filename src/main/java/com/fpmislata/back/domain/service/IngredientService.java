package com.fpmislata.back.domain.service;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.service.dto.IngredientDto;

import java.util.List;
import java.util.Optional;

public interface IngredientService {
    Page<IngredientDto> findAll(int page, int size);
    Optional<IngredientDto> findById(Long id);
    List<IngredientDto> findByName(String name);
    IngredientDto getById(Long id);
    IngredientDto create(IngredientDto ingredientDto);
    IngredientDto update(IngredientDto ingredientDto);
    void deleteById(Long id);
}
