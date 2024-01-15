package com.example.firebaseprueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class logged extends AppCompatActivity {

    private User actualUser;
    private ConectionBD conectionBD;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private String userId;
    private DatabaseReference ref;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);

        startComponenets();
        menu();


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
        updateUser();

        addButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlate();
            }
        });



    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.sign_in_with_google))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Failed at logging out", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void menu(){

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item ->{

            if(item.getItemId() == (R.id.logOut)){
                logOut();
                return true;
            }
            if(item.getItemId() == (R.id.mostrarPlatos)) {
                Intent intent = new Intent(this, Recipes.class);
                intent.putExtra("connection", conectionBD);
                startActivity(intent);
            }

            return true;
        });
    }

    public void addPlate() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View dialogView = layoutInflater.inflate(R.layout.upload_plate, null);

        builder.setView(dialogView).setTitle("Agregar plato")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText categoria = dialogView.findViewById(R.id.textCategoria);
                        EditText plato = dialogView.findViewById(R.id.textPlato);

                        String cat = String.valueOf(categoria.getText()).toLowerCase().trim();
                        String plate = String.valueOf(plato.getText()).trim();

                        if (cat.isEmpty() || plate.isEmpty()) {
                            Toast.makeText(logged.this, "Ingrese una categoría y un plato válidos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        DatabaseReference categoriaRef = ref.child(userId).child("categorias").child(cat);

                        categoriaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    if (snapshot.child(plate).exists()) {
                                        Toast.makeText(logged.this, "El plato ya existe en esta categoría", Toast.LENGTH_SHORT).show();
                                    } else {
                                        categoriaRef.child(plate).setValue("");
                                        Toast.makeText(logged.this, "Plato subido correctamente", Toast.LENGTH_SHORT).show();
                                        updateUser();
                                    }
                                } else {
                                    Toast.makeText(logged.this, "La categoría no existe", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(logged.this, "Fallo en la subida del plato", Toast.LENGTH_SHORT).show();
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

    /*public void addPlate(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.upload_plate, null);

        builder.setView(dialogView).setTitle("Agregar plato")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText categoria = dialogView.findViewById(R.id.textCategoria);
                        EditText plato = dialogView.findViewById(R.id.textPlato);

                        String cat = String.valueOf(categoria.getText()).toLowerCase();
                        String plate = String.valueOf(plato.getText());

                        DatabaseReference dbr = ref.child(userId).child("categorias").child(cat);

                        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    dbr.child(plate).setValue("");
                                    Toast.makeText(logged.this, "Plato subido correctamente", Toast.LENGTH_SHORT).show();
                                    updateUser();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(logged.this, "Fallo en la subida del plato", Toast.LENGTH_SHORT).show();
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
    }*/

    private void updateUser(){
        conectionBD.getCategoriesFromDb();
        actualUser = conectionBD.getUser();
    }

    private void addUserToDb(){

        // El id del usuario sera su correo hasta el @, que si lo pongo entero da error por el . del correo
        userId = user.getEmail().substring(0, user.getEmail().indexOf("@"));

            ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
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

}