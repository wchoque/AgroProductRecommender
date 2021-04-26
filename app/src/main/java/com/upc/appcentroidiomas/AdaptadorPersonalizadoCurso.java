package com.upc.appcentroidiomas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

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
    }

    @Override
    public int getItemCount() {
        return listaCursos.size();
    }

    public class vistaHolder extends RecyclerView.ViewHolder{
        TextView filaNombreCurso, filaSeccion, filaHorario;
        public vistaHolder(@NonNull View itemView) {
            super(itemView);
            filaNombreCurso = itemView.findViewById(R.id.filaNombreCurso);
            filaSeccion = itemView.findViewById(R.id.filaSeccion);
            filaHorario = itemView.findViewById(R.id.filaHorario);
        }
    }
}
