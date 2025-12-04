package com.fpmislata.back.domain.model;

public class User {
    private Long id;
    private String name;
    private String passwordHash;
    private String role;
    
    public User() {
    }

    public User(Long id, String name, String passwordHash, String role) {
        this.id = id;
        this.name = name;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }

    public Boolean checkIfAdminRole() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }
    
}

