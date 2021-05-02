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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atencion);

      btnllamar = findViewById(R.id.btnllamar);
      btnllamar.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent i = new Intent(Intent.ACTION_CALL);
              i.setData(Uri.parse("tel:123456789"));
               startActivity(i);
          }
      });
    }
}