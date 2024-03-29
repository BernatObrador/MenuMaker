package com.example.firebaseprueba;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDelete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plates.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onClickDelete(int pos){
        Plate plate = plates.get(pos);

        FirebaseDatabase database = ConectionBD.getDatabase();
        database.getReference("MenuMaker")
                .child(conectionBD.getUser().getUserId())
                .child("categorias")
                .child(plate.getCategory())
                .child(plate.getName()).removeValue();

        plates.remove(plate);
        notifyItemRemoved(pos);
        notifyDataSetChanged();
        Toast.makeText(context, "Plato eliminado correctamente", Toast.LENGTH_SHORT).show();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        private final TextView plato;
        private final TextView categoria;
        private final ImageView deleteBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            plato = itemView.findViewById(R.id.nombrePlato);
            categoria = itemView.findViewById(R.id.nombreCat);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
