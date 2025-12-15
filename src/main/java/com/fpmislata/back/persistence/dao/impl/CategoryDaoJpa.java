package com.fpmislata.back.persistence.dao.impl;

import java.util.List;
import java.util.Optional;

import com.fpmislata.back.persistence.dao.CategoryDao;
import com.fpmislata.back.persistence.dao.impl.entity.CategoryJpaEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

public class CategoryDaoJpa implements CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(c) FROM CategoryJpaEntity c", Long.class)
                .getSingleResult();
    }

    @Override
    public List<CategoryJpaEntity> findAll(int pageNumber, int pageSize) {
        int pageIndex = Math.max(pageNumber - 1, 0);
        return entityManager.createQuery("SELECT c FROM CategoryJpaEntity c", CategoryJpaEntity.class)
                .setFirstResult(pageIndex * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        CategoryJpaEntity entity = entityManager.find(CategoryJpaEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public Optional<CategoryJpaEntity> findById(Long id) {
        CategoryJpaEntity entity = entityManager.find(CategoryJpaEntity.class, id);
        if (entity != null) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }

    @Override
    public List<CategoryJpaEntity> findByName(String name) {
        try {
            String jpql = "SELECT c FROM CategoryJpaEntity c WHERE LOWER(c.name) LIKE LOWER(:name)";
            return entityManager.createQuery(jpql, CategoryJpaEntity.class)
                    .setParameter("name", name)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Transactional
    @Override
    public CategoryJpaEntity insert(CategoryJpaEntity jpaEntity) {
        entityManager.persist(jpaEntity);
        entityManager.flush();
        return jpaEntity;
    }

    @Transactional
    @Override
    public CategoryJpaEntity update(CategoryJpaEntity jpaEntity) {
        return entityManager.merge(jpaEntity);
    }

}
