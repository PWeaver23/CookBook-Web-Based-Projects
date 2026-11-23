package com.cookbook.model;

import java.util.HashSet;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Represents a user account, with an id, email, and password. 
@Document(collection = "accounts")
public class Account {

    @Id
    private Long id;
    
    private String username;
    private String password_hash;
    private String role;

    private HashSet<String> favoriteRecipes;

    public Account() { }

    public Account(String username, String password_hash, String role, HashSet<String> favoriteRecipes) {
        this.username = username;
        this.password_hash = password_hash;
        this.role = role;
        this.favoriteRecipes = favoriteRecipes;
    }

    public Account(Long id, String username, String password_hash, String role, HashSet<String> favoriteRecipes) {
        this.id = id;
        this.username = username;
        this.password_hash = password_hash;
        this.role = role;
        this.favoriteRecipes = favoriteRecipes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return password_hash;
    }

    public void setPasswordHash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public HashSet<String> getFavoriteRecipes() {
        return favoriteRecipes;
    }

    public void setFavoriteRecipes(HashSet<String> favoriteRecipes) {
        this.favoriteRecipes = favoriteRecipes;
    }
}
