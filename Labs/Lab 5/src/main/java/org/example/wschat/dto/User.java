package org.example.wschat.dto;

public class User {
    private String username;
    private String role; // "admin" or "client"

    public User(String username) {
        this.username = username;
        this.role = "admin".equals(username) ? "admin" : "client";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return "admin".equals(role);
    }
} 