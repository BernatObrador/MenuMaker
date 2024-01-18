package com.example.firebaseprueba;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

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
}

