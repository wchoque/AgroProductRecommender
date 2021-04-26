package com.upc.appcentroidiomas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FloatingActionButton btnMapas, btnCursos;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ListView lstCurso;
    ArrayList<Curso> listaCursos = new ArrayList<>();
    ArrayAdapter<Curso> cursoArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadFragment(new DashboardFragment());
        asignarReferencias();
        inicializarFirebase();
        listarDatos();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.dashboardFragment:
                    loadFragment(new DashboardFragment());
                    break;
                case R.id.chatFragment:
                    loadFragment(new ChatFragment());
                    break;
                case R.id.profileFragment:
                    loadFragment(new ProfileFragment());
                    break;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, fragment)
                .setReorderingAllowed(true)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    private void asignarReferencias(){
        btnMapas = findViewById(R.id.btnMapas);
        btnMapas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ActivityMapa.class);
                startActivity(intent);
            }
        });
        btnCursos = findViewById(R.id.btnCursos);
        btnCursos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ActivityCursos.class);
                startActivity(intent);
            }
        });

    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void listarDatos(){
        databaseReference.child("Curso").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaCursos.clear();
                for (DataSnapshot item:snapshot.getChildren()){
                    Curso c = item.getValue(Curso.class);
                    listaCursos.add(c);
                }
                cursoArrayAdapter = new ArrayAdapter<>(HomeActivity.this, android.R.layout.simple_list_item_1,listaCursos);
                lstCurso.setAdapter(cursoArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}