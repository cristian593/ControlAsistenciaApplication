package com.suarez.guambana.controlasistencia;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MateriasViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView nombreU, nombreM, nombreS;

    private ItemClickListener itemClickListener;

    public MateriasViewHolder(@NonNull View itemView){
        super(itemView);
        nombreU =itemView.findViewById(R.id.nombreunidadeducativa);
        nombreM =itemView.findViewById(R.id.materianombre);
        nombreS =itemView.findViewById(R.id.salonclases);

    }
    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(), false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
