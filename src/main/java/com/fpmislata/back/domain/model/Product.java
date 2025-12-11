package com.fpmislata.back.domain.model;

import java.util.List;

public class Product {
    Long id;
    String name;
    List<Ingredient> ingredients;
    double basePrice;
    Integer discountPercentage;
    Double finalPrice;
    String image;
    List<Category> categories;

    public Product(Long id, String name, List<Ingredient> ingredients, Double basePrice, Integer discountPercentage, String image, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.basePrice = basePrice;
        this.discountPercentage = discountPercentage;
        this.finalPrice = calcFinalPrice();
        this.image = image;
        this.categories = categories;
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

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public double calcFinalPrice(){
        if(discountPercentage == null){
            return basePrice;
        }
        return basePrice - (basePrice * discountPercentage);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
