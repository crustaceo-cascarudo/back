package com.fpmislata.back.domain.repository;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.repository.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Page<ProductEntity> findAll(int page, int size);
    List<ProductEntity> findByName(String name);
    Optional<ProductEntity> findById(Long id);
    ProductEntity save(ProductEntity productEntity);
    void deleteById(Long id);
}
