package com.fpmislata.back.persistence.repository.impl;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.repository.IngredientRepository;
import com.fpmislata.back.domain.repository.entity.IngredientEntity;
import com.fpmislata.back.persistence.dao.IngredientDao;
import com.fpmislata.back.persistence.dao.impl.entity.IngredientJpaEntity;
import com.fpmislata.back.persistence.repository.mapper.IngredientMapper;


import java.util.List;
import java.util.Optional;

public class IngredientRepositoryImpl implements IngredientRepository {

    IngredientDao ingredientDao;

    public IngredientRepositoryImpl(IngredientDao ingredientDao) {
        this.ingredientDao = ingredientDao;
    }

    @Override
    public Page<IngredientEntity> findAll(int page, int size) {
        List<IngredientEntity> content = ingredientDao.findAll(page, size).stream()
                .map(IngredientMapper.getInstance()::fromIngredientJpaEntityToIngredientEntity)
                .toList();
        long totalElements = ingredientDao.count();
        return new Page<>(content, page, size, totalElements);
    }

    @Override
    public Optional<IngredientEntity> findById(Long id) {
        return ingredientDao.findById(id).map(IngredientMapper.getInstance()::fromIngredientJpaEntityToIngredientEntity);
    }

    @Override
    public List<IngredientEntity> findByName(String name) {
        return ingredientDao.findByName(name).stream().map(IngredientMapper.getInstance()::fromIngredientJpaEntityToIngredientEntity).toList();
    }

    @Override
    public IngredientEntity save(IngredientEntity ingredientEntity) {
        IngredientJpaEntity entity = IngredientMapper.getInstance().fromIngredientEntityToIngredientJpaEntity(ingredientEntity);
        if(entity.getId() == null){
            return IngredientMapper.getInstance().fromIngredientJpaEntityToIngredientEntity(ingredientDao.insert(entity));
        }
        return IngredientMapper.getInstance().fromIngredientJpaEntityToIngredientEntity(ingredientDao.update(entity));
    }

    @Override
    public void deleteById(Long id) {
        ingredientDao.delete(id);
    }
}
