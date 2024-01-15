package com.example.firebaseprueba;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConectionBD implements Serializable {
    private static FirebaseDatabase database;
    private static User user;

    public interface OnDataLoadedListener{
        void onDataLoaded();
    }

    public ConectionBD(FirebaseDatabase database, String userId) {
        ConectionBD.database = database;
        user = new User();
        user.setUserId(userId);
    }

    public ConectionBD(String userId) {
        user = new User();
        user.setUserId(userId);
    }

    public void getCategoriesFromDb(){
        DatabaseReference dbr = database.getReference("MenuMaker").child(user.getUserId()).child("categorias");

        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Plate> plates = new ArrayList<>();
                Plate plate = null;
                //Category category = null;
                for(DataSnapshot categoria : snapshot.getChildren()){
                    //category = new Category(categoria.getKey());
                    for(DataSnapshot plato : categoria.getChildren()){
                        plates.add(new Plate(plato.getKey(), categoria.getKey()));
                        //category.addPlate(plato.getKey());
                    }
                    //user.addCategory(category);
                }
                user.setPlates(plates);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getCategoriesFromDb(OnDataLoadedListener listener){
        DatabaseReference dbr = database.getReference("MenuMaker").child(user.getUserId()).child("categorias");

        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Plate> plates = new ArrayList<>();
                Plate plate = null;
                //Category category = null;
                for(DataSnapshot categoria : snapshot.getChildren()){
                    //category = new Category(categoria.getKey());
                    for(DataSnapshot plato : categoria.getChildren()){
                        plates.add(new Plate(plato.getKey(), categoria.getKey()));
                        //category.addPlate(plato.getKey());
                    }
                    //user.addCategory(category);
                }
                user.setPlates(plates);
                Log.d("plates from connection", user.getPlates().toString());

                if (listener != null) {
                    listener.onDataLoaded();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static FirebaseDatabase getDatabase() {
        return database;
    }

    public User getUser() {
        return user;
    }
}
