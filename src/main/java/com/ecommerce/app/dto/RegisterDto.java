package com.ecommerce.app.dto;

import com.ecommerce.app.entities.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class RegisterDto {
    private String name;
    private String email;
    private String password;
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public RegisterDto() {
        this.role = UserRole.USER;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
