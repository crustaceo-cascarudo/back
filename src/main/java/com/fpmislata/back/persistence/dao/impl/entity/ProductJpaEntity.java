package com.fpmislata.back.persistence.dao.impl.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "product")
public class ProductJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductIngredientJpaEntity> productIngredients;
    private double basePrice;
    private Integer discountPercentage;
    private String image;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCategoryJpaEntity> productCategories;

    public ProductJpaEntity() {
    }

    public ProductJpaEntity(Long id, String name, List<ProductIngredientJpaEntity> productIngredients, double basePrice,
                            Integer discountPercentage, String image, List<ProductCategoryJpaEntity> categories) {
        this.id = id;
        this.name = name;
        this.productIngredients = productIngredients;
        this.basePrice = basePrice;
        this.discountPercentage = discountPercentage;
        this.image = image;
        this.productCategories = categories;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientJpaEntity> getProductIngredients() {
        return productIngredients.stream().map(ProductIngredientJpaEntity::getIngredient).collect(Collectors.toList());
    }

    public void setProductIngredients(List<IngredientJpaEntity> productIngredients) {
        this.productIngredients.clear();
        for(IngredientJpaEntity ingredientJpa : productIngredients){
            ProductIngredientJpaEntity productIngredientJpa = new ProductIngredientJpaEntity(this, ingredientJpa);
            this.productIngredients.add(productIngredientJpa);
        }
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<CategoryJpaEntity> getProductCategories() {
        return productCategories.stream().map(ProductCategoryJpaEntity::getCategory).collect(Collectors.toList());
    }

    public void setProductCategories(List<CategoryJpaEntity> productCategories) {
        this.productCategories.clear();
        for(CategoryJpaEntity categoryJpa : productCategories){
            ProductCategoryJpaEntity productCategoryJpa = new ProductCategoryJpaEntity(this, categoryJpa);
            this.productCategories.add(productCategoryJpa);
        }
    }
}
