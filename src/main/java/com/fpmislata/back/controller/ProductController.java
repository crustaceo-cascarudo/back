package com.fpmislata.back.controller;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.service.ProductService;
import com.fpmislata.back.domain.service.dto.ProductDto;

import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Page<ProductDto>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ProductDto> products = productService.findAll(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/category/{categorySlug}")
    public ResponseEntity<Page<ProductDto>> findByCategory(
            @PathVariable String categorySlug,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ProductDto> products = productService.findByCategory(categorySlug, page, size);
        System.out.println(products.data().getFirst());
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> findByName(@RequestParam String name) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody @Validated ProductDto productDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(productDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(
            @PathVariable Long id,
            @RequestBody ProductDto productDto
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.update(productDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
