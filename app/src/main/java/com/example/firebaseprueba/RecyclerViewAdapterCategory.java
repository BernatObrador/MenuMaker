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

    public RecyclerViewAdapterCategory(Context context, List<String> categories) {
        this.context = context;
        this.categories = categories;
        this.cantidadCat = new HashMap<>();
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
        holder.nombreCat.setText(categories.get(position));

        holder.arrowUp.setOnClickListener(v -> {
            if (cantidadDePlatos() < 5) {
                int quantity = sumQuantity(Integer.parseInt(holder.cantidadCat.getText().toString()));
                holder.cantidadCat.setText(String.valueOf(quantity));
                cantidadCat.put(categories.get(position), Integer.parseInt(holder.cantidadCat.getText().toString()));
            } else {
                Toast.makeText(context, "No puedes poner mas de 5 platos.", Toast.LENGTH_SHORT).show();
            }
        });

        holder.arrowDwn.setOnClickListener(v -> {
            int qunatity = downQuantity(Integer.parseInt(holder.cantidadCat.getText().toString()));
            holder.cantidadCat.setText(String.valueOf(qunatity));
            cantidadCat.put(categories.get(position), Integer.parseInt(holder.cantidadCat.getText().toString()));
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
