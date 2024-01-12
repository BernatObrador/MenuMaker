package com.example.firebaseprueba;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String email;
    private List<Category> categories;

    public User() {
        categories = new ArrayList<>();
    }

    public User(String userId, String email, List<Category> categories) {
        this.userId = userId;
        this.email = email;
        this.categories = categories;
    }

    public void addCategory(Category category){
        this.categories.add(category);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
