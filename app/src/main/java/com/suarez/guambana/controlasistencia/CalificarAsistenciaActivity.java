package com.suarez.guambana.controlasistencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class CalificarAsistenciaActivity extends AppCompatActivity {

    private String materiaR;
    private String usuarioD;
    private String fecha;
    private Boolean presente;

    DatabaseReference asistenciaReference;

    private RecyclerView recyclerViewAsistencia;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar_asistencia);

        Bundle datoRecivido = this.getIntent().getExtras();
        materiaR = datoRecivido.getString("materia");
        getSupportActionBar().setTitle("Control Asistencia");
        usuarioD = MainActivity.usuarioDocente;

        recyclerViewAsistencia = findViewById(R.id.recyclerAsistencia);
        layoutManager = new LinearLayoutManager(CalificarAsistenciaActivity.this);
        recyclerViewAsistencia.setLayoutManager(layoutManager);


        asistenciaReference = FirebaseDatabase.getInstance().getReference().child(usuarioD).child("Materias").child(materiaR).child("Alumnos");

    }

   @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Alumno> options =
                new FirebaseRecyclerOptions.Builder<Alumno>()
                        .setQuery(asistenciaReference, Alumno.class).build();

        FirebaseRecyclerAdapter<Alumno, AlumnoViewHolder> adapter = new FirebaseRecyclerAdapter<Alumno, AlumnoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AlumnoViewHolder alumnoViewHolder, int position, @NonNull final Alumno alumno) {
                alumnoViewHolder.btnPresente.setText("Presente");
                alumnoViewHolder.apellidoalumn.setText(alumno.getApellido());
                alumnoViewHolder.codigoalumn.setText(alumno.getCodigo());
                alumnoViewHolder.dosnombresalum.setText(alumno.getNombre());

                alumnoViewHolder.btnPresente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(CalificarAsistenciaActivity.this,alumno.getNombre(),Toast.LENGTH_SHORT).show();
                        presente = false;
                        ColocarAsistencia(alumno.getCodigo(), alumno.getNombre(), alumno.getApellido());

                            alumnoViewHolder.btnPresente.setText(":)");
                            alumnoViewHolder.btnPresente.setEnabled(false);


                    }
                });
            }

            @NonNull
            @Override
            public AlumnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alumnos_items_view, parent, false);
                AlumnoViewHolder alumnoViewHolder = new AlumnoViewHolder(v);
                return alumnoViewHolder;
            }
        };
        recyclerViewAsistencia.setAdapter(adapter);
        adapter.startListening();
    }


    private void ColocarAsistencia(final String codigo, final String nombre, final String apellido ) {

        long date = System.currentTimeMillis();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
        fecha = formatoFecha.format(date);

        final DatabaseReference asistenciaReference;
        asistenciaReference = FirebaseDatabase.getInstance().getReference();
        asistenciaReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child(usuarioD).child("Materias").child(materiaR).child("Asistencia").child(fecha).child(codigo).exists())) {
                    HashMap<String, Object> asistenciaMap = new HashMap<>();
                    asistenciaMap.put("nombre", nombre);
                    asistenciaMap.put("apellido", apellido);
                    asistenciaMap.put("codigo", codigo);

                    asistenciaReference.child(usuarioD).child("Materias").child(materiaR).child("Asistencia")
                            .child(fecha).child(codigo).setValue(asistenciaMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                presente = true;
                            } else {
                                Toast.makeText(CalificarAsistenciaActivity.this, "No se pudo guardar la asistencia", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }else {
                    Toast.makeText(CalificarAsistenciaActivity.this, " La Asistencia ya ha sido calificada anteriormente", Toast.LENGTH_SHORT).show();
                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
