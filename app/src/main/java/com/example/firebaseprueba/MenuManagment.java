package com.example.firebaseprueba;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MenuManagment {
    private int cantidadCategorias;
    private FirebaseDatabase database;
    private String userId;

    public MenuManagment(FirebaseDatabase database, String userId) {
        this.database = database;
        this.userId = userId;
        countCategory();
    }

    private void countCategory(){
        DatabaseReference dbr = database.getReference("MenuMaker").child(userId).child("categorias");

        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            int count = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    count++;
                }
                Log.d("","Cantidad de categorias: " + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
