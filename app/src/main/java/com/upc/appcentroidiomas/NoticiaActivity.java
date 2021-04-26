package com.upc.appcentroidiomas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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


public class NoticiaActivity extends AppCompatActivity {

    EditText txtCriterio;
    Button btnbuscar;
    ListView lstNoticia;
    FloatingActionButton btnNuevo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);
        asignarReferencias();
        listarDatos();
    }

    private void listarDatos(){


    }


    private void asignarReferencias(){
        txtCriterio = findViewById(R.id.txtCriterio);
        btnbuscar = findViewById(R.id.btnbuscar);
        lstNoticia = findViewById(R.id.lstNoticias);
        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarNoticia();
            }
        });
        btnNuevo = findViewById(R.id.btnNuevo);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoticiaActivity.this, RegistrarActivity.class);
                startActivity(intent);
            }
        });

    }

    private void buscarNoticia(){

        String criterio = txtCriterio.getText().toString();
        String url;
        if(criterio.equals("")){
            url = "https://appcentroidiomas.azurewebsites.net/api/Post";

        }else{
            url = "https://appcentroidiomas.azurewebsites.net/api/Post"+criterio;
        }


        StringRequest peticion = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //que va ocurrir cuando se recive respuesta a la petición
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    List<String> items = new ArrayList<>();// se puede conectar con un listview o un array
                    for (int i=0; i < jsonArray.length(); i++){// se hace un recorrido a jasonarray
                        JSONObject objeto = jsonArray.getJSONObject(i);
                        items.add(objeto.getString("name") + " " + objeto.getString("postedBy") + ")");
                    }
                    //se necesita un adaptador para poder conectar con el listview
                    ArrayAdapter<String> adaptador = new ArrayAdapter<>(NoticiaActivity.this, android.R.layout.simple_list_item_1, items);
                    lstNoticia.setAdapter(adaptador);
                }catch(JSONException e){
                    Toast.makeText(NoticiaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {// que pasa si ocurre un error en la peticion
                Toast.makeText(NoticiaActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        //se envia la peticion para que se pueda ejecutar en php y se ejecuta con la libreria volley
        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(peticion);
    }
}