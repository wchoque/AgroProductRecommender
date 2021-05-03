package com.upc.appcentroidiomas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
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
        holder.disenoname.setText(listaNoticia.get(position).getName());
        holder.disenodescription.setText(listaNoticia.get(position).getDescription());
        holder.disenopublishedAt.setText(listaNoticia.get(position).getPublishedAt());
        holder.disenopublishedBy.setText(listaNoticia.get(position).getPostedBy());

        new AdaptadorPersonalizadoNoticia.DownloadImageTask(holder.disenoimageUrl)
                .execute(listaNoticia.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return listaNoticia.size();
    }

    public class vistaHolder extends RecyclerView.ViewHolder {
        TextView disenoname,disenodescription,disenopublishedBy;
        TextView disenopublishedAt;
        ImageView disenoimageUrl;


        public vistaHolder(View items) {
            super(items);
            disenoname = items.findViewById(R.id.disenoname);
            disenopublishedBy = items.findViewById(R.id.disenopublishedBy);
            disenodescription = items.findViewById(R.id.disenodescription);
            disenopublishedAt = items.findViewById(R.id.disenopublishedAt);
            disenoimageUrl = items.findViewById(R.id.disenoimageUrl);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
