package com.fpmislata.back.domain.service;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.service.dto.ProductDto;

import java.util.List;

public interface ProductService {
    Page<ProductDto> getAll(int page, int size);
    List<ProductDto> findByName(String name);
    ProductDto getById(Long id);
    ProductDto create(ProductDto productDto);
    ProductDto update(ProductDto productDto);
    void deleteById(Long id);
}
