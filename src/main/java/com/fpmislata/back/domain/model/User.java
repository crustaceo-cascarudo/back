package com.fpmislata.back.domain.model;

import com.fpmislata.back.domain.enumerado.Role;

public class User {
    private Long id;
    private String name;
    private String passwordHash;
    private Role role;
    
    public User() {
    }

    public User(Long id, String name, String passwordHash, Role role) {
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

    public Role getRole() {
        return role;
    }

    public Boolean checkIfAdminRole() {
        return Role.ADMIN.equals(this.role);
    }
    
}

