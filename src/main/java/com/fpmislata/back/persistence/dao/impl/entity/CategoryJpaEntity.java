package com.fpmislata.back.persistence.dao.impl.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "category")
public class CategoryJpaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Boolean estado;

    public CategoryJpaEntity() {
    }

    public CategoryJpaEntity(Long id, String name, String slug, String description, Boolean estado) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getEstado() {
        return estado;
    }

    
}


