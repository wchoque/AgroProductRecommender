package com.upc.appcentroidiomas;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegNoticia extends AppCompatActivity {

    EditText txtname, txtdescripcion, txtpostedBy, txtpublishedAt;
    View imageUrl;
    Button btnRegistrarNoticia;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_noticia);
        asignarReferencias();

    }
    private void asignarReferencias(){
        txtname = findViewById(R.id.txtname);
        txtdescripcion = findViewById(R.id.txtdescription);
        txtpostedBy = findViewById(R.id.txtpostedBy);
        txtpublishedAt = findViewById(R.id.txtpublishedAt);
        imageUrl = findViewById(R.id.imageUrl);
        btnRegistrarNoticia = findViewById(R.id.btnRegistrarNoticia);
        btnRegistrarNoticia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarNoticia();
            }
        });
    }

    private void registrarNoticia(){
        String url = "https://appcentroidiomas.azurewebsites.net/api/Post";

        StringRequest peticion = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast toast = Toast.makeText(RegNoticia.this,"Se inserto correctamente",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegNoticia.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            // para enviar los datos

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("name",txtname.getText().toString());
                parametros.put("descripcion",txtdescripcion.getText().toString());
                parametros.put("publishedAt",txtpublishedAt.getText().toString());
                //parametros.put("imageUrl",imageUrl.getVi)
                parametros.put("postedBy",txtpostedBy.getText().toString());

                return parametros;
            }
        };
        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(peticion);
    }
}