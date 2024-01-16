package com.example.firebaseprueba;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GenerateMenu {

    private User user;
    private ConectionBD conectionBD;

    public GenerateMenu(User user , ConectionBD conectionBD) {
        this.user = user;
        this.conectionBD = conectionBD;
    }
    public List<Plate> getMenuForCat(HashMap<String, Integer> cantidadPorCategoria){
        HashMap<String, List<Plate>> categories = new HashMap<>();
        List<Plate> plates = user.getPlates();
        List<Plate> menu = new ArrayList<>();

        for(Plate plate : plates){
            String cat = plate.getCategory();
            categories.put(cat, new ArrayList<>());
        }

        for(Plate plate: plates){
            for(Map.Entry<String, List<Plate>> entry : categories.entrySet()){
                String cat = entry.getKey();
                if(plate.getCategory().equals(cat)){
                    entry.getValue().add(plate);
                }
            }
        }

        for(Map.Entry<String, Integer> entry : cantidadPorCategoria.entrySet()){
            String cat = entry.getKey();
            int cantidad = entry.getValue();
            while (cantidad > 0){
                List<Plate> p = categories.get(cat);

                int rand = (int) (Math.random() * p.size());

                while (menu.contains(p.get(rand))){
                    rand = (int) (Math.random() * p.size());
                }

                menu.add(p.get(rand));
                cantidad--;
            }
        }

        return menu;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
