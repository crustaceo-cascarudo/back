package com.fpmislata.back.persistence.dao.impl;

import com.fpmislata.back.domain.Exception.ResourceNotFoundException;
import com.fpmislata.back.persistence.dao.ProductDao;
import com.fpmislata.back.persistence.dao.impl.entity.ProductJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class ProductDaoJpa implements ProductDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ProductJpaEntity> findAll(int pageNumber, int pageSize) {
        int pageIndex = Math.max(pageNumber - 1, 0);

        String sql = "SELECT p FROM ProductJpaEntity p ORDER BY p.id";
        TypedQuery<ProductJpaEntity> productJpaEntityPage = entityManager
                .createQuery(sql, ProductJpaEntity.class)
                .setFirstResult(pageIndex * pageSize)
                .setMaxResults(pageSize);

        return productJpaEntityPage.getResultList();
    }

    @Override
    public ProductJpaEntity insert(ProductJpaEntity jpaEntity) {
        entityManager.persist(jpaEntity);
        return jpaEntity;
    }

    @Override
    public Optional<ProductJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(ProductJpaEntity.class, id));
    }

    @Override
    public List<ProductJpaEntity> findByName(String name) {
        String sql = "SELECT b FROM ProductJpaEntity b WHERE b.name = :name";
        try {
            return entityManager.createQuery(sql, ProductJpaEntity.class)
                    .setParameter("name", name)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public ProductJpaEntity update(ProductJpaEntity jpaEntity) {
        ProductJpaEntity managed = entityManager.find(ProductJpaEntity.class, jpaEntity.getId());
        if (managed == null) {
            throw new ResourceNotFoundException("Product not found with id " + jpaEntity.getId());
        }

        entityManager.createQuery("DELETE FROM ProductCategoryJpaEntity pc WHERE pc.product.id = :id")
                 .setParameter("id", jpaEntity.getId())
                 .executeUpdate();
                 
        entityManager.createQuery("DELETE FROM ProductIngredientJpaEntity pi WHERE pi.product.id = :id")
                 .setParameter("id", jpaEntity.getId())
                 .executeUpdate();
        managed.getProductCategories().clear();
        managed.getProductIngredients().clear();

        entityManager.flush();
        entityManager.clear();

        managed = entityManager.find(ProductJpaEntity.class, jpaEntity.getId());
        managed.setName(jpaEntity.getName());
        managed.setBasePrice(jpaEntity.getBasePrice());
        managed.setDiscountPercentage(jpaEntity.getDiscountPercentage());
        managed.setImage(jpaEntity.getImage());

        managed.setProductCategories(jpaEntity.getProductCategories());
        managed.setProductIngredients(jpaEntity.getProductIngredients());
        
        return entityManager.merge(managed);
    }

    public int deleteProductIngredientsByProductId(Long productId) {
        return entityManager.createQuery(
                        "DELETE FROM ProductIngredientJpaEntity pi WHERE pi.product.id = :productId")
                .setParameter("productId", productId)
                .executeUpdate();
    }


    public int deleteProductCategorysByProductId(Long productId) {
        return entityManager.createQuery(
                        "DELETE FROM ProductCategoryJpaEntity pi WHERE pi.product.id = :productId")
                .setParameter("productId", productId)
                .executeUpdate();
    }

    @Override
    public void delete(Long id) {
        entityManager.remove(entityManager.find(ProductJpaEntity.class, id));
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(b) FROM ProductJpaEntity b", Long.class).getSingleResult();
    }
}
