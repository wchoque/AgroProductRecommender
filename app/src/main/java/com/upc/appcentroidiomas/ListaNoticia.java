package com.upc.appcentroidiomas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaNoticia extends AppCompatActivity {
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
                Intent intent = new Intent(ListaNoticia.this,RegNoticia.class);
                startActivity(intent);
            }
        });

    }

    private void BuscarNoticia(){
        String criterio = txtCriterio.getText().toString();
        String url;
        if (criterio.equals("")){
            url = "https://appcentroidiomas.azurewebsites.net/api/Post";
        }else{
            url = "https://appcentroidiomas.azurewebsites.net/api/Post" + criterio;
        }

        StringRequest peticion = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray= new JSONArray(response);
                    List<String> items = new ArrayList<>();// también se conecta con un recycler para que se muestre
                    for (int i=0; i < jsonArray.length(); i++){
                        JSONObject objeto = jsonArray.getJSONObject(i);
                        items.add(objeto.getString("name") + objeto.getString("description") + objeto.getString("publishedAt") + objeto.getJSONObject("imageUrl") + objeto.getString("postedBy"));
                    }
                    //ArrayAdapter<String> adaptador = new ArrayAdapter<>(ListaNoticia.this, android.R.layout.simple_list_item_1,items);
                    //recyclerNoticia.setAdapter(adaptador);
                   // aqui se agrega el diseño personalizado
                    adaptador = new AdaptadorPersonalizadoNoticia(ListaNoticia.this, listaNoticia);
                    recyclerNoticia.setAdapter(adaptador);
                    recyclerNoticia.setLayoutManager(new LinearLayoutManager(ListaNoticia.this ));

                }catch (JSONException e){
                    Toast.makeText(ListaNoticia.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
             public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListaNoticia.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(peticion);
        
    }
}