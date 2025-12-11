package com.fpmislata.back.persistence.dao.impl.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_category")
public class ProductCategoryJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductJpaEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryJpaEntity category;
    public ProductCategoryJpaEntity() {}

    public ProductCategoryJpaEntity(ProductJpaEntity product, CategoryJpaEntity category) {
        this.product = product;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public ProductJpaEntity getProduct() {
        return product;
    }

    public void setProduct(ProductJpaEntity product) {
        this.product = product;
    }

    public CategoryJpaEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryJpaEntity category) {
        this.category = category;
    }
}
