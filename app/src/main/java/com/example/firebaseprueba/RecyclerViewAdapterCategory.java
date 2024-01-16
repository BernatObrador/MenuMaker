package com.example.firebaseprueba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapterCategory extends RecyclerView.Adapter<RecyclerViewAdapterCategory.MyHolder> {
    private Context context;
    private List<String> categories;

    public RecyclerViewAdapterCategory(Context context, List<String> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterCategory.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_categories, parent, false);

        return new RecyclerViewAdapterCategory.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterCategory.MyHolder holder, int position) {
        holder.nombreCat.setText(categories.get(position));

        holder.arrowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qunatity = sumQuantity(Integer.parseInt(holder.cantidadCat.getText().toString()));
                holder.cantidadCat.setText(String.valueOf(qunatity));
            }
        });

        holder.arrowDwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qunatity = downQuantity(Integer.parseInt(holder.cantidadCat.getText().toString()));
                holder.cantidadCat.setText(String.valueOf(qunatity));
            }
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

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        private TextView nombreCat;
        private ImageView arrowUp;
        private ImageView arrowDwn;
        private TextView cantidadCat;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            nombreCat = itemView.findViewById(R.id.textCategory);
            arrowUp = itemView.findViewById(R.id.arrowUp);
            arrowDwn = itemView.findViewById(R.id.arrowDwn);
            cantidadCat = itemView.findViewById(R.id.cantidadCategoria);
        }
    }
}
