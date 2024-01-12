package com.example.firebaseprueba;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String name;
    private List<String> plates;

    public Category(String name) {
        this.name = name;
        plates = new ArrayList<>();
    }

    public Category(String name, List<String> plates) {
        this.name = name;
        this.plates = plates;
    }

    public void addPlate(String plate){
        this.plates.add(plate);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPlates() {
        return plates;
    }

    public void setPlates(List<String> plates) {
        this.plates = plates;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", plates=" + plates +
                '}';
    }
}
