package com.example.firebaseprueba;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterPlate extends RecyclerView.Adapter<RecyclerViewAdapterPlate.MyHolder> {
    private Context context;
    private List<Plate> plates;

    public RecyclerViewAdapterPlate(Context context, List<Plate> plates) {
        this.context = context;
        this.plates = plates;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterPlate.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_plates, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterPlate.MyHolder holder, int position) {
        holder.plato.setText(plates.get(position).getName());
        holder.categoria.setText(plates.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return plates.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        private final TextView plato;
        private final TextView categoria;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            plato = itemView.findViewById(R.id.nombrePlato);
            categoria = itemView.findViewById(R.id.nombreCat);
        }
    }
}
