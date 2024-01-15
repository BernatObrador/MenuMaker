package com.example.firebaseprueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Recipes extends AppCompatActivity {
    private ConectionBD conectionBD;
    private User actualUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);




        conectionBD = (ConectionBD) getIntent().getExtras().get("connection");

        conectionBD.getCategoriesFromDb(new ConectionBD.OnDataLoadedListener() {
            @Override
            public void onDataLoaded() {
                startComponents();
                updateUi();
            }
        });
    }

    private void startComponents(){

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.navView);
        Utilities.setupMenu(this, toolbar, drawerLayout, navigationView, conectionBD);
        addPlate();

    }

    private void updateUi(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapterPlate recyclerViewAdapter = new RecyclerViewAdapterPlate(this, conectionBD.getUser().getPlates());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addPlate() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View dialogView = layoutInflater.inflate(R.layout.upload_plate, null);
        DatabaseReference ref = ConectionBD.getDatabase().getReference();
        String userId = conectionBD.getUser().getUserId();

        builder.setView(dialogView).setTitle("Agregar plato")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText categoria = dialogView.findViewById(R.id.textCategoria);
                        EditText plato = dialogView.findViewById(R.id.textPlato);

                        String cat = String.valueOf(categoria.getText()).toLowerCase().trim();
                        String plate = String.valueOf(plato.getText()).trim();

                        if (cat.isEmpty() || plate.isEmpty()) {
                            Toast.makeText(Recipes.this, "Ingrese una categoría y un plato válidos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        DatabaseReference categoriaRef = ref.child(userId).child("categorias").child(cat);

                        categoriaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    if (snapshot.child(plate).exists()) {
                                        Toast.makeText(Recipes.this, "El plato ya existe en esta categoría", Toast.LENGTH_SHORT).show();
                                    } else {
                                        categoriaRef.child(plate).setValue("");
                                        Toast.makeText(Recipes.this, "Plato subido correctamente", Toast.LENGTH_SHORT).show();
                                        updateUser();
                                    }
                                } else {
                                    Toast.makeText(Recipes.this, "La categoría no existe", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Recipes.this, "Fallo en la subida del plato", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateUser(){
        conectionBD.getCategoriesFromDb();
    }
}