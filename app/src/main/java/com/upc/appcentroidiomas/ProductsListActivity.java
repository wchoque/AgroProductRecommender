package com.upc.appcentroidiomas;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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

public class ProductsListActivity extends AppCompatActivity {
    EditText txtFilter;
    Button btnSearch;
    RecyclerView recyclerProducts;
    FloatingActionButton btnAddProduct;
    ArrayList<Product> products = new ArrayList<>();
    ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);

        txtFilter = findViewById(R.id.txtFilter);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search();
            }
        });

        recyclerProducts = findViewById(R.id.recyclerProducts);
        //productsAdapter = new ProductsAdapter(ProductsListActivity.this, products);

        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        //recyclerProducts.setLayoutManager(mLayoutManager);
        //recyclerProducts.setItemAnimator(new DefaultItemAnimator());
        //recyclerProducts.setAdapter(productsAdapter);

        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsListActivity.this, ActivityProduct.class);
                startActivity(intent);
            }
        });

        Search();
    }

    private void Search(){
        String criteria = txtFilter.getText().toString();
        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this.getApplicationContext()).getLoggedUser();

        String url = ApiContants.BASE_URL + "products/filtered-by-user?userId=" + loggedInUser.getUserId();

        if (!criteria.equals("")){
            url = url + "&description=" + criteria;
        }

            /* TODO I NEED TO UPDATE TO THIS
        // TODO can be launched in a separate asynchronous job
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this.getApplicationContext()).getLoggedUser();

        ProductApi productApi = retrofit.create(ProductApi.class);
        Call<ArrayList<ProductResponse>> call = productApi.getFilteredProducts(loggedInUser.getUserId(), criteria);

        call.enqueue(new Callback<ArrayList<ProductResponse>>() {
            @ Override
            public void onResponse(Call<ArrayList<ProductResponse>> call, retrofit2.Response<ArrayList<ProductResponse>> response) {
                if (response.isSuccessful()){

                    //productsAdapter = new ProductsAdapter(ProductsListActivity.this, products);
                    //recyclerProducts.setAdapter(productsAdapter);
                    //recyclerProducts.setLayoutManager(new LinearLayoutManager(ProductsListActivity.this ));
                    products = response.body().forEach(){

                    };
                    productsAdapter.setProducts(products);
                    productsAdapter.notifyDataSetChanged();

                    //availableUsers = response.body().availableUsers;
                    //chatAdapter.setAvailableUsers(availableUsers);
                    //chatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProductResponse>> call, Throwable t) {

            }
        });
        */



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
                        Toast.makeText(ProductsListActivity.this, "No se encontraron productos con su filtro.", Toast.LENGTH_LONG).show();
                    }

                    productsAdapter = new ProductsAdapter(ProductsListActivity.this, products);
                    recyclerProducts.setAdapter(productsAdapter);
                    recyclerProducts.setLayoutManager(new LinearLayoutManager(ProductsListActivity.this ));

                }catch (JSONException e){
                    Toast.makeText(ProductsListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductsListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(peticion);
    }

    protected void onResume() {
        super.onResume();
        //TODO refresh data?
        //refreshAvailableUsersList();
    }
}