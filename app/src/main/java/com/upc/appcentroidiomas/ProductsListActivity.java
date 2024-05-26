package com.upc.appcentroidiomas;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.LoggedInUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductsListActivity extends Fragment {
    EditText txtFilter;
    Button btnSearch;
    RecyclerView recyclerProducts;
    FloatingActionButton btnAddProduct;
    ArrayList<Product> products = new ArrayList<>();
    ProductsAdapter productsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list_products, container, false);
        // Configura tus vistas aquí usando view.findViewById(...)
        super.onCreate(savedInstanceState);

        txtFilter = view.findViewById(R.id.txtFilter);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search();
            }
        });

        recyclerProducts = view.findViewById(R.id.recyclerProducts);
        //productsAdapter = new ProductsAdapter(ProductsListActivity.this, products);

        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        //recyclerProducts.setLayoutManager(mLayoutManager);
        //recyclerProducts.setItemAnimator(new DefaultItemAnimator());
        //recyclerProducts.setAdapter(productsAdapter);

        btnAddProduct = view.findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //Intent intent = new Intent(ProductsListActivity.this, ActivityProduct.class);
                //startActivity(intent);
                Intent intent = new Intent(getActivity(), ActivityProduct.class);
                startActivity(intent);
            }
        });

        Search();

        return view;
    }


    private void Search(){
        String criteria = txtFilter.getText().toString();
        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this.getContext()).getLoggedUser();

        String url = ApiContants.BASE_URL + "products/filtered-by-user?userId=" + loggedInUser.getUserId();

        if (!criteria.equals("")){
            url = url + "&description=" + criteria;
        }

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
                        Toast.makeText(getContext(), "No se encontraron productos con su filtro.", Toast.LENGTH_LONG).show();
                    }

                    productsAdapter = new ProductsAdapter(getContext(), products);
                    recyclerProducts.setAdapter(productsAdapter);
                    recyclerProducts.setLayoutManager(new LinearLayoutManager(getContext()));

                }catch (JSONException e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(ProductsListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(peticion);
    }

    /*protected void onResume() {
        super.onResume();
        //TODO refresh data?
        //refreshAvailableUsersList();
    }*/
}