package com.example.firebaseprueba;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapterCategory extends RecyclerView.Adapter<RecyclerViewAdapterCategory.MyHolder> {
    private final Context context;
    private final List<String> categories;
    private final HashMap<String, Integer> cantidadPorCategoria;
    private ConectionBD conectionBD;

    public RecyclerViewAdapterCategory(Context context, List<String> categories, ConectionBD conectionBD) {
        this.context = context;
        this.categories = categories;
        this.cantidadPorCategoria = new HashMap<>();
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
                cantidadPorCategoria.put(categoria, Integer.parseInt(holder.cantidadCat.getText().toString()));
            }
        });

        holder.arrowDwn.setOnClickListener(v -> {
            int qunatity = downQuantity(Integer.parseInt(holder.cantidadCat.getText().toString()));
            holder.cantidadCat.setText(String.valueOf(qunatity));
            cantidadPorCategoria.put(categoria, Integer.parseInt(holder.cantidadCat.getText().toString()));
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                onClickDelete(position, holder);
            }
        });
    }

    private void onClickDelete(int position, MyHolder holder){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Estas seguro que quieres eliminar la categoria " + categories.get(position) + " y todos sus platos?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    DatabaseReference ref = ConectionBD.getDatabase()
                            .getReference("MenuMaker")
                            .child(conectionBD.getUser().getUserId())
                            .child("categorias")
                            .child(categories.get(position));

                    holder.cantidadCat.setText("0");
                    ref.removeValue();
                    categories.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                }).setNegativeButton("Cancelar", (dialog, which) -> {

                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

        for(Map.Entry<String, Integer> entry : cantidadPorCategoria.entrySet()) {
            count += entry.getValue();
        }

        return count;
    }

    public HashMap<String, Integer> getCantidadPorCategoria() {
        return cantidadPorCategoria;
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
        private final ImageView deleteBtn;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            nombreCat = itemView.findViewById(R.id.textCategory);
            arrowUp = itemView.findViewById(R.id.arrowUp);
            arrowDwn = itemView.findViewById(R.id.arrowDwn);
            cantidadCat = itemView.findViewById(R.id.cantidadCategoria);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
