package com.fpmislata.back.persistence.dao.impl.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "ingredient")
public class IngredientJpaEntity implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    private String image;

    public IngredientJpaEntity() {
    }

    public IngredientJpaEntity(Long id, String name, double price, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
