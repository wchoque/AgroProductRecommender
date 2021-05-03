package com.upc.appcentroidiomas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;

public class ActivityUbicaciones extends AppCompatActivity {

    Button btnUbicacion1,btnUbicacion2, btnUbicacion3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicaciones);
        asignarReferencias();


    }

    private void asignarReferencias(){
        btnUbicacion1 = findViewById(R.id.btnUbicacion1);
        btnUbicacion2 = findViewById(R.id.btnUbicacion2);
        btnUbicacion3 = findViewById(R.id.btnUbicacion3);
        btnUbicacion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityUbicaciones.this,ActivityMapa.class);
                intent.putExtra("latitud","-12.087354");
                intent.putExtra("longitud","-77.050224");
                intent.putExtra("titulo","SEDE LA MOLINA");
                startActivity(intent);
            }
        });
        btnUbicacion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityUbicaciones.this,ActivityMapa.class);
                intent.putExtra("latitud","-12.103430");
                intent.putExtra("longitud","-76.963029");
                intent.putExtra("titulo","SEDE MIRAFLORES");
                startActivity(intent);
            }
        });
        btnUbicacion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityUbicaciones.this,ActivityMapa.class);
                intent.putExtra("latitud","-12.152323123125754");
                intent.putExtra("longitud","-76.98807530256826");
                intent.putExtra("titulo","SEDE SURCO");
                startActivity(intent);
            }
        });
    }
}