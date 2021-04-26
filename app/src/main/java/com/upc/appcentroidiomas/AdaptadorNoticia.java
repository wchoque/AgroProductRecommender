package com.upc.appcentroidiomas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

    public class AdaptadorNoticia  extends RecyclerView.Adapter<com.upc.appcentroidiomas.AdaptadorNoticia.vistaHolder>{
        private Context context;
        private ArrayList<Noticia> listaNoticia = new ArrayList<>();

        public AdaptadorNoticia(Context c, ArrayList<Noticia> lista){
            this.context = c;
            this.listaNoticia = lista;

        }

        @NonNull
        @Override
        public com.upc.appcentroidiomas.AdaptadorNoticia.vistaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View vista = inflater.inflate(R.layout.diseno_noticia,parent,false);
            return new vistaHolder(vista);
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorNoticia.vistaHolder holder, int position) {
            holder.filaName.setText(listaNoticia.get(position).getName()+" "+listaNoticia.get(position).getPostedBy());
            holder.filapublishedBy.setText(listaNoticia.get(position).getPostedBy()+"");

        }

        @Override
        public int getItemCount() {
            return listaNoticia.size();
        }

        public class vistaHolder extends RecyclerView.ViewHolder{
            TextView filaName,filapublishedBy;

            public vistaHolder(@NonNull View itemView) {
                super(itemView);
                filaName = itemView.findViewById(R.id.name);
                filapublishedBy = itemView.findViewById(R.id.publishedBy);
            }
        }
    }


