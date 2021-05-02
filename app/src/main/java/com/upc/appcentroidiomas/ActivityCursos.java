package com.upc.appcentroidiomas;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ActivityCursos extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    EditText txtCurso, txtSeccion, txtHorario;
    Button btnRegistrarCurso;
    TextView titulo;

    String curso, seccion, horario, id;

    Boolean indregistrar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);
        inicializarFirebase();
        asignarReferencias();
        verificarRegistraroActualizar();
    }
    private void verificarRegistraroActualizar(){
        if (getIntent().hasExtra("id")){
            //actualizar
            indregistrar = false;
            setTitle("Actualizar Curso");
            titulo.setText("Actualizar Curso");
            id= getIntent().getStringExtra("id");
            txtCurso.setText(getIntent().getStringExtra("curso"));
            txtSeccion.setText(getIntent().getStringExtra("seccion"));
            txtHorario.setText(getIntent().getStringExtra("horario"));
            btnRegistrarCurso.setText("Actualizar Curso");
        }else {
            //registrar
            indregistrar = true;
            setTitle("Registrar Curso");
            titulo.setText("Registrar Curso");
            txtCurso.setText("");
            txtSeccion.setText("");
            txtHorario.setText("");
            btnRegistrarCurso.setText("Registrar Curso");
        }
    }

    private void asignarReferencias(){
        titulo = findViewById(R.id.titulo);
        txtCurso = findViewById(R.id.txtCurso);
        txtSeccion = findViewById(R.id.txtSeccion);
        txtHorario = findViewById(R.id.txtHorario);
        btnRegistrarCurso = findViewById(R.id.btnRegistrarCurso);
        btnRegistrarCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indregistrar) {
                    registrar();
                }else {
                    actualizar();
                }
                finish();
            }
        });
    }
    private void actualizar(){
        curso = txtCurso.getText().toString();
        seccion = txtSeccion.getText().toString();
        horario = txtHorario.getText().toString();
        if (curso.equals("") || seccion.equals("") || horario.equals("")){
            mostrarErrores();
        }else {
          HashMap map = new HashMap();
          map.put("curso", curso);
            map.put("seccion", seccion);
            map.put("horario", horario);
            databaseReference.child("Curso").child(id).updateChildren(map);
            Toast.makeText(this, "Curso Actualizado", Toast.LENGTH_SHORT).show();
        }
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
