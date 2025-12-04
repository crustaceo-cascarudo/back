package com.fpmislata.back.persistence.dao.impl;

import java.util.UUID;

import com.fpmislata.back.persistence.dao.UserDao;
import com.fpmislata.back.persistence.dao.impl.entity.SessionTokenJpaEntity;
import com.fpmislata.back.persistence.dao.impl.entity.UserJpaEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

public class UserDaoJpa implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserJpaEntity getByName(String name) {
        try {
            String jpql = "SELECT u FROM UserJpaEntity u WHERE u.name = :name";
            return entityManager.createQuery(jpql, UserJpaEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void delete(Long id) {
        UserJpaEntity entity = entityManager.find(UserJpaEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public UserJpaEntity getById(Long id) {
        return entityManager.find(UserJpaEntity.class, id);
    }

    @Override
    public Long insert(UserJpaEntity userJpaEntity) {
        entityManager.persist(userJpaEntity);
        entityManager.flush();
        return userJpaEntity.getId();
    }

    @Override
    public void update(UserJpaEntity userJpaEntity) {
        entityManager.merge(userJpaEntity);
    }

    @Override
    public String createSessionToken(Long userId) {
        String token = UUID.randomUUID().toString();

        SessionTokenJpaEntity sessionToken = new SessionTokenJpaEntity(null, token, userId);

        entityManager.persist(sessionToken);
        entityManager.flush();

        return token;
    }

}
