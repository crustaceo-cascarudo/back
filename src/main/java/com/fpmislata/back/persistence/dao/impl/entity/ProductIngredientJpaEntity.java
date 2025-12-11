package com.fpmislata.back.persistence.dao.impl.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_ingredient")
public class ProductIngredientJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductJpaEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private IngredientJpaEntity ingredient;
    public ProductIngredientJpaEntity() {}

    public ProductIngredientJpaEntity(ProductJpaEntity product, IngredientJpaEntity ingredient) {
        this.product = product;
        this.ingredient = ingredient;
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

    public IngredientJpaEntity getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientJpaEntity ingredient) {
        this.ingredient = ingredient;
    }
}
