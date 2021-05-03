package com.upc.appcentroidiomas;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.upc.appcentroidiomas.api.ApiContants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class NoticiaActivity extends AppCompatActivity {
    EditText txtCriterio;
    Button btnBuscar;
    RecyclerView recyclerNoticia;
    FloatingActionButton btnAgregarNoticia;

    // para implemetar adaptador
    ArrayList<Noticia> listaNoticia = new ArrayList<>();
    AdaptadorPersonalizadoNoticia adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_noticia);
        asignarReferencias();
        BuscarNoticia();
    }

    private void asignarReferencias(){
        txtCriterio = findViewById(R.id.txtCriterio);
        btnBuscar = findViewById(R.id.btnBuscar);

        recyclerNoticia = findViewById(R.id.recyclerNoticia);
        //btnAgregarNoticia = findViewById(R.id.btnAgregarNoticia);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuscarNoticia();
            }
        });
        btnAgregarNoticia = findViewById(R.id.btnAgregarNoticia);
        btnAgregarNoticia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoticiaActivity.this,RegNoticia.class);
                startActivity(intent);
            }
        });

    }

    private void BuscarNoticia(){
        String criterio = txtCriterio.getText().toString();
        String url;
        if (criterio.equals("")){
            url = ApiContants.BASE_URL + "Post";
        }else{
            url = ApiContants.BASE_URL + "Post/GetPostsByName/" + criterio;
        }

        StringRequest peticion = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try{
                    listaNoticia.clear();
                    JSONArray jsonArray= new JSONArray(response);
                    for (int i=0; i < jsonArray.length(); i++){
                        JSONObject objeto = jsonArray.getJSONObject(i);

                        Noticia _noticia = new Noticia();
                        _noticia.setId(objeto.getInt("id"));
                        _noticia.setName(objeto.getString("name"));
                        _noticia.setDescription(objeto.getString("description"));
                        _noticia.setPublishedAt(objeto.getString("publishedAt"));
                        _noticia.setPostedBy(objeto.getString("postedBy"));
                        _noticia.setImageUrl(objeto.getString("imageUrl"));

                        listaNoticia.add(_noticia);
                    }
                    //ArrayAdapter<String> adaptador = new ArrayAdapter<>(ListaNoticia.this, android.R.layout.simple_list_item_1,items);
                    //recyclerNoticia.setAdapter(adaptador);
                   // aqui se agrega el diseño personalizado
                    adaptador = new AdaptadorPersonalizadoNoticia(NoticiaActivity.this, listaNoticia);
                    recyclerNoticia.setAdapter(adaptador);
                    recyclerNoticia.setLayoutManager(new LinearLayoutManager(NoticiaActivity.this ));

                }catch (JSONException e){
                    Toast.makeText(NoticiaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
             public void onErrorResponse(VolleyError error) {
                Toast.makeText(NoticiaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(peticion);
        
    }
}