package com.upc.appcentroidiomas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.common.wrappers.PackageManagerWrapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ActivityAtencion extends AppCompatActivity {

    ImageButton btnllamar;
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atencion);

        btnllamar = findViewById(R.id.btnllamar);
        btnllamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ActivityAtencion.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    // Aquí ya está concedido, procede a realizar lo que tienes que hacer
                    Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:784545"));
                    startActivity(i);
                } else {
                    // Aquí lanzamos un dialog para que el usuario confirme si permite o no el realizar llamadas
                    ActivityCompat.requestPermissions(ActivityAtencion.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }
            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // PERMISO CONCEDIDO, procede a realizar lo que tienes que hacer
                    Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0000000"));
                } else {
                    // PERMISO DENEGADO
                }
                return;
            }
        }
    }
}