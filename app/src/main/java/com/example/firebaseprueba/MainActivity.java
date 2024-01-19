package com.example.firebaseprueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
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

public class MainActivity extends AppCompatActivity {
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


    }

    private void startComponenets() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("MenuMaker");

        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        addUserToDb(new ConectionBD.OnDataLoadedListener() {
            @Override
            public void onDataLoaded() {
                conectionBD = new ConectionBD(database, userId);
                conectionBD.getCategoriesFromDb(() -> generateMenu = new GenerateMenu(conectionBD.getUser()));
                Utilities.setupMenu(MainActivity.this, toolbar, drawerLayout, navigationView, conectionBD);

                categories = conectionBD.getNameCategories(() -> {
                    ProgressBar progressBar = findViewById(R.id.loading);
                    progressBar.setVisibility(View.GONE);
                    setUpRecyclerView();
                });
            }
        });
    }

    public void generateMenu(View view) {
        if (conectionBD.getUser().getPlates().size() > 0) {
            TextView textMenu = findViewById(R.id.menu);
            List<Plate> menu = generateMenu.getMenuForCat(recyclerViewAdapter.getCantidadPorCategoria());

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
            Toast.makeText(MainActivity.this, "No tienes platos para crear el menu.", Toast.LENGTH_SHORT).show();
        }
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

    private void addUserToDb(ConectionBD.OnDataLoadedListener listener) {
        // El id del usuario sera su correo hasta el @, que si lo pongo entero da error por el . del correo
        userId = user.getEmail().substring(0, user.getEmail().indexOf("@"));

        ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    ref.child(userId).child("categorias").child("pasta").child("stopDelete").setValue("");
                    ref.child(userId).child("categorias").child("legumbres").child("stopDelete").setValue("");
                    ref.child(userId).child("categorias").child("carne").child("stopDelete").setValue("");
                    ref.child(userId).child("categorias").child("pescado").child("stopDelete").setValue("");
                    ref.child(userId).child("categorias").child("arroz").child("stopDelete").setValue("");
                    ref.child(userId).child("categorias").child("stopDelete").setValue("");
                }

                listener.onDataLoaded();
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