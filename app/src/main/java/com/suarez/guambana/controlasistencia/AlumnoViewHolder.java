package com.suarez.guambana.controlasistencia;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlumnoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView   apellidoalumn, dosnombresalum, codigoalumn;
    public Button btnPresente;
    private ItemClickListener itemClickListener;

    public AlumnoViewHolder(@NonNull View itemView){
        super(itemView);
        codigoalumn = itemView.findViewById(R.id.alumnocodigo);
        dosnombresalum = itemView.findViewById(R.id.dosnombres);
        btnPresente = itemView.findViewById(R.id.alumnopresente);
        apellidoalumn = itemView.findViewById(R.id.apellidoalumno);

    }


    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(), false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
