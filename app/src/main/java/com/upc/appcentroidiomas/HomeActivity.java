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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    FloatingActionButton btnMapas, btnCursos, btnPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //loadFragment(new DashboardFragment());
        asignarReferencias();

        //BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()){
//                case R.id.dashboardFragment:
//                    loadFragment(new DashboardFragment());
//                    break;
//                case R.id.chatFragment:
//                    loadFragment(new ChatFragment());
//                    break;
//                case R.id.profileFragment:
//                    loadFragment(new ProfileFragment());
//                    break;
//            }
//            return false;
//        });
    }

   /* private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, fragment)
                .setReorderingAllowed(true)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }*/

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

        btnCursos = findViewById(R.id.btnPerfil);
        btnCursos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}