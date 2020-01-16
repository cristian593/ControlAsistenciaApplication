package com.suarez.guambana.controlasistencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AgregarAlumnosActivity extends AppCompatActivity {
    private String materiaR;

    private EditText nombreA, apellidoA, codigoA;
    private Button crearAlumno;
    private  String alumnonombre, alumnoApellido, alumnocodigo;
    private String docenteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_alumnos);
        getSupportActionBar().setTitle("Datos del Alumno");


        Bundle datoRecivido = this.getIntent().getExtras();
        materiaR = datoRecivido.getString("materia");
        docenteID = MainActivity.usuarioDocente;

        nombreA = findViewById(R.id.nomalumno);
        apellidoA =findViewById(R.id.apealumno);
        codigoA = findViewById(R.id.codalumno);
        crearAlumno = findViewById(R.id.guardaralumno);
        crearAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alumnonombre =  nombreA.getText().toString();
                alumnoApellido = apellidoA.getText().toString();
                alumnocodigo = codigoA.getText().toString();

                if(alumnonombre.isEmpty()){
                    Toast.makeText(AgregarAlumnosActivity.this, "Por favor Ingresa la Unidad Educativa", Toast.LENGTH_SHORT);
                }else if(alumnoApellido.isEmpty()){
                    Toast.makeText(AgregarAlumnosActivity.this, "Por favor Ingresa el nombre de la Materia", Toast.LENGTH_SHORT);
                }else if(alumnocodigo.isEmpty()){
                    Toast.makeText(AgregarAlumnosActivity.this, "Por favor Ingresa el nombre del Salon de clases", Toast.LENGTH_SHORT);
                }else{
                    CrearNuevoAlumno(alumnonombre, alumnoApellido, alumnocodigo);
                }
            }
        });
    }

    private void CrearNuevoAlumno(final String alumnonombre, final String alumnoApellido, final String alumnocodigo) {
        final DatabaseReference alumnoReference;
        alumnoReference = FirebaseDatabase.getInstance().getReference();
        alumnoReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child(docenteID).child("Materias").child(materiaR).child("Alumnos").child(alumnocodigo).exists())){
                    HashMap<String, Object> alumnoMap = new HashMap<>();
                    alumnoMap.put("nombre",alumnonombre);
                    alumnoMap.put("apellido",alumnoApellido);
                    alumnoMap.put("codigo",alumnocodigo);

                    alumnoReference.child(docenteID).child("Materias").child(materiaR).child("Alumnos").child(alumnocodigo).setValue(alumnoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AgregarAlumnosActivity.this, "Alumno Creado Correctamente", Toast.LENGTH_SHORT);
                                nombreA.setText("");
                                apellidoA.setText("");
                                codigoA.setText("");
                                nombreA.requestFocus();
                            }else {
                                Toast.makeText(AgregarAlumnosActivity.this, "Error al crear el alumno, compruebe la coneccion a internet", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(AgregarAlumnosActivity.this, "La el Alumno con ese codigo ya existe, compruebe la informacion", Toast.LENGTH_SHORT).show();
                    codigoA.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
