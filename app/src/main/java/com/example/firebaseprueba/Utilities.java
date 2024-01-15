package com.example.firebaseprueba;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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

import java.sql.Connection;

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
                        Intent intent = new Intent(context, MainActivity.class);
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
                Intent intent = new Intent(activity, logged.class);
                activity.startActivity(intent);
                return true;
            }
            if (item.getItemId() == (R.id.logOut)) {
                logOut(activity);
                return true;
            }
            if (item.getItemId() == (R.id.mostrarPlatos)) {
                Intent intent = new Intent(activity, Recipes.class);
                intent.putExtra("connection", conectionBD);
                activity.startActivity(intent);
                return true;
            }
            return true;
        });
    }

    public static void showAddPlateDialog(Context context, DatabaseReference ref, String userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
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
                            Toast.makeText(context, "Ingrese una categoría y un plato válidos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        DatabaseReference categoriaRef = ref.child(userId).child("categorias").child(cat);

                        categoriaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    if (snapshot.child(plate).exists()) {
                                        Toast.makeText(context, "El plato ya existe en esta categoría", Toast.LENGTH_SHORT).show();
                                    } else {
                                        categoriaRef.child(plate).setValue("");
                                        Toast.makeText(context, "Plato subido correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, "La categoría no existe", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(context, "Fallo en la subida del plato", Toast.LENGTH_SHORT).show();
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

    private static User updateUser(ConectionBD connectionBD){
        connectionBD.getCategoriesFromDb();
        return connectionBD.getUser();
    }
}

