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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Recipes extends AppCompatActivity {
    private ConectionBD conectionBD;
    private List<Plate> plates;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private RecyclerViewAdapterPlate recyclerViewAdapter;
    private List<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);


        conectionBD = (ConectionBD) getIntent().getExtras().get("connection");
        database = ConectionBD.getDatabase();
        ref = database.getReference("MenuMaker");

        conectionBD.getCategoriesFromDb(new ConectionBD.OnDataLoadedListener() {
            @Override
            public void onDataLoaded() {
                startComponents();
                plates = conectionBD.getUser().getPlates();
                updateUi();
            }
        });
    }

    private void startComponents(){

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.navView);
        Utilities.setupMenu(this, toolbar, drawerLayout, navigationView, conectionBD);
        FloatingActionButton addButon = findViewById(R.id.addButton);
        addButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlate();
            }
        });


    }

    private void updateUi(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapterPlate(this, plates, conectionBD);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addPlate() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View dialogView = layoutInflater.inflate(R.layout.upload_plate, null);
        String userId = conectionBD.getUser().getUserId();

        List<String> getCategoryFromSpinner = new ArrayList<>();

        Spinner spinner = dialogView.findViewById(R.id.spinner);
        categories = conectionBD.getNameCategories(new ConectionBD.OnDataLoadedListener() {
            @Override
            public void onDataLoaded() {
                setupSpinner(categories, spinner);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getCategoryFromSpinner.add(0, (String) parent.getItemAtPosition(position));
                Log.d("cat", getCategoryFromSpinner.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(dialogView).setTitle("Agregar plato")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText plato = dialogView.findViewById(R.id.textPlato);

                        String cat = getCategoryFromSpinner.get(0);
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
                                        plates.add(new Plate(plate, cat));
                                        Toast.makeText(Recipes.this, "Plato subido correctamente", Toast.LENGTH_SHORT).show();
                                        recyclerViewAdapter.notifyItemInserted(plates.size() - 1);
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

    public void setupSpinner(List<String> categories, Spinner spinner){
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterCategories);
    }
}