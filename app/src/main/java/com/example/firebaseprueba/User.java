package com.example.firebaseprueba;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String userId;
    private String email;
    private List<Plate> plates;

    public User() {
        plates = new ArrayList<>();
    }

    public User(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public int getPositionPlate(String plate, String cat){
        for(int i = 0; i < plates.size(); i++){
            if(plates.get(i).getCategory().equals(cat) && plates.get(i).getName().equals(plate)){
                return i;
            }
        }
        return -1;
    }

    public List<String> getCategories(){
        List<String> categories = new ArrayList<>();
        for(Plate p : plates){
            if (!categories.contains(p.getCategory())){
                categories.add(p.getCategory());
            }
        }

        return categories;
    }

    public void addPlate(Plate plate){
        this.plates.add(plate);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Plate> getPlates() {
        return plates;
    }

    public void setPlates(List<Plate> plates) {
        this.plates = plates;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
