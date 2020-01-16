package com.suarez.guambana.controlasistencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    DatabaseReference materiaReference;

    private RecyclerView recyclerViewMaterias;
    private RecyclerView.LayoutManager layoutManager;
    //FirebaseRecyclerAdapter<Materia, MateriasViewHolder> adapter;

    private String usuarioD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("Mis Materias");
        usuarioD = MainActivity.usuarioDocente;

        recyclerViewMaterias = findViewById(R.id.recyclercursos);
        layoutManager = new LinearLayoutManager(HomeActivity.this);
        recyclerViewMaterias.setLayoutManager(layoutManager);

        materiaReference = FirebaseDatabase.getInstance().getReference().child(usuarioD).child("Materias");


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Materia> options =
                new FirebaseRecyclerOptions.Builder<Materia>()
                        .setQuery(materiaReference, Materia.class).build();

        FirebaseRecyclerAdapter<Materia, MateriasViewHolder> adapter = new FirebaseRecyclerAdapter<Materia, MateriasViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MateriasViewHolder materiasViewHolder, int i, @NonNull final Materia materia) {

                materiasViewHolder.nombreU.setText(materia.getNombreInstitucion());
                materiasViewHolder.nombreM.setText(materia.getNombreMateria());
                materiasViewHolder.nombreS.setText(materia.getNombreSalon());

                materiasViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String nomMateria = materia.getNombreMateria();
                        CharSequence options[]= new CharSequence[]{
                                "Calificar Asistencia",
                                "Agregar Alumnos"

                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                        builder.setTitle("Materia opciones: ");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                                if(i==0){

                                    Intent intent = new Intent(HomeActivity.this, CalificarAsistenciaActivity.class);
                                    intent.putExtra("materia", nomMateria);
                                    startActivity(intent);
                                }
                                if(i==1){

                                    Intent intent = new Intent(HomeActivity.this, AgregarAlumnosActivity.class);
                                    intent.putExtra("materia", nomMateria);
                                    startActivity(intent);
                                }
                            }
                        });
                    builder.show();
                    }
                });
                /*materiasViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String nomMateria = materia.getNombreMateria();
                        CharSequence options[]= new CharSequence[]{
                                "Calificar Asistencia",
                                "Agregar Alumnos"

                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                        builder.setTitle("Materia opciones: ");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                                if(i==0){

                                    Intent intent = new Intent(HomeActivity.this, AgregarAlumnosActivity.class);
                                    intent.putExtra("materia", nomMateria);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                });*/
            }

            @NonNull
            @Override
            public MateriasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.materias_items_view, parent, false);
                MateriasViewHolder vh =new MateriasViewHolder(v);
                return vh;
            }
        };
        recyclerViewMaterias.setAdapter(adapter);
        adapter.startListening();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu, menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nuevamateria:
                Intent intent = new Intent(HomeActivity.this, AgregarMateriaActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
