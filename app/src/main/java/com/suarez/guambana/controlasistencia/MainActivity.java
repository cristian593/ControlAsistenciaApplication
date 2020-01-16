package com.suarez.guambana.controlasistencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.DataTruncation;

public class MainActivity extends AppCompatActivity {

    private EditText usuario, contrasena;
    private Button login;
    private TextView cuentanueva;
    private String dusuario, dcontrasena, contra;
    private DatabaseReference docenteReferencia;

    public static String usuarioDocente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Control Asistencia");

        usuario = findViewById(R.id.usuario);
        contrasena = findViewById(R.id.contrasena);
        login = findViewById(R.id.loguear);
        cuentanueva = findViewById(R.id.cuentanueva);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dusuario = usuario.getText().toString();
                dcontrasena = contrasena.getText().toString();
                if(TextUtils.isEmpty(dusuario)){
                    Toast.makeText(MainActivity.this, "Ingrese un Usuario", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(dcontrasena)){
                    Toast.makeText(MainActivity.this, "Ingrese la Contraseña", Toast.LENGTH_SHORT).show();
                }else{
                    ValidarUsuario(dusuario, dcontrasena);
                }
            }
        });

        cuentanueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DatosDocenteActivity.class);
                startActivity(intent);

            }
        });

    }

    private void ValidarUsuario(final String dusuario, final String dcontrasena) {
        docenteReferencia = FirebaseDatabase.getInstance().getReference().child("Docente").child(dusuario);
        docenteReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    contra = dataSnapshot.child("Contrasena").getValue().toString();

                    if(dcontrasena.equals(contra)){
                        usuarioDocente = dusuario;
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else {
                        Toast.makeText(MainActivity.this, "Contraseña Incorrecta", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
