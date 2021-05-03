package com.upc.appcentroidiomas;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ActivityHorario extends AppCompatActivity {

    EditText txtCriterio;
    Button btnObtenerHorario;
    ListView lstHorario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);
        asignarReferencias();
    }
    private void asignarReferencias(){
        txtCriterio = findViewById(R.id.txtCriterio);
        btnObtenerHorario = findViewById(R.id.btnObtenerHorario);
        lstHorario = findViewById(R.id.lstHorario);
        btnObtenerHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarHorario();
            }
        });
    }
    private void buscarHorario(){
        String criterio = txtCriterio.getText().toString();
        String url;
        if(criterio.equals("")){
            url = "https://appcentroidiomas.azurewebsites.net/api/student/1/getallcourses";
        }else {
            url = "https://appcentroidiomas.azurewebsites.net/api/student/1/getallcourses/" + criterio;
        }

        StringRequest peticion = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<String> items = new ArrayList<>();
                    for (int i=0; i < jsonArray.length(); i++){
                        JSONObject objecto = jsonArray.getJSONObject(i);
                        items.add(objecto.getString("name")+"   --   "+"  --  "+objecto.getString("date")+ objecto.getString("startTime")+ "  --  "+objecto.getString("endTime"));
                    }

                    ArrayAdapter<String> adaptador = new ArrayAdapter<>(ActivityHorario.this, android.R.layout.simple_list_item_1,items);
                    lstHorario.setAdapter(adaptador);
                }catch (JSONException e){
                    Toast.makeText(ActivityHorario.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityHorario.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(peticion);
    }
}