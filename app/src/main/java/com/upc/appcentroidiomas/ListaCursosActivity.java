package com.upc.appcentroidiomas;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class ListaCursosActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ListView lstCursos;
    FloatingActionButton btnAgregarCurso;

    ArrayList<Curso> listaCursos = new ArrayList<>();
    ArrayAdapter<Curso> cursoArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cursos);
        asignarReferencias();
        inicializarFirebase();
        listarDatos();

    }
    private void asignarReferencias(){
        lstCursos =findViewById(R.id.lstCursos);
        btnAgregarCurso = findViewById(R.id.btnAgregarCurso);
        btnAgregarCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaCursosActivity.this, ActivityCursos.class);
                startActivity(intent);
            }
        });
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void listarDatos() {
        databaseReference.child("Curso").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaCursos.clear();
                for (DataSnapshot item:snapshot.getChildren()) {
                    Curso c = item.getValue(Curso.class);
                    listaCursos.add(c);
                }

                cursoArrayAdapter = new ArrayAdapter<>(ListaCursosActivity.this, android.R.layout.simple_list_item_1,listaCursos);
                lstCursos.setAdapter(cursoArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}