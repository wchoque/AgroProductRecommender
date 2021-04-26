package com.upc.appcentroidiomas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;



import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class ActivityCursos extends AppCompatActivity {

        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;

        EditText txtCurso, txtSeccion, txtHorario;
        Button btnRegistrarCurso, btnListarCursos;

        String curso, seccion, horario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);
        inicializarFirebase();
        asignarReferencias();
    }

    private void asignarReferencias(){
        txtCurso = findViewById(R.id.txtCurso);
       txtSeccion = findViewById(R.id.txtSeccion);
       txtHorario = findViewById(R.id.txtHorario);
      btnRegistrarCurso = findViewById(R.id.btnRegistrarCurso);
       btnRegistrarCurso.setOnClickListener(new View.OnClickListener() {
          @Override
           public void onClick(View v) {
                   registrar();
          }
       });
       btnListarCursos = findViewById(R.id.btnListarCursos);
        btnListarCursos.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
               Intent intent = new Intent(ActivityCursos.this,ListaCursosActivity.class);
               startActivity(intent);
           }
       });
    }

    private void registrar(){
        curso = txtCurso.getText().toString();
        seccion = txtSeccion.getText().toString();
        horario = txtHorario.getText().toString();
        if (curso.equals("") || seccion.equals("") || horario.equals("")){
            mostrarErrores();
        }else {
            Curso c = new Curso();
            c.setId(UUID.randomUUID().toString());
            c.setCurso(curso);
            c.setSeccion(seccion);
            c.setHorario(horario);

            databaseReference.child("Curso").child(c.getId()).setValue(c);
            Toast.makeText(this, "Curso Agregado", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarErrores(){
        if (curso.equals("")){
            txtCurso.setError("Requerido");
        }
        if (seccion.equals("")){
            txtSeccion.setError("Requerido");
        }
        if (horario.equals("")){
            txtHorario.setError("Requerido");
        }
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

}