package com.suarez.guambana.controlasistencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class AgregarMateriaActivity extends AppCompatActivity {

    private EditText unidadEducativa, nombreMateria, salonClases;
    private Button crearMateria;
    private  String unidadE, nombreM, salonC;
    private String udocente;
    private String IDmateria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_materia);

        getSupportActionBar().setTitle("Datos de la Materia");


        unidadEducativa = findViewById(R.id.unidadeducativa);
        nombreMateria = findViewById(R.id.nombremateria);
        salonClases = findViewById(R.id.salon);
        crearMateria = findViewById(R.id.guardarmateria);
        udocente = MainActivity.usuarioDocente;

        crearMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unidadE =  unidadEducativa.getText().toString();
                nombreM = nombreMateria.getText().toString();
                salonC = salonClases.getText().toString();

                if(unidadE.isEmpty()){
                    Toast.makeText(AgregarMateriaActivity.this, "Por favor Ingresa la Unidad Educativa", Toast.LENGTH_SHORT);
                }else if(nombreM.isEmpty()){
                    Toast.makeText(AgregarMateriaActivity.this, "Por favor Ingresa el nombre de la Materia", Toast.LENGTH_SHORT);
                }else if(salonC.isEmpty()){
                    Toast.makeText(AgregarMateriaActivity.this, "Por favor Ingresa el nombre del Salon de clases", Toast.LENGTH_SHORT);
                }else{
                    CrearNuevaMateria(unidadE, nombreM, salonC);
                }

            }
        });
    }

    private void CrearNuevaMateria(final String unidadE, final String nombreM, final String salonC) {

        IDmateria = unidadE+"-"+nombreM+"-"+salonC;

        final DatabaseReference materiaReference;
        materiaReference = FirebaseDatabase.getInstance().getReference();
        materiaReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child(udocente).child("Materias").child(IDmateria).exists())){
                    HashMap<String, Object> materiaMap = new HashMap<>();
                    materiaMap.put("nombreInstitucion",unidadE);
                    materiaMap.put("nombreMateria",nombreM);
                    materiaMap.put("nombreSalon",salonC);

                    materiaReference.child(udocente).child("Materias").child(nombreM).setValue(materiaMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AgregarMateriaActivity.this, "Materia Creada Existosamente", Toast.LENGTH_SHORT).show();
                                Intent intent  = new Intent(AgregarMateriaActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }   else {
                                Toast.makeText(AgregarMateriaActivity.this, "Error al crear la materia, compruebe la coneccion a internet", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(AgregarMateriaActivity.this, "La materia con esos datos ya existe, compruebe la informacion", Toast.LENGTH_SHORT).show();
                    unidadEducativa.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
