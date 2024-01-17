package com.example.firebaseprueba;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapterCategory extends RecyclerView.Adapter<RecyclerViewAdapterCategory.MyHolder> {
    private final Context context;
    private final List<String> categories;
    private final HashMap<String, Integer> cantidadCat;
    private ConectionBD conectionBD;

    public RecyclerViewAdapterCategory(Context context, List<String> categories, ConectionBD conectionBD) {
        this.context = context;
        this.categories = categories;
        this.cantidadCat = new HashMap<>();
        this.conectionBD = conectionBD;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterCategory.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_categories, parent, false);

        return new RecyclerViewAdapterCategory.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterCategory.MyHolder holder, @SuppressLint("RecyclerView") int position) {
        String categoria = categories.get(position);
        holder.nombreCat.setText(categoria);
        final int[] cantidadDePlatosCategoria = {0};

        conectionBD.getCantidadPlatosCategoria(categoria, new ConectionBD.OnCantidadPlatosCategoriaListener() {
            @Override
            public void onCantidadPlatosCategoria(int cantidad) {
                cantidadDePlatosCategoria[0] = cantidad;
            }
        });

        holder.arrowUp.setOnClickListener(v -> {

            if (cantidadDePlatos() < 5 && Integer.parseInt((String) holder.cantidadCat.getText()) < cantidadDePlatosCategoria[0]) {
                int quantity = sumQuantity(Integer.parseInt(holder.cantidadCat.getText().toString()));
                holder.cantidadCat.setText(String.valueOf(quantity));
                cantidadCat.put(categoria, Integer.parseInt(holder.cantidadCat.getText().toString()));
            }

        });

        holder.arrowDwn.setOnClickListener(v -> {
            int qunatity = downQuantity(Integer.parseInt(holder.cantidadCat.getText().toString()));
            holder.cantidadCat.setText(String.valueOf(qunatity));
            cantidadCat.put(categoria, Integer.parseInt(holder.cantidadCat.getText().toString()));
        });
    }

    private int downQuantity(int numero){

        if (numero > 0){
            numero--;
        }

        return numero;
    }

    private int sumQuantity(int numero){

        if (numero < 5){
            numero++;
        }

        return numero;
    }

    public int cantidadDePlatos(){
        int count = 0;

        for(Map.Entry<String, Integer> entry : cantidadCat.entrySet()) {
            count += entry.getValue();
        }

        return count;
    }

    public HashMap<String, Integer> getCantidadCat() {
        return cantidadCat;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        private final TextView nombreCat;
        private final ImageView arrowUp;
        private final ImageView arrowDwn;
        private final TextView cantidadCat;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            nombreCat = itemView.findViewById(R.id.textCategory);
            arrowUp = itemView.findViewById(R.id.arrowUp);
            arrowDwn = itemView.findViewById(R.id.arrowDwn);
            cantidadCat = itemView.findViewById(R.id.cantidadCategoria);
        }
    }
}
