package com.fpmislata.back.controller;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.service.IngredientService;
import com.fpmislata.back.domain.service.dto.IngredientDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("")
    public ResponseEntity<Page<IngredientDto>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<IngredientDto> ingredients = ingredientService.findAll(page, size);

        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<IngredientDto>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(ingredientService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<IngredientDto> create(@RequestBody @Validated IngredientDto ingredientDto) {
        return ResponseEntity.ok(ingredientService.create(ingredientDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientDto> update(
            @PathVariable Long id,
            @RequestBody IngredientDto ingredientDto
    ) {
        return ResponseEntity.ok(ingredientService.update(ingredientDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ingredientService.deleteById(id);
        return ResponseEntity.notFound().build();
    }
}
