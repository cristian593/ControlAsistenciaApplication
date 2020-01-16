package com.suarez.guambana.controlasistencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DatosDocenteActivity extends AppCompatActivity {

    private EditText nombre, usuario, contrasena,celular, correo;
    private Button crear;
    private String dnombre, dusuario, dcontrasena, dcelular, dcorreo;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_docente);

        nombre = findViewById(R.id.docentenombre);
        usuario = findViewById(R.id.docenteusuario);
        contrasena = findViewById(R.id.docentecontrasena);
        celular = findViewById(R.id.docentetelefono);
        correo = findViewById(R.id.docentecorreo);
        crear = findViewById(R.id.crearcuenta);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarDatos();

            }
        });

    }

    private void ValidarDatos() {
        dnombre = nombre.getText().toString();
        dusuario = usuario.getText().toString();
        dcontrasena = contrasena.getText().toString();
        dcelular = celular.getText().toString();
        dcorreo = correo.getText().toString();

        if(TextUtils.isEmpty(dnombre)){
            Toast.makeText(DatosDocenteActivity.this, "Falta el Nombre", Toast.LENGTH_SHORT).show();
            nombre.requestFocus();
        }else if(TextUtils.isEmpty(dusuario)) {
            Toast.makeText(DatosDocenteActivity.this, "Falta el Usuario", Toast.LENGTH_SHORT).show();
            usuario.requestFocus();
        }else if(TextUtils.isEmpty(dcontrasena)) {
            Toast.makeText(DatosDocenteActivity.this, "Falta la Contraseña", Toast.LENGTH_SHORT).show();
            contrasena.requestFocus();
        }else if(TextUtils.isEmpty(dcelular)) {
            Toast.makeText(DatosDocenteActivity.this, "Falta el Numero de Celular", Toast.LENGTH_SHORT).show();
            celular.requestFocus();
        }else if(TextUtils.isEmpty(dcorreo)) {
            Toast.makeText(DatosDocenteActivity.this, "Falta el Correo electronico", Toast.LENGTH_SHORT).show();
            correo.requestFocus();
        }else {
            CrearDocente(dnombre, dusuario, dcontrasena, dcelular, dcorreo);
        }
    }

    private void CrearDocente(final String dnombre, final String dusuario, final String dcontrasena, final String dcelular, final String dcorreo) {

        final DatabaseReference docenteReference;
        docenteReference = FirebaseDatabase.getInstance().getReference();
        docenteReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Docente").child(dusuario)).exists()){
                    //Usuario Nuevo
                    HashMap<String, Object> docente = new HashMap<>();
                    docente.put("Nombre", dnombre);
                    docente.put("Usuario", dusuario);
                    docente.put("Contrasena", dcontrasena);
                    docente.put("Celular", dcelular);
                    docente.put("Correo", dcorreo);

                    docenteReference.child("Docente").child(dusuario).setValue(docente).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(DatosDocenteActivity.this, "Docente Creado", Toast.LENGTH_SHORT).show();
                                startActivity(intent = new Intent(DatosDocenteActivity.this, MainActivity.class));
                            }else {
                                Toast.makeText(DatosDocenteActivity.this, "No se pudo crear el docente :(", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }else {
                    //Actualizacion
                    HashMap<String, Object> docente = new HashMap<>();
                    docente.put("Nombre", dnombre);
                    docente.put("Usuario", dusuario);
                    docente.put("Contrasena", dcontrasena);
                    docente.put("Celular", dcelular);
                    docente.put("Correo", dcorreo);

                    docenteReference.child("Docente").child(dusuario).updateChildren(docente).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(DatosDocenteActivity.this, "Docente Actualizado", Toast.LENGTH_SHORT).show();
                                startActivity(intent = new Intent(DatosDocenteActivity.this, HomeActivity.class));
                            }else {
                                Toast.makeText(DatosDocenteActivity.this, "No se pudo crear el docente :(", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
