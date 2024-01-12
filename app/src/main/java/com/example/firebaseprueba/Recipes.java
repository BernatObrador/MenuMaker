package com.example.firebaseprueba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Recipes extends AppCompatActivity {
    private ConectionBD conectionBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        conectionBD = (ConectionBD) getIntent().getExtras().get("connection");

        Log.d("recipes", conectionBD.getUser().getPlates().toString());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapterPlate recyclerViewAdapter = new RecyclerViewAdapterPlate(this, conectionBD.getUser().getPlates());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d("recipes", conectionBD.getUser().getPlates().toString());
    }
}