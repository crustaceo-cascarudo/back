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
import com.fpmislata.back.domain.repository.entity.IngredientEntity;
import com.fpmislata.back.persistence.dao.IngredientDao;
import com.fpmislata.back.persistence.dao.impl.entity.IngredientJpaEntity;
import com.fpmislata.back.persistence.repository.mapper.IngredientMapper;

class IngredientRepositoryImplTest {

    private IngredientDao ingredientDao;
    private IngredientRepositoryImpl ingredientRepository;
    private MockedStatic<IngredientMapper> ingredientMapperStatic;
    private IngredientMapper ingredientMapper;

    @BeforeEach
    void setUp() {
        ingredientDao = mock(IngredientDao.class);
        ingredientRepository = new IngredientRepositoryImpl(ingredientDao);

        ingredientMapper = mock(IngredientMapper.class);
        ingredientMapperStatic = mockStatic(IngredientMapper.class);
        ingredientMapperStatic.when(IngredientMapper::getInstance).thenReturn(ingredientMapper);
    }

    @AfterEach
    void tearDown() {
        ingredientMapperStatic.close();
    }

    @Test
    void findAll_returnsPageOfIngredientEntities() {
        IngredientJpaEntity jpa = new IngredientJpaEntity(1L, "Lettuce", 0.5, "lettuce.png");
        IngredientEntity entity = new IngredientEntity(1L, "Lettuce", 0.5, "lettuce.png");

        when(ingredientDao.findAll(1, 10)).thenReturn(List.of(jpa));
        when(ingredientDao.count()).thenReturn(1L);
        when(ingredientMapper.fromIngredientJpaEntityToIngredientEntity(jpa)).thenReturn(entity);

        Page<IngredientEntity> page = ingredientRepository.findAll(1, 10);

        assertEquals(1, page.data().size());
        assertEquals("Lettuce", page.data().get(0).name());
    }

    @Test
    void findById_exists_returnsMappedEntity() {
        IngredientJpaEntity jpa = new IngredientJpaEntity(1L, "Lettuce", 0.5, "lettuce.png");
        IngredientEntity entity = new IngredientEntity(1L, "Lettuce", 0.5, "lettuce.png");

        when(ingredientDao.findById(1L)).thenReturn(Optional.of(jpa));
        when(ingredientMapper.fromIngredientJpaEntityToIngredientEntity(jpa)).thenReturn(entity);

        Optional<IngredientEntity> result = ingredientRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Lettuce", result.get().name());
    }

    @Test
    void findById_notFound_returnsEmpty() {
        when(ingredientDao.findById(999L)).thenReturn(Optional.empty());

        Optional<IngredientEntity> result = ingredientRepository.findById(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByName_delegatesToDaoAndMaps() {
        IngredientJpaEntity jpa = new IngredientJpaEntity(1L, "Lettuce", 0.5, "lettuce.png");
        IngredientEntity entity = new IngredientEntity(1L, "Lettuce", 0.5, "lettuce.png");

        when(ingredientDao.findByName("Lettuce")).thenReturn(List.of(jpa));
        when(ingredientMapper.fromIngredientJpaEntityToIngredientEntity(jpa)).thenReturn(entity);

        List<IngredientEntity> result = ingredientRepository.findByName("Lettuce");

        assertEquals(1, result.size());
    }

    @Test
    void save_newIngredient_callsInsert() {
        IngredientEntity entity = new IngredientEntity(null, "Cheese", 1.2, "cheese.png");
        IngredientJpaEntity jpa = new IngredientJpaEntity(null, "Cheese", 1.2, "cheese.png");
        IngredientJpaEntity savedJpa = new IngredientJpaEntity(1L, "Cheese", 1.2, "cheese.png");
        IngredientEntity savedEntity = new IngredientEntity(1L, "Cheese", 1.2, "cheese.png");

        when(ingredientMapper.fromIngredientEntityToIngredientJpaEntity(entity)).thenReturn(jpa);
        when(ingredientDao.insert(jpa)).thenReturn(savedJpa);
        when(ingredientMapper.fromIngredientJpaEntityToIngredientEntity(savedJpa)).thenReturn(savedEntity);

        IngredientEntity result = ingredientRepository.save(entity);

        assertEquals(1L, result.id());
        verify(ingredientDao).insert(jpa);
    }

    @Test
    void save_existingIngredient_callsUpdate() {
        IngredientEntity entity = new IngredientEntity(1L, "Updated", 2.0, "updated.png");
        IngredientJpaEntity jpa = new IngredientJpaEntity(1L, "Updated", 2.0, "updated.png");
        IngredientJpaEntity updatedJpa = new IngredientJpaEntity(1L, "Updated", 2.0, "updated.png");
        IngredientEntity updatedEntity = new IngredientEntity(1L, "Updated", 2.0, "updated.png");

        when(ingredientMapper.fromIngredientEntityToIngredientJpaEntity(entity)).thenReturn(jpa);
        when(ingredientDao.update(jpa)).thenReturn(updatedJpa);
        when(ingredientMapper.fromIngredientJpaEntityToIngredientEntity(updatedJpa)).thenReturn(updatedEntity);

        IngredientEntity result = ingredientRepository.save(entity);

        assertEquals("Updated", result.name());
        verify(ingredientDao).update(jpa);
    }

    @Test
    void deleteById_delegatesToDao() {
        ingredientRepository.deleteById(1L);

        verify(ingredientDao).delete(1L);
    }
}
