package com.example.firebaseprueba;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Utilities {

    private static void logOut(Context context) {
        FirebaseAuth.getInstance().signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.sign_in_with_google))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, LogIn.class);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Failed at logging out", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void setupMenu(Activity activity, Toolbar toolbar, DrawerLayout drawerLayout, NavigationView navigationView, ConectionBD conectionBD) {
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == (R.id.pagPrincipal)){
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                return true;
            }
            if (item.getItemId() == (R.id.logOut)) {
                logOut(activity);
                return true;
            }
            if (item.getItemId() == (R.id.mostrarPlatos)) {
                Intent intent = new Intent(activity, TusPlatos.class);
                intent.putExtra("connection", conectionBD);
                activity.startActivity(intent);
                return true;
            }
            return true;
        });
    }
/*
        public void addPlate() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View dialogView = layoutInflater.inflate(R.layout.upload_plate, null);
        List<String> getCategoryFromSpinner = new ArrayList<>();

        Spinner spinner = dialogView.findViewById(R.id.spinner);

        categories = conectionBD.getNameCategories(() -> setupSpinner(categories, spinner));

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
                .setPositiveButton("Guardar", (dialog, which) -> {

                    EditText plato = dialogView.findViewById(R.id.textPlato);
                    String cat = getCategoryFromSpinner.get(0);
                    String plate = String.valueOf(plato.getText()).trim();

                    DatabaseReference categoriaRef = ref.child(userId).child("categorias").child(cat);

                    if(conectionBD.agregarPlato(plate, cat, categoriaRef, MainActivity.this)){
                        updateUser();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {

                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

 */
}

