package com.example.firebaseprueba;

import android.content.Context;
import android.widget.Toast;

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

    public interface OnCantidadPlatosCategoriaListener {
        void onCantidadPlatosCategoria(int cantidad);
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
                for(DataSnapshot categoria : snapshot.getChildren()){
                    for(DataSnapshot plato : categoria.getChildren()){
                        plates.add(new Plate(plato.getKey(), categoria.getKey()));
                    }
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
                for(DataSnapshot categoria : snapshot.getChildren()){
                    for(DataSnapshot plato : categoria.getChildren()){
                        plates.add(new Plate(plato.getKey(), categoria.getKey()));
                    }
                }
                user.setPlates(plates);

                if (listener != null) {
                    listener.onDataLoaded();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public int getCountCategories(){

        DatabaseReference dbr = database.getReference("MenuMaker").child(user.getUserId()).child("categorias");
        final int[] countCat = {0};
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot categoria : snapshot.getChildren()){
                    countCat[0]++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return countCat[0];
    }

    public int getCountCategories(String cat){

        DatabaseReference dbr = database.getReference("MenuMaker").child(user.getUserId()).child("categorias").child(cat);
        final int[] countCat = {0};
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot categoria : snapshot.getChildren()){
                    countCat[0]++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return countCat[0];
    }

    public List<String> getNameCategories(OnDataLoadedListener listener){
        DatabaseReference dbr = database.getReference("MenuMaker").child(user.getUserId()).child("categorias");
        List<String> cat = new ArrayList<>();
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot categoria : snapshot.getChildren()){
                    cat.add(categoria.getKey());
                }
                if(listener != null){
                    listener.onDataLoaded();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return cat;
    }

    public boolean agregarPlato(String plate, String cat, DatabaseReference categoriaRef, Context context){
        boolean[] agregado = new boolean[1];

        if (cat.isEmpty() || plate.isEmpty()) {
            Toast.makeText(context, "Ingrese una categoría y un plato válidos", Toast.LENGTH_SHORT).show();
            return false;
        }

        categoriaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child(plate).exists()) {
                        Toast.makeText(context, "El plato ya existe en esta categoría", Toast.LENGTH_SHORT).show();
                    } else {
                        categoriaRef.child(plate).setValue("");
                        Toast.makeText(context, "Plato subido correctamente", Toast.LENGTH_SHORT).show();
                        agregado[0] = true;
                    }
                } else {
                    Toast.makeText(context, "La categoría no existe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return agregado[0];
    }

    public void getCantidadPlatosCategoria(String categoria, OnCantidadPlatosCategoriaListener listener){
        DatabaseReference dbr = database.getReference("MenuMaker").child(user.getUserId()).child("categorias").child(categoria);

        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            int cantidad = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    cantidad++;
                }

                listener.onCantidadPlatosCategoria(cantidad);
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
