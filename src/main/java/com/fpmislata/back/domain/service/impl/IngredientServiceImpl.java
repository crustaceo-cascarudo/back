package com.fpmislata.back.domain.service.impl;

import com.fpmislata.back.domain.Exception.BusinessException;
import com.fpmislata.back.domain.Exception.ResourceNotFoundException;
import com.fpmislata.back.domain.mapper.IngredientMapper;
import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.repository.IngredientRepository;
import com.fpmislata.back.domain.repository.entity.IngredientEntity;
import com.fpmislata.back.domain.service.IngredientService;
import com.fpmislata.back.domain.service.dto.IngredientDto;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    
    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }
    
    @Override
    public Page<IngredientDto> findAll(int page, int size){
        Page<IngredientEntity> ingredientEntityPage = ingredientRepository.findAll(page, size);
        List<IngredientDto> ingredientDtos = ingredientEntityPage.data()
                .stream()
                .map(IngredientMapper.getInstance()::fromIngredientEntityToIngredient)
                .map(IngredientMapper.getInstance()::fromIngredientToIngredientDto)
                .toList();
        return new Page<>(
                ingredientDtos,
                ingredientEntityPage.pageNumber(),
                ingredientEntityPage.pageSize(),
                ingredientEntityPage.totalElements()
        );
    }

    @Override
    public Optional<IngredientDto> findById(Long id) {
        return ingredientRepository
                .findById(id)
                .map(IngredientMapper.getInstance()::fromIngredientEntityToIngredient)
                .map(IngredientMapper.getInstance()::fromIngredientToIngredientDto);
    }

    @Override
    public List<IngredientDto> findByName(String name) {
        return ingredientRepository
                .findByName(name)
                .stream()
                .map(IngredientMapper.getInstance()::fromIngredientEntityToIngredient)
                .map(IngredientMapper.getInstance()::fromIngredientToIngredientDto)
                .toList();
    }

    @Override
    public IngredientDto getById(Long id) {
        return ingredientRepository
                .findById(id)
                .map(IngredientMapper.getInstance()::fromIngredientEntityToIngredient)
                .map(IngredientMapper.getInstance()::fromIngredientToIngredientDto)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient with id "+id+" does not exist"));
    }

    @Override
    @Transactional
    public IngredientDto create(IngredientDto ingredientDto) {
        List<IngredientDto> existingIngredients = findByName(ingredientDto.name());
        if(!existingIngredients.isEmpty() && existingIngredients.getFirst().name().equalsIgnoreCase(ingredientDto.name())){
            throw new BusinessException("Ingredient with name '"+ingredientDto.name()+"' already exists");
        }

        IngredientEntity ingredientEntity = IngredientMapper.getInstance().fromIngredientToIngredientEntity(
                IngredientMapper.getInstance().fromIngredientDtoToIngredient(ingredientDto)
        );

        return IngredientMapper.getInstance().fromIngredientToIngredientDto(
                IngredientMapper.getInstance().fromIngredientEntityToIngredient(
                        ingredientRepository.save(ingredientEntity)
                )
        );

    }

    @Override
    @Transactional
    public IngredientDto update(IngredientDto ingredientDto) {
         ingredientRepository.findById(ingredientDto.id())
                .orElseThrow(() -> new ResourceNotFoundException("No ingredient found with id "+ingredientDto.id()));

        List<IngredientDto> existingIngredients = findByName(ingredientDto.name());
        if(!existingIngredients.isEmpty() && existingIngredients.getFirst().name().equalsIgnoreCase(ingredientDto.name())){
                 throw new BusinessException("Ingredient with name '" + ingredientDto.name() + "' already exists");
             }

         IngredientEntity ingredientEntity = IngredientMapper.getInstance().fromIngredientToIngredientEntity(
                 IngredientMapper.getInstance().fromIngredientDtoToIngredient(ingredientDto)
         );

         return IngredientMapper.getInstance().fromIngredientToIngredientDto(
                 IngredientMapper.getInstance().fromIngredientEntityToIngredient(
                         ingredientRepository.save(ingredientEntity)
                 )
         );
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if(findById(id).isEmpty()){
            throw new ResourceNotFoundException("No ingredient found with id "+id);
        }
        ingredientRepository.deleteById(id);

    }

}
