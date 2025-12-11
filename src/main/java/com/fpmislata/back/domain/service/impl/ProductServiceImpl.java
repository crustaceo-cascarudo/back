package com.fpmislata.back.domain.service.impl;

import com.fpmislata.back.domain.Exception.BusinessException;
import com.fpmislata.back.domain.Exception.ResourceNotFoundException;
import com.fpmislata.back.domain.mapper.ProductMapper;
import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.repository.ProductRepository;
import com.fpmislata.back.domain.repository.entity.ProductEntity;
import com.fpmislata.back.domain.service.ProductService;
import com.fpmislata.back.domain.service.dto.ProductDto;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    
    @Override
    public Page<ProductDto> getAll(int page, int size) {
        Page<ProductEntity> productEntityPage = productRepository.findAll(page, size);
        List<ProductDto> productDtos = productEntityPage.data()
                .stream()
                .map(ProductMapper.getInstance()::fromProductEntityToProduct)
                .map(ProductMapper.getInstance()::fromProductToProductDto)
                .toList();
        
        return new Page<>(
                productDtos,
                productEntityPage.pageNumber(),
                productEntityPage.pageSize(),
                productEntityPage.totalPages()
        );
    }

    @Override
    public List<ProductDto> findByName(String name) {
        return productRepository
                .findByName(name)
                .stream()
                .map(ProductMapper.getInstance()::fromProductEntityToProduct)
                .map(ProductMapper.getInstance()::fromProductToProductDto)
                .toList();
    }

    @Override
    public ProductDto getById(Long id) {
        return productRepository
                .findById(id)
                .map(ProductMapper.getInstance()::fromProductEntityToProduct)
                .map(ProductMapper.getInstance()::fromProductToProductDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id '"+id+"' not found"));
    }

    @Override
    public ProductDto create(ProductDto productDto) {
        if (findByName(productDto.name()).getFirst().name().equalsIgnoreCase(productDto.name())) {
            throw new BusinessException("Product with name " + productDto.name() + " already exists");
        }

        ProductEntity newProductEntity = ProductMapper.getInstance()
                .fromProductToProductEntity(
                        ProductMapper.getInstance().fromProductDtoToProduct(productDto)
                );

        return ProductMapper.getInstance().fromProductToProductDto(
                ProductMapper.getInstance().fromProductEntityToProduct(
                        productRepository.save(newProductEntity)
                )
        );
    }

    @Override
    public ProductDto update(ProductDto productDto) {
        productRepository.findById(productDto.id())
                .orElseThrow(() -> new ResourceNotFoundException("Product with id '"+productDto.id()+"' not found"));

        if(productRepository.findByName(productDto.name()).getFirst().name().equalsIgnoreCase(productDto.name()))
            if(!productRepository.findByName(productDto.name()).getFirst().id().equals(productDto.id())) {
                throw new BusinessException("Ingredient with name '" + productDto.name() + "' already exists");
            }

        ProductEntity productEntity = ProductMapper.getInstance().fromProductToProductEntity(
                ProductMapper.getInstance().fromProductDtoToProduct(productDto)
        );

        return ProductMapper.getInstance().fromProductToProductDto(
                ProductMapper.getInstance().fromProductEntityToProduct(
                        productRepository.save(productEntity)
                )
        );
    }

    @Override
    public void deleteById(Long id) {
        if(productRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Product with id '" + id + "' not found");
        }
       productRepository.deleteById(id);
    }
}
