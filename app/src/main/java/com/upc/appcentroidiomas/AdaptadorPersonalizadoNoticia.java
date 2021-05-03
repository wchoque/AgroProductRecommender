package com.upc.appcentroidiomas;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorPersonalizadoNoticia extends RecyclerView.Adapter<AdaptadorPersonalizadoNoticia.vistaHolder>{
    private Context context;
    private ArrayList<Noticia> listaNoticia = new ArrayList<>();

    public AdaptadorPersonalizadoNoticia(Context c , ArrayList<Noticia> lista){
        this.context = c;
        this.listaNoticia = lista;
    }

    @NonNull
    @Override
    public AdaptadorPersonalizadoNoticia.vistaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View vista = inflater.inflate(R.layout.diseno_noticia,parent,false);
        return new vistaHolder(vista);
        
    }
 
    @Override
    public void onBindViewHolder(@NonNull AdaptadorPersonalizadoNoticia.vistaHolder holder, int position) {
        holder.disenoname.setText(listaNoticia.get(position).getName()+ "");
        holder.disenopublishedBy.setText(listaNoticia.get(position).getPublishedBy()+ "");
        holder.disenopublishedAt.setText(listaNoticia.get(position).getPublishedAt()+ "");
        holder.disenodescription.setText(listaNoticia.get(position).getDescription()+ "");
       // holder.disenoimageUrl.
    }

    @Override
    public int getItemCount() {
        return listaNoticia.size();
    }

    public class vistaHolder extends RecyclerView.ViewHolder {

        TextView disenoname,disenodescription,disenopublishedBy;
        EditText disenopublishedAt;
        Image disenoimageUrl;


        public vistaHolder(View items) {
            super(items);
            disenoname = items.findViewById(R.id.disenoname);
            disenopublishedBy = items.findViewById(R.id.disenopublishedBy);
            disenodescription = items.findViewById(R.id.disenodescription);
            disenopublishedAt = items.findViewById(R.id.disenopublishedAt);
            //disenoimageUrl = vista.findViewById(R.id.disenoimageUrl);

        }
    }
}
