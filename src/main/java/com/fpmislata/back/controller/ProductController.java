package com.fpmislata.back.controller;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.service.ProductService;
import com.fpmislata.back.domain.service.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<List<ProductDto>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ProductDto> products = productService.getAll(page, size);

        return ResponseEntity.ok(products.data());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<ProductDto>> findByName(@PathVariable String name) {
        return ResponseEntity.ok(productService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody @Validated ProductDto productDto) {
        return ResponseEntity.ok(productService.create(productDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(
            @PathVariable Long id,
            @RequestBody ProductDto productDto
    ) {
        return ResponseEntity.ok(productService.update(productDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.notFound().build();
    }
}
