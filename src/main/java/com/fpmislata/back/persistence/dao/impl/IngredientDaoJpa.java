package com.fpmislata.back.persistence.dao.impl;

import com.fpmislata.back.domain.Exception.ResourceNotFoundException;
import com.fpmislata.back.persistence.dao.IngredientDao;
import com.fpmislata.back.persistence.dao.impl.entity.IngredientJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class IngredientDaoJpa implements IngredientDao {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<IngredientJpaEntity> findAll(int pageNumber, int pageSize) {
        int pageIndex = Math.max(pageNumber - 1, 0);

        String sql = "SELECT b FROM IngredientJpaEntity b ORDER BY b.id";
        TypedQuery<IngredientJpaEntity> ingredientJpaEntityPage = entityManager.createQuery(sql, IngredientJpaEntity.class)
                .setFirstResult(pageIndex * pageSize)
                .setMaxResults(pageSize);

        return ingredientJpaEntityPage.getResultList();
    }

    @Override
    public IngredientJpaEntity insert(IngredientJpaEntity jpaEntity) {
        entityManager.persist(jpaEntity);
        return jpaEntity;
    }

    @Override
    public Optional<IngredientJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(IngredientJpaEntity.class, id));
    }

    @Override
    public List<IngredientJpaEntity> findByName(String name) {
        String sql = "SELECT b FROM IngredientJpaEntity b WHERE b.name = :name";
        try{
            return entityManager.createQuery(sql, IngredientJpaEntity.class)
                    .setParameter("name", name)
                    .getResultList();
        } catch (Exception e){
            return List.of();
        }
    }

    @Override
    public IngredientJpaEntity update(IngredientJpaEntity jpaEntity) {
        IngredientJpaEntity managed = entityManager.find(IngredientJpaEntity.class, jpaEntity.getId());
        if (managed == null) {
            throw new ResourceNotFoundException("Ingredient not found with id " + jpaEntity.getId());
        }
        entityManager.flush();
        return entityManager.merge(jpaEntity);
    }

    @Override
    public void delete(Long id) {
        entityManager.remove(entityManager.find(IngredientJpaEntity.class, id));
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(b) FROM IngredientJpaEntity b", Long.class).getSingleResult();
    }
}
