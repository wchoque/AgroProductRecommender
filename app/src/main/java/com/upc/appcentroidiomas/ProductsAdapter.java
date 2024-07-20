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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.ChatApi;
import com.upc.appcentroidiomas.api.ProductApi;
import com.upc.appcentroidiomas.api.ProductChatApi;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.AvailableChatUserDetailResponse;
import com.upc.appcentroidiomas.data.model.HistoryChatResponse;
import com.upc.appcentroidiomas.data.model.LoggedInUser;
import com.upc.appcentroidiomas.data.model.NewMessageModel;
import com.upc.appcentroidiomas.data.model.NewMessageResponse;
import com.upc.appcentroidiomas.data.model.ProductResponse;
import com.upc.appcentroidiomas.data.model.UserInformationModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.vistaHolder>{
    private Context context;
    private ArrayList<Product> products = new ArrayList<>();

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ProductsAdapter(Context context , ArrayList<Product> products){
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductsAdapter.vistaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View vista = inflater.inflate(R.layout.row_product,parent,false);
        return new vistaHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.vistaHolder holder, @SuppressLint("RecyclerView") int position) {
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

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, ActivityProduct.class);
                intent.putExtra("id", products.get(position).getId() + "");
                intent.putExtra("harvestDate", products.get(position).getHarvestDate());
                intent.putExtra("description", products.get(position).getDescription());
                intent.putExtra("location", products.get(position).getLocation());
                intent.putExtra("quantity", products.get(position).getQuantity() + "");
                intent.putExtra("price", products.get(position).getPrice() + "");

                //intent.putExtra("curso", products.get(position).getCurso()+"");
                //intent.putExtra("seccion", products.get(position).getSeccion()+"");
                //intent.putExtra("horario", products.get(position).getHorario()+"");
                context.startActivity(intent);
            }
        });

        holder.btnChatToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiContants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ProductChatApi productChatApi = retrofit.create(ProductChatApi.class);
                LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), context).getLoggedUser();

                NewMessageModel newMessage = new NewMessageModel();
                newMessage.userIdFrom = loggedInUser.getUserId();
                newMessage.userIdTo = product.getUserId();
                newMessage.messageContent = "Quisiera mas informacion del producto " + product.getDescription();

                Call<NewMessageResponse> call = productChatApi.SendNewMessage(newMessage);
                call.enqueue(new Callback<NewMessageResponse>() {
                    @Override
                    public void onResponse(Call<NewMessageResponse> call, Response<NewMessageResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Mensaje enviado", Toast.LENGTH_LONG).show();


                            Intent intent= new Intent(context, ChatDetailActivity.class);
                            intent.putExtra("id", products.get(position).getId() + "");
                            intent.putExtra("userIdTo", newMessage.userIdTo);
                            intent.putExtra("displayNameTo", "Wilber Choque");
                            intent.putExtra("imageUrl", products.get(position).getDefaultImageUrl());

                            //llamr a api
                            //obtener chat

                            //intent.putExtra("curso", products.get(position).getCurso()+"");
                            //intent.putExtra("seccion", products.get(position).getSeccion()+"");
                            //intent.putExtra("horario", products.get(position).getHorario()+"");
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "Ha sucedido un error, intente nuevamente mas tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<NewMessageResponse> call, Throwable t) {
                        Toast.makeText(context, "Ha sucedido un error, intente nuevamente mas tarde.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(products.get(position).getId());
            }
        });
        holder.btnDelete.setVisibility(View.INVISIBLE);
        holder.btnUpdate.setVisibility(View.INVISIBLE);

        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), context).getLoggedUser();
        if (loggedInUser.isProductorAgricola()){
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnUpdate.setVisibility(View.VISIBLE);
        }

        new DownloadImageTask(holder.rowDefaultImageUrl)
                .execute(products.get(position).getDefaultImageUrl());
    }

    private void delete(int id){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Mensage de Confirmacion");
        builder.setMessage("Desea eliminar el producto?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiContants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ProductApi productApi = retrofit.create(ProductApi.class);

                Call<ResponseBody> call = productApi.delete(id);
                call.enqueue(new Callback<ResponseBody>() {
                    @ Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            //Toast.makeText(ActivityProduct.this, "Producto eliminado con exito.", Toast.LENGTH_LONG).show();
                            //We will close the window only when created the product
                            //userChatNewContentMessage.setText("");
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        //Toast.makeText(ActivityProduct.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                    }
                });
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
        return products.size();
    }

    public static class vistaHolder extends RecyclerView.ViewHolder{
        TextView rowDetail, rowPublishedAt, rowPublishedBy, rowLocation, rowType, rowQuantity, rowPrice, rowDescription;
        ImageView rowDefaultImageUrl;
        ImageButton btnUpdate, btnDelete, btnChatToUser;

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

            btnUpdate = itemView.findViewById(R.id.btnUpdateProduct);
            btnDelete = itemView.findViewById(R.id.btnDeleteProduct);
            btnChatToUser = itemView.findViewById(R.id.btnChatToUser);
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
