package com.example.firebaseprueba;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Plate implements Comparable<Plate>, Serializable {
    private String name;
    private String category;


    public Plate(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @NonNull
    @Override
    public String toString() {
        return "Plate{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    @Override
    public int compareTo(Plate p) {
        return this.category.compareTo(p.getCategory());
    }
}
