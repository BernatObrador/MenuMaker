package com.example.firebaseprueba;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConectionBD {
    private FirebaseDatabase database;
    private User user;

    public ConectionBD(FirebaseDatabase database, String userId) {
        this.database = database;
        user = new User();
        user.setUserId(userId);
        getCategoriesFromDb();
    }

    public void getCategoriesFromDb(){
        DatabaseReference dbr = database.getReference("MenuMaker").child(user.getUserId()).child("categorias");

        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Category category = null;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    category = new Category(dataSnapshot.getKey());
                    for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                        category.addPlate(snapshot1.getKey());
                    }
                    user.addCategory(category);
                }

                Log.d("Categ", user.getCategories().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public User getUser() {
        return user;
    }
}
