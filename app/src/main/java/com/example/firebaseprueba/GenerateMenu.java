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
    private int cantidadCatregories;
    private HashMap<String, Integer> cantidadPorCategoria;
    private final int diasDeMenu = 5;

    public GenerateMenu(User user, int cantidadCatregories) {
        this.user = user;
        this.cantidadCatregories = cantidadCatregories;
    }

    public GenerateMenu(User user, int cantidadCatregories, HashMap<String, Integer> cantidadPorCategoria, ConectionBD conectionBD) {
        this.user = user;
        this.cantidadCatregories = cantidadCatregories;
        this.cantidadPorCategoria = cantidadPorCategoria;
        this.conectionBD = conectionBD;
    }
    public List<Plate> getMenuForCat(){
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


    public List<Plate> getMenu(){
        List<Plate> plates = user.getPlates();
        List<Plate> menu = new ArrayList<>();
        int cantidad = 0;

        while (cantidad < diasDeMenu){
            int posPlate = (int) (Math.random() * plates.size());

            while (menu.contains(plates.get(posPlate))){
                posPlate = (int) (Math.random() * plates.size() - 1);
            }

            menu.add(plates.get(posPlate));
            cantidad++;
        }

        return menu;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCantidadCatregories() {
        return cantidadCatregories;
    }

    public void setCantidadCatregories(int cantidadCatregories) {
        this.cantidadCatregories = cantidadCatregories;
    }
}
