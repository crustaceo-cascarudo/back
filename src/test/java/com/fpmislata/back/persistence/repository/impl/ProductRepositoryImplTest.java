package com.fpmislata.back.persistence.repository.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.repository.entity.ProductEntity;
import com.fpmislata.back.persistence.dao.ProductDao;
import com.fpmislata.back.persistence.dao.impl.entity.ProductJpaEntity;
import com.fpmislata.back.persistence.repository.mapper.ProductMapper;

class ProductRepositoryImplTest {

    private ProductDao productDao;
    private ProductRepositoryImpl productRepository;
    private MockedStatic<ProductMapper> productMapperStatic;
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productDao = mock(ProductDao.class);
        productRepository = new ProductRepositoryImpl(productDao);

        productMapper = mock(ProductMapper.class);
        productMapperStatic = mockStatic(ProductMapper.class);
        productMapperStatic.when(ProductMapper::getInstance).thenReturn(productMapper);
    }

    @AfterEach
    void tearDown() {
        productMapperStatic.close();
    }

    @Test
    void findAll_returnsPageOfProductEntities() {
        ProductJpaEntity jpa = new ProductJpaEntity(1L, "Burger",
                new java.util.ArrayList<>(), 10.0, null, "img.png", new java.util.ArrayList<>());
        ProductEntity entity = new ProductEntity(1L, "Burger", List.of(), 10.0, null, "img.png", List.of());

        when(productDao.findAll(1, 10)).thenReturn(List.of(jpa));
        when(productDao.count()).thenReturn(1L);
        when(productMapper.fromProductJpaEntityToProductEntity(jpa)).thenReturn(entity);

        Page<ProductEntity> page = productRepository.findAll(1, 10);

        assertEquals(1, page.data().size());
        assertEquals(entity, page.data().get(0));
        assertEquals(1, page.totalElements());
    }

    @Test
    void findByCategory_returnsFilteredPage() {
        ProductJpaEntity jpa = new ProductJpaEntity(2L, "Pizza",
                new java.util.ArrayList<>(), 12.0, 5, "pizza.png", new java.util.ArrayList<>());
        ProductEntity entity = new ProductEntity(2L, "Pizza", List.of(), 12.0, 5, "pizza.png", List.of());

        when(productDao.findByCategory("pizzas", 1, 10)).thenReturn(List.of(jpa));
        when(productDao.countByCategory("pizzas")).thenReturn(1L);
        when(productMapper.fromProductJpaEntityToProductEntity(jpa)).thenReturn(entity);

        Page<ProductEntity> page = productRepository.findByCategory("pizzas", 1, 10);

        assertEquals(1, page.data().size());
        assertEquals("Pizza", page.data().get(0).name());
    }

    @Test
    void findByName_delegatesToDaoAndMaps() {
        ProductJpaEntity jpa = new ProductJpaEntity(1L, "Burger",
                new java.util.ArrayList<>(), 10.0, null, "img.png", new java.util.ArrayList<>());
        ProductEntity entity = new ProductEntity(1L, "Burger", List.of(), 10.0, null, "img.png", List.of());

        when(productDao.findByName("Burger")).thenReturn(List.of(jpa));
        when(productMapper.fromProductJpaEntityToProductEntity(jpa)).thenReturn(entity);

        List<ProductEntity> result = productRepository.findByName("Burger");

        assertEquals(1, result.size());
        assertEquals("Burger", result.get(0).name());
    }

    @Test
    void findById_exists_returnsMappedEntity() {
        ProductJpaEntity jpa = new ProductJpaEntity(1L, "Burger",
                new java.util.ArrayList<>(), 10.0, null, "img.png", new java.util.ArrayList<>());
        ProductEntity entity = new ProductEntity(1L, "Burger", List.of(), 10.0, null, "img.png", List.of());

        when(productDao.findById(1L)).thenReturn(Optional.of(jpa));
        when(productMapper.fromProductJpaEntityToProductEntity(jpa)).thenReturn(entity);

        Optional<ProductEntity> result = productRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Burger", result.get().name());
    }

    @Test
    void findById_notFound_returnsEmpty() {
        when(productDao.findById(999L)).thenReturn(Optional.empty());

        Optional<ProductEntity> result = productRepository.findById(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void save_newProduct_callsInsert() {
        ProductEntity entity = new ProductEntity(null, "New Burger", List.of(), 11.0, null, "new.png", List.of());
        ProductJpaEntity jpa = new ProductJpaEntity(null, "New Burger",
                new java.util.ArrayList<>(), 11.0, null, "new.png", new java.util.ArrayList<>());
        ProductJpaEntity savedJpa = new ProductJpaEntity(1L, "New Burger",
                new java.util.ArrayList<>(), 11.0, null, "new.png", new java.util.ArrayList<>());
        ProductEntity savedEntity = new ProductEntity(1L, "New Burger", List.of(), 11.0, null, "new.png", List.of());

        when(productMapper.fromProductEntityToProductJpaEntity(entity)).thenReturn(jpa);
        when(productDao.insert(jpa)).thenReturn(savedJpa);
        when(productMapper.fromProductJpaEntityToProductEntity(savedJpa)).thenReturn(savedEntity);

        ProductEntity result = productRepository.save(entity);

        assertEquals(1L, result.id());
        verify(productDao).insert(jpa);
        verify(productDao, never()).update(any());
    }

    @Test
    void save_existingProduct_callsUpdate() {
        ProductEntity entity = new ProductEntity(1L, "Updated Burger", List.of(), 12.0, null, "up.png", List.of());
        ProductJpaEntity jpa = new ProductJpaEntity(1L, "Updated Burger",
                new java.util.ArrayList<>(), 12.0, null, "up.png", new java.util.ArrayList<>());
        ProductJpaEntity updatedJpa = new ProductJpaEntity(1L, "Updated Burger",
                new java.util.ArrayList<>(), 12.0, null, "up.png", new java.util.ArrayList<>());
        ProductEntity updatedEntity = new ProductEntity(1L, "Updated Burger", List.of(), 12.0, null, "up.png", List.of());

        when(productMapper.fromProductEntityToProductJpaEntity(entity)).thenReturn(jpa);
        when(productDao.update(jpa)).thenReturn(updatedJpa);
        when(productMapper.fromProductJpaEntityToProductEntity(updatedJpa)).thenReturn(updatedEntity);

        ProductEntity result = productRepository.save(entity);

        assertEquals("Updated Burger", result.name());
        verify(productDao).update(jpa);
        verify(productDao, never()).insert(any());
    }

    @Test
    void deleteById_delegatesToDao() {
        productRepository.deleteById(1L);

        verify(productDao).delete(1L);
    }
}
