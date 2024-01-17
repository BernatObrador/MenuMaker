package com.example.firebaseprueba;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecyclerViewAdapterPlate extends RecyclerView.Adapter<RecyclerViewAdapterPlate.MyHolder> {
    private final Context context;
    private List<Plate> plates;
    private ConectionBD conectionBD;

    public RecyclerViewAdapterPlate(Context context, List<Plate> plates, ConectionBD conectionBD) {
        this.context = context;
        this.plates = plates;
        this.conectionBD = conectionBD;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterPlate.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_plates, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterPlate.MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.plato.setText(plates.get(position).getName());
        holder.categoria.setText(plates.get(position).getCategory());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return onLongClickDelete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plates.size();
    }

    private boolean onLongClickDelete(int pos){
        Plate plate = plates.get(pos);

        FirebaseDatabase database = ConectionBD.getDatabase();
        DatabaseReference ref = database.getReference("MenuMaker")
                .child(conectionBD.getUser().getUserId())
                .child("categorias")
                .child(plate.getCategory())
                .child(plate.getName());

        ref.removeValue();
        plates.remove(pos);
        notifyItemRemoved(pos);
        Toast.makeText(context, "Plato eliminado correctamente", Toast.LENGTH_SHORT).show();
        return true;
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
