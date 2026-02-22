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
import com.fpmislata.back.domain.repository.entity.CategoryEntity;
import com.fpmislata.back.persistence.dao.CategoryDao;
import com.fpmislata.back.persistence.dao.impl.entity.CategoryJpaEntity;
import com.fpmislata.back.persistence.repository.mapper.CategoryMapper;

class CategoryRepositoryImplTest {

    private CategoryDao categoryDao;
    private CategoryRepositoryImpl categoryRepository;
    private MockedStatic<CategoryMapper> categoryMapperStatic;
    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        categoryDao = mock(CategoryDao.class);
        categoryRepository = new CategoryRepositoryImpl(categoryDao);

        categoryMapper = mock(CategoryMapper.class);
        categoryMapperStatic = mockStatic(CategoryMapper.class);
        categoryMapperStatic.when(CategoryMapper::getInstance).thenReturn(categoryMapper);
    }

    @AfterEach
    void tearDown() {
        categoryMapperStatic.close();
    }

    @Test
    void findAllPaged_returnsPageOfCategoryEntities() {
        CategoryJpaEntity jpa = new CategoryJpaEntity(1L, "Burgers", "burgers", "Desc", true);
        CategoryEntity entity = new CategoryEntity(1L, "Burgers", "burgers", "Desc", true);

        when(categoryDao.findAll(1, 10)).thenReturn(List.of(jpa));
        when(categoryDao.count()).thenReturn(1L);
        when(categoryMapper.fromCategoryJpaEntityToEntity(jpa)).thenReturn(entity);

        Page<CategoryEntity> page = categoryRepository.findAllPaged(1, 10);

        assertEquals(1, page.data().size());
        assertEquals("Burgers", page.data().get(0).name());
    }

    @Test
    void findById_exists_returnsMappedEntity() {
        CategoryJpaEntity jpa = new CategoryJpaEntity(1L, "Burgers", "burgers", "Desc", true);
        CategoryEntity entity = new CategoryEntity(1L, "Burgers", "burgers", "Desc", true);

        when(categoryDao.findById(1L)).thenReturn(Optional.of(jpa));
        when(categoryMapper.fromCategoryJpaEntityToEntity(jpa)).thenReturn(entity);

        Optional<CategoryEntity> result = categoryRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Burgers", result.get().name());
    }

    @Test
    void findById_notFound_returnsEmpty() {
        when(categoryDao.findById(999L)).thenReturn(Optional.empty());

        Optional<CategoryEntity> result = categoryRepository.findById(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByName_delegatesToDaoAndMaps() {
        CategoryJpaEntity jpa = new CategoryJpaEntity(1L, "Burgers", "burgers", "Desc", true);
        CategoryEntity entity = new CategoryEntity(1L, "Burgers", "burgers", "Desc", true);

        when(categoryDao.findByName("Burgers")).thenReturn(List.of(jpa));
        when(categoryMapper.fromCategoryJpaEntityToEntity(jpa)).thenReturn(entity);

        List<CategoryEntity> result = categoryRepository.findByName("Burgers");

        assertEquals(1, result.size());
    }

    @Test
    void save_newCategory_callsInsert() {
        CategoryEntity entity = new CategoryEntity(null, "New", "new", "Desc", true);
        CategoryJpaEntity jpa = new CategoryJpaEntity(null, "New", "new", "Desc", true);
        CategoryJpaEntity savedJpa = new CategoryJpaEntity(1L, "New", "new", "Desc", true);
        CategoryEntity savedEntity = new CategoryEntity(1L, "New", "new", "Desc", true);

        when(categoryMapper.fromCategoryEntityToJpaEntity(entity)).thenReturn(jpa);
        when(categoryDao.insert(jpa)).thenReturn(savedJpa);
        when(categoryMapper.fromCategoryJpaEntityToEntity(savedJpa)).thenReturn(savedEntity);

        CategoryEntity result = categoryRepository.save(entity);

        assertEquals(1L, result.id());
        verify(categoryDao).insert(jpa);
    }

    @Test
    void save_existingCategory_callsUpdate() {
        CategoryEntity entity = new CategoryEntity(1L, "Updated", "updated", "Updated Desc", false);
        CategoryJpaEntity jpa = new CategoryJpaEntity(1L, "Updated", "updated", "Updated Desc", false);
        CategoryJpaEntity existingJpa = new CategoryJpaEntity(1L, "Old", "old", "Old Desc", true);

        when(categoryMapper.fromCategoryEntityToJpaEntity(entity)).thenReturn(jpa);
        when(categoryDao.findById(1L)).thenReturn(Optional.of(existingJpa));
        when(categoryDao.update(jpa)).thenReturn(jpa);

        CategoryEntity result = categoryRepository.save(entity);

        assertEquals("Updated", result.name());
        verify(categoryDao).update(jpa);
    }

    @Test
    void delete_delegatesToDao() {
        categoryRepository.delete(1L);

        verify(categoryDao).delete(1L);
    }
}
