package com.fpmislata.back.persistence.dao;

import com.fpmislata.back.persistence.dao.impl.entity.ProductJpaEntity;

import java.util.List;

public interface ProductDao extends GenericDao<ProductJpaEntity> {
    List<ProductJpaEntity> findByCategory(String categorySlug, int pageNumber, int pageSize);
    long countByCategory(String categorySlug);
}
