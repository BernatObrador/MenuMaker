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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class logged extends AppCompatActivity {
    private ConectionBD conectionBD;
    private GenerateMenu generateMenu;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private String userId;
    private DatabaseReference ref;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private List<String> categories;
    private RecyclerViewAdapterCategory recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);

        startComponenets();
        Utilities.setupMenu(this, toolbar, drawerLayout, navigationView, conectionBD);

    }

    private void startComponenets() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("MenuMaker");


        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        addUserToDb();


        ImageView addButon = findViewById(R.id.addButton);
        conectionBD = new ConectionBD(database, userId);

        conectionBD.getCategoriesFromDb(new ConectionBD.OnDataLoadedListener() {
            @Override
            public void onDataLoaded() {
                generateMenu = new GenerateMenu(conectionBD.getUser(), conectionBD);
            }
        });

        addButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlate();
            }
        });

        categories = conectionBD.getNameCategories(new ConectionBD.OnDataLoadedListener() {
            @Override
            public void onDataLoaded() {
                setUpRecyclerView();
                ProgressBar progressBar = findViewById(R.id.loading);
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    public void generateMenu(View view) {
        if (conectionBD.getUser().getPlates().size() > 0) {
            TextView textMenu = findViewById(R.id.menu);
            List<Plate> menu = generateMenu.getMenuForCat(recyclerViewAdapter.getCantidadCat());

            Collections.shuffle(menu);
            String menuTxt = "";
            for (int i = 0; i < menu.size(); i++) {
                switch (i) {
                    case 0:
                        menuTxt += "Lunes : " + menu.get(i).getName() + "\n";
                        break;
                    case 1:
                        menuTxt += "Martes : " + menu.get(i).getName() + "\n";
                        break;
                    case 2:
                        menuTxt += "Miércoles : " + menu.get(i).getName() + "\n";
                        break;
                    case 3:
                        menuTxt += "Jueves : " + menu.get(i).getName() + "\n";
                        break;
                    case 4:
                        menuTxt += "Viernes : " + menu.get(i).getName() + "\n";
                        break;

                }
            }

            textMenu.setText(menuTxt);

        } else {
            Toast.makeText(logged.this, "No tienes platos para crear el menu.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addPlate() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View dialogView = layoutInflater.inflate(R.layout.upload_plate, null);
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

                        DatabaseReference categoriaRef = ref.child(userId).child("categorias").child(cat);

                        if(conectionBD.agregarPlato(plate, cat, categoriaRef, logged.this)){
                            updateUser();
                        }
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

    private void updateUser() {
        conectionBD.getCategoriesFromDb();
    }

    public void setupSpinner(List<String> categories, Spinner spinner) {
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterCategories);
    }

    private void addUserToDb() {

        // El id del usuario sera su correo hasta el @, que si lo pongo entero da error por el . del correo
        userId = user.getEmail().substring(0, user.getEmail().indexOf("@"));

        ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    ref.child(userId).child("categorias").child("pasta").setValue("");
                    ref.child(userId).child("categorias").child("legumbres").setValue("");
                    ref.child(userId).child("categorias").child("carne").setValue("");
                    ref.child(userId).child("categorias").child("pescado").setValue("");
                    ref.child(userId).child("categorias").child("arroz").setValue("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerCategories);
        recyclerViewAdapter = new RecyclerViewAdapterCategory(this, categories, conectionBD);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}