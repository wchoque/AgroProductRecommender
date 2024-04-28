package com.upc.appcentroidiomas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.ProductApi;

import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoriteProductAdapter extends RecyclerView.Adapter<FavoriteProductAdapter.vistaHolder>{
    private Context context;
    private ArrayList<Product> products = new ArrayList<>();

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public FavoriteProductAdapter(Context context , ArrayList<Product> products){
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public FavoriteProductAdapter.vistaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_favorite_product,parent,false);
        return new vistaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteProductAdapter.vistaHolder holder, @SuppressLint("RecyclerView") int position) {
        Product product = products.get(position);
        String customProductDetail = "Fecha de cosecha " + product.getHarvestDate();
        holder.rowDetail.setText(customProductDetail);
        holder.rowPublishedAt.setText("Fecha de publicación: " + product.getCreatedAt());
        holder.rowPublishedBy.setText("Publicado por: " + product.getCreatedBy());
        holder.rowLocation.setText("Ubicación: "+ product.getLocation());
        holder.rowQuantity.setText("Cantidad: "+ product.getQuantity());
        holder.rowPrice.setText("Precio: "+ product.getPrice());
        holder.rowDescription.setText("Descripción: "+ product.getDescription());
        holder.rowType.setText("Variedad: "+ product.getProductTypeName());

        new DownloadImageTask(holder.rowDefaultImageUrl)
                .execute(products.get(position).getDefaultImageUrl());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class vistaHolder extends RecyclerView.ViewHolder{
        TextView rowDetail, rowPublishedAt, rowPublishedBy, rowLocation, rowType, rowQuantity, rowPrice, rowDescription;
        ImageView rowDefaultImageUrl;

        public vistaHolder(@NonNull View itemView) {
            super(itemView);
            rowDetail = itemView.findViewById(R.id.row_product_detail);
            rowPublishedAt = itemView.findViewById(R.id.row_product_publishedAt);
            rowPublishedBy = itemView.findViewById(R.id.row_product_publishedBy);
            rowLocation = itemView.findViewById(R.id.row_product_location);
            rowType = itemView.findViewById(R.id.row_product_type);
            rowQuantity = itemView.findViewById(R.id.row_product_quantity);
            rowPrice = itemView.findViewById(R.id.row_product_price);
            rowDescription = itemView.findViewById(R.id.row_product_description);
            rowDefaultImageUrl = itemView.findViewById(R.id.row_product_defaultImageUrl);
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
