package com.fpmislata.back.persistence.repository.impl;

import com.fpmislata.back.persistence.dao.impl.entity.ProductJpaEntity;
import com.fpmislata.back.persistence.repository.mapper.ProductMapper;
import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.repository.ProductRepository;
import com.fpmislata.back.domain.repository.entity.ProductEntity;
import com.fpmislata.back.persistence.dao.ProductDao;

import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository {


    private final ProductDao productDao;

    public ProductRepositoryImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public Page<ProductEntity> findAll(int page, int size) {
        List<ProductEntity> content = productDao.findAll(page, size).stream()
                .map(ProductMapper.getInstance()::fromProductJpaEntityToProductEntity)
                .toList();
        long total = productDao.count();
        return new Page<>(content, page, size, total);
    }

    @Override
    public List<ProductEntity> findByName(String name) {
        return productDao.findByName(name).stream().map(ProductMapper.getInstance()::fromProductJpaEntityToProductEntity).toList();
    }

    @Override
    public Optional<ProductEntity> findById(Long id) {
        return productDao.findById(id).map(ProductMapper.getInstance()::fromProductJpaEntityToProductEntity);
    }

    @Override
    public ProductEntity save(ProductEntity productEntity) {
        ProductJpaEntity entity = ProductMapper.getInstance().fromProductEntityToProductJpaEntity(productEntity);
        if(entity.getId() == null){
            return ProductMapper.getInstance().fromProductJpaEntityToProductEntity(productDao.insert(entity));
        }
        return ProductMapper.getInstance().fromProductJpaEntityToProductEntity(productDao.update(entity));
    }

    @Override
    public void deleteById(Long id) {
        productDao.delete(id);
    }
}
