package com.fpmislata.back.domain.model;

public class Category {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Boolean estado;

    public Category() {
    }

    public Category(Long id, String name, String slug, String description, Boolean estado) {
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
