package com.fpmislata.back.domain.service.impl;

import com.fpmislata.back.domain.Exception.BusinessException;
import com.fpmislata.back.domain.Exception.ResourceNotFoundException;
import com.fpmislata.back.domain.mapper.IngredientMapper;
import com.fpmislata.back.domain.model.Ingredient;
import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.repository.IngredientRepository;
import com.fpmislata.back.domain.repository.entity.IngredientEntity;
import com.fpmislata.back.domain.service.dto.IngredientDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private IngredientMapper ingredientMapperMock;

    @InjectMocks
    private IngredientServiceImpl ingredientService;

    private MockedStatic<IngredientMapper> mockedStaticIngredientMapper;

    @BeforeEach
    void setUp() {
        mockedStaticIngredientMapper = mockStatic(IngredientMapper.class);
        when(IngredientMapper.getInstance()).thenReturn(ingredientMapperMock);
    }

    @AfterEach
    void tearDown() {
        mockedStaticIngredientMapper.close();
    }

    private IngredientDto createIngredientDto(Long id, String name) {
        return new IngredientDto(id, name, 1.5, "img.jpg");
    }

    private IngredientEntity createIngredientEntity(Long id, String name) {
        return new IngredientEntity(id, name, 1.5, "img.jpg");
    }

    private Ingredient createIngredient(Long id, String name) {
        return new Ingredient(id, name, 1.5, "img.jpg");
    }

    // === create ===

    @Test
    void create_whenNameIsUnique_shouldCreateIngredient() {
        IngredientDto inputDto = createIngredientDto(null, "Queso");
        Ingredient ingredient = createIngredient(null, "Queso");
        IngredientEntity entityToSave = createIngredientEntity(null, "Queso");
        IngredientEntity savedEntity = createIngredientEntity(1L, "Queso");
        Ingredient savedIngredient = createIngredient(1L, "Queso");
        IngredientDto expectedDto = createIngredientDto(1L, "Queso");

        when(ingredientRepository.findByName("Queso")).thenReturn(Collections.emptyList());
        when(ingredientMapperMock.fromIngredientDtoToIngredient(inputDto)).thenReturn(ingredient);
        when(ingredientMapperMock.fromIngredientToIngredientEntity(ingredient)).thenReturn(entityToSave);
        when(ingredientRepository.save(entityToSave)).thenReturn(savedEntity);
        when(ingredientMapperMock.fromIngredientEntityToIngredient(savedEntity)).thenReturn(savedIngredient);
        when(ingredientMapperMock.fromIngredientToIngredientDto(savedIngredient)).thenReturn(expectedDto);

        IngredientDto result = ingredientService.create(inputDto);

        assertEquals(1L, result.id());
        assertEquals("Queso", result.name());
    }

    @Test
    void create_whenNameExists_shouldThrowBusinessException() {
        IngredientDto inputDto = createIngredientDto(null, "Queso");
        IngredientEntity existingEntity = createIngredientEntity(1L, "Queso");
        Ingredient existingIngredient = createIngredient(1L, "Queso");
        IngredientDto existingDto = createIngredientDto(1L, "Queso");

        when(ingredientRepository.findByName("Queso")).thenReturn(List.of(existingEntity));
        when(ingredientMapperMock.fromIngredientEntityToIngredient(existingEntity)).thenReturn(existingIngredient);
        when(ingredientMapperMock.fromIngredientToIngredientDto(existingIngredient)).thenReturn(existingDto);

        assertThrows(BusinessException.class, () -> ingredientService.create(inputDto));
    }

    // === getById ===

    @Test
    void getById_whenNotExists_shouldThrowResourceNotFoundException() {
        when(ingredientRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> ingredientService.getById(99L));
    }

    @Test
    void getById_whenExists_shouldReturnIngredient() {
        IngredientEntity entity = createIngredientEntity(1L, "Queso");
        Ingredient ingredient = createIngredient(1L, "Queso");
        IngredientDto dto = createIngredientDto(1L, "Queso");

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(ingredientMapperMock.fromIngredientEntityToIngredient(entity)).thenReturn(ingredient);
        when(ingredientMapperMock.fromIngredientToIngredientDto(ingredient)).thenReturn(dto);

        IngredientDto result = ingredientService.getById(1L);
        assertEquals("Queso", result.name());
    }

    // === update ===

    @Test
    void update_whenExistsAndNameIsUnique_shouldUpdate() {
        IngredientDto inputDto = createIngredientDto(1L, "QuesoUpdated");
        IngredientEntity existingEntity = createIngredientEntity(1L, "Queso");
        Ingredient updatedIngredient = createIngredient(1L, "QuesoUpdated");
        IngredientEntity updatedEntity = createIngredientEntity(1L, "QuesoUpdated");
        IngredientEntity savedEntity = createIngredientEntity(1L, "QuesoUpdated");
        Ingredient savedIngredient = createIngredient(1L, "QuesoUpdated");
        IngredientDto expectedDto = createIngredientDto(1L, "QuesoUpdated");

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(ingredientRepository.findByName("QuesoUpdated")).thenReturn(Collections.emptyList());
        when(ingredientMapperMock.fromIngredientDtoToIngredient(inputDto)).thenReturn(updatedIngredient);
        when(ingredientMapperMock.fromIngredientToIngredientEntity(updatedIngredient)).thenReturn(updatedEntity);
        when(ingredientRepository.save(updatedEntity)).thenReturn(savedEntity);
        when(ingredientMapperMock.fromIngredientEntityToIngredient(savedEntity)).thenReturn(savedIngredient);
        when(ingredientMapperMock.fromIngredientToIngredientDto(savedIngredient)).thenReturn(expectedDto);

        IngredientDto result = ingredientService.update(inputDto);
        assertEquals("QuesoUpdated", result.name());
    }

    @Test
    void update_whenNotExists_shouldThrowResourceNotFoundException() {
        IngredientDto inputDto = createIngredientDto(99L, "Queso");
        when(ingredientRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> ingredientService.update(inputDto));
    }

    @Test
    void update_whenNameExistsOnAnother_shouldThrowBusinessException() {
        IngredientDto inputDto = createIngredientDto(1L, "Pepperoni");
        IngredientEntity existingEntity = createIngredientEntity(1L, "Queso");
        IngredientEntity conflictEntity = createIngredientEntity(2L, "Pepperoni");
        Ingredient conflictIngredient = createIngredient(2L, "Pepperoni");
        IngredientDto conflictDto = createIngredientDto(2L, "Pepperoni");

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(ingredientRepository.findByName("Pepperoni")).thenReturn(List.of(conflictEntity));
        when(ingredientMapperMock.fromIngredientEntityToIngredient(conflictEntity)).thenReturn(conflictIngredient);
        when(ingredientMapperMock.fromIngredientToIngredientDto(conflictIngredient)).thenReturn(conflictDto);

        assertThrows(BusinessException.class, () -> ingredientService.update(inputDto));
    }

    // === deleteById ===

    @Test
    void deleteById_whenExists_shouldDelete() {
        IngredientEntity entity = createIngredientEntity(1L, "Queso");
        Ingredient ingredient = createIngredient(1L, "Queso");
        IngredientDto dto = createIngredientDto(1L, "Queso");

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(ingredientMapperMock.fromIngredientEntityToIngredient(entity)).thenReturn(ingredient);
        when(ingredientMapperMock.fromIngredientToIngredientDto(ingredient)).thenReturn(dto);

        ingredientService.deleteById(1L);
        verify(ingredientRepository).deleteById(1L);
    }

    @Test
    void deleteById_whenNotExists_shouldThrowResourceNotFoundException() {
        when(ingredientRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> ingredientService.deleteById(99L));
    }

    // === findAll ===

    @Test
    void findAll_shouldReturnPagedIngredients() {
        IngredientEntity entity = createIngredientEntity(1L, "Queso");
        Page<IngredientEntity> entityPage = new Page<>(List.of(entity), 1, 10, 1);
        Ingredient ingredient = createIngredient(1L, "Queso");
        IngredientDto dto = createIngredientDto(1L, "Queso");

        when(ingredientRepository.findAll(1, 10)).thenReturn(entityPage);
        when(ingredientMapperMock.fromIngredientEntityToIngredient(entity)).thenReturn(ingredient);
        when(ingredientMapperMock.fromIngredientToIngredientDto(ingredient)).thenReturn(dto);

        Page<IngredientDto> result = ingredientService.findAll(1, 10);
        assertEquals(1, result.data().size());
    }
}
