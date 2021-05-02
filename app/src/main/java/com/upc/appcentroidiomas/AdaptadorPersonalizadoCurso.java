package com.upc.appcentroidiomas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;



public class AdaptadorPersonalizadoCurso extends RecyclerView.Adapter<AdaptadorPersonalizadoCurso.vistaHolder>{

    private Context context;
    private ArrayList<Curso> listaCursos = new ArrayList<>();

    public AdaptadorPersonalizadoCurso(Context c , ArrayList<Curso> lista){
        this.context = c;
        this.listaCursos = lista;
    }


    @NonNull
    @Override
    public AdaptadorPersonalizadoCurso.vistaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View vista = inflater.inflate(R.layout.diseno_fila,parent,false);
        return new vistaHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPersonalizadoCurso.vistaHolder holder, int position) {
        holder.filaNombreCurso.setText(listaCursos.get(position).getCurso()+"");
        holder.filaSeccion.setText(listaCursos.get(position).getSeccion()+"");
        holder.filaHorario.setText(listaCursos.get(position).getHorario()+"");
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, ActivityCursos.class);
                intent.putExtra("id", listaCursos.get(position).getId()+"");
                intent.putExtra("curso", listaCursos.get(position).getCurso()+"");
                intent.putExtra("seccion", listaCursos.get(position).getSeccion()+"");
                intent.putExtra("horario", listaCursos.get(position).getHorario()+"");
                context.startActivity(intent);
            }
        });
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar(listaCursos.get(position).getId());
            }
        });
    }

    private void eliminar(String id){
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        FirebaseApp.initializeApp(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Mensage de Confirmacion");
        builder.setMessage("Desea Eliminar el Curso ?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child("Curso").child(id).removeValue();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
    

    @Override
    public int getItemCount() {
        return listaCursos.size();
    }

    public class vistaHolder extends RecyclerView.ViewHolder{
        TextView filaNombreCurso, filaSeccion, filaHorario;
        ImageButton btnEditar, btnEliminar;

        public vistaHolder(@NonNull View itemView) {
            super(itemView);
            filaNombreCurso = itemView.findViewById(R.id.filaSecretaria);
            filaSeccion = itemView.findViewById(R.id.filaHorarioAtencion);
            filaHorario = itemView.findViewById(R.id.filaHorario);
            btnEditar = itemView.findViewById(R.id.btnllamar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
