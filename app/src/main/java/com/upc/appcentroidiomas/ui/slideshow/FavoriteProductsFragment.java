package com.upc.appcentroidiomas.ui.slideshow;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.upc.appcentroidiomas.FavoriteProductAdapter;
import com.upc.appcentroidiomas.Product;
import com.upc.appcentroidiomas.ProductsAdapter;
import com.upc.appcentroidiomas.R;
import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.LoggedInUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoriteProductsFragment extends Fragment {
    RecyclerView recyclerProducts;
    ArrayList<Product> products = new ArrayList<>();
    FavoriteProductAdapter favoriteProductsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_products, container, false);
        // Configura tus vistas aquí usando view.findViewById(...)
        super.onCreate(savedInstanceState);

        recyclerProducts = view.findViewById(R.id.recyclerFavoriteProducts);
        Search();

        return view;
    }


    private void Search(){
        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this.getContext()).getLoggedUser();

        String url = ApiContants.BASE_URL + "favorite-products/list/" + loggedInUser.getUserId();

        StringRequest peticion = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try{
                    products.clear();
                    JSONArray jsonArray= new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        Product _product = new Product();
                        _product.setId(object.getInt("id"));
                        _product.setUserId(object.getInt("userId"));
                        _product.setDescription(object.getString("description"));
                        _product.setLocation(object.getString("location"));
                        _product.setQuantity(object.getInt("quantity"));
                        _product.setPrice(object.getDouble("price"));
                        _product.setHarvestDate(object.getString("harvestDate"));
                        _product.setCreatedAt(object.getString("createdAt"));
                        _product.setCreatedBy(object.getString("createdBy"));
                        _product.setProductTypeId(object.getInt("productTypeId"));
                        _product.setProductTypeName(object.getString("productTypeName"));
                        _product.setProductPresentationId(object.getInt("productPresentationId"));
                        _product.setProductPresentationUnit(object.getString("productPresentationUnit"));
                        _product.setDefaultImageUrl(object.getString("defaultImageUrl"));

                        products.add(_product);
                    }

                    if (products.size() == 0 ){
                        Toast.makeText(getContext(), "No se encontraron productos", Toast.LENGTH_LONG).show();
                    }

                    favoriteProductsAdapter = new FavoriteProductAdapter(getContext(), products);
                    recyclerProducts.setAdapter(favoriteProductsAdapter);
                    recyclerProducts.setLayoutManager(new LinearLayoutManager(getContext()));

                }catch (JSONException e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(peticion);
    }
}