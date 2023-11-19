package com.upc.appcentroidiomas;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.ChatApi;
import com.upc.appcentroidiomas.api.ProductApi;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.LoggedInUser;
import com.upc.appcentroidiomas.data.model.NewMessageModel;
import com.upc.appcentroidiomas.data.model.NewMessageResponse;
import com.upc.appcentroidiomas.data.model.NewProductModel;
import com.upc.appcentroidiomas.data.model.ProductResponse;

import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText txtHarvestDate, txtDescription, txtLocation, txtQuantity, txtPrice;
    Spinner spinnerType, spinnerPresentationUnit;

    Button btnConfirmAddProduct;
    TextView txtTitle;

    int id;

    Boolean indregistrar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        txtTitle = findViewById(R.id.txt_product_title);
        txtHarvestDate = findViewById(R.id.txt_product_harvestDate);
        txtDescription = findViewById(R.id.txt_product_description);
        txtLocation = findViewById(R.id.txt_product_location);
        txtQuantity = findViewById(R.id.txt_product_quantity);



        //TODO TEST
        spinnerType = (Spinner) findViewById(R.id.spinner_product_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.planets_array,
                android.R.layout.simple_spinner_item
        );
// Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        spinnerType.setAdapter(adapter);


        spinnerPresentationUnit = (Spinner) findViewById(R.id.spinner_product_presentationUnit);
        ArrayAdapter<CharSequence> adapterPresentationUnit = ArrayAdapter.createFromResource(
                this,
                R.array.units_array,
                android.R.layout.simple_spinner_item
        );
        adapterPresentationUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPresentationUnit.setAdapter(adapterPresentationUnit);



        txtPrice = findViewById(R.id.txt_product_price);

        btnConfirmAddProduct = findViewById(R.id.btnConfirmAddProduct);
        btnConfirmAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indregistrar) {
                    registrar();
                }else {
                    actualizar();
                }
            }
        });

        verificarRegistraroActualizar();
    }
    private void verificarRegistraroActualizar(){
        if (getIntent().hasExtra("id")){
            //actualizar
            indregistrar = false;
            setTitle("Actualizar Producto");
            txtTitle.setText("Actualizar Producto");

            id = new Integer(getIntent().getStringExtra("id"));
            txtHarvestDate.setText(getIntent().getStringExtra("harvestDate"));
            txtDescription.setText(getIntent().getStringExtra("description"));
            txtLocation.setText(getIntent().getStringExtra("location"));
            //txtPresentationUnit.setText(getIntent().getStringExtra("presentationUnit"));
            txtQuantity.setText(getIntent().getStringExtra("quantity"));

            //txtType.setText(getIntent().getStringExtra("type"));

            txtPrice.setText(getIntent().getStringExtra("price"));

            btnConfirmAddProduct.setText("Actualizar Producto");
        }else {
            //registrar
            indregistrar = true;
            setTitle("Registrar Producto");
            txtTitle.setText("Registrar Producto");

            txtHarvestDate.setText("");
            txtDescription.setText("");
            txtLocation.setText("");
            //txtPresentationUnit.setText("");
            txtQuantity.setText("");
            //txtType.setText("");
            txtPrice.setText("");

            btnConfirmAddProduct.setText("Registrar Producto");
        }
    }

    private void actualizar(){
        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this).getLoggedUser();
        boolean hasErrors = validateProduct();

        if (!hasErrors){

            NewProductModel product = new NewProductModel();
            product.id = id;
            product.description = txtDescription.getText().toString();
            product.location = txtLocation.getText().toString();
            product.quantity = Integer.parseInt(txtQuantity.getText().toString());
            product.price = Double.parseDouble(txtPrice.getText().toString());
            product.harvestDate = txtHarvestDate.getText().toString();
            product.productPresentationId = 1;
            product.productTypeId = 1;

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiContants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ProductApi productApi = retrofit.create(ProductApi.class);

            Call<ProductResponse> call = productApi.update(product.id, product);
            call.enqueue(new Callback<ProductResponse>() {
                @ Override
                public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(ActivityProduct.this, "Producto actualizado con exito.", Toast.LENGTH_LONG).show();
                        //We will close the window only when created the product
                        finish();
                    }else {
                        Toast.makeText(ActivityProduct.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ProductResponse> call, Throwable t) {
                    Toast.makeText(ActivityProduct.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void registrar(){
        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this).getLoggedUser();
        boolean hasErrors = validateProduct();

        if (!hasErrors){
            //TODO CALL PRODUCTS API


            NewProductModel newProduct = new NewProductModel();
            newProduct.userId = loggedInUser.getUserId();
            newProduct.description = txtDescription.getText().toString();
            newProduct.location = txtLocation.getText().toString();
            newProduct.quantity = Integer.parseInt(txtQuantity.getText().toString());
            newProduct.price = Double.parseDouble(txtPrice.getText().toString());
            newProduct.harvestDate = txtHarvestDate.getText().toString();
            newProduct.productPresentationId = 1;
            newProduct.productTypeId = 1;

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiContants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ProductApi productApi = retrofit.create(ProductApi.class);

            Call<ProductResponse> call = productApi.create(newProduct);
            call.enqueue(new Callback<ProductResponse>() {
                @ Override
                public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(ActivityProduct.this, "Producto agregado con exito.", Toast.LENGTH_LONG).show();
                        //We will close the window only when created the product
                        finish();

                        //TODO
                        //userChatNewContentMessage.setText("");
                        //refreshHistoryChat();
                    }
                }

                @Override
                public void onFailure(Call<ProductResponse> call, Throwable t) {
                    Toast.makeText(ActivityProduct.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean validateProduct(){
        //TODO I NEED THE IDS i need to add a drowpdown
        //product.setProductPresentationId(txtPresentationUnit.getText().toString());
        //product.setProductTypeId(txtType.getText().toString());

        boolean hasErrors = false;
        if (txtDescription.getText().equals("")){
            txtDescription.setError("Requerido");
            hasErrors = true;
        }

        if (txtDescription.getText().equals("")){
            txtLocation.setError("Requerido");
            hasErrors = true;
        }

        if (txtHarvestDate.getText().equals("")){
            txtHarvestDate.setError("Requerido");
            hasErrors = true;
        }

        try {
            if (Integer.parseInt(txtQuantity.getText().toString()) <= 0){
                txtQuantity.setError("Requerido");
                hasErrors = true;
            }
        } catch (NumberFormatException nfe) {
            txtQuantity.setError("Requerido");
            hasErrors = true;
        }

        try {
            if (Double.parseDouble(txtPrice.getText().toString()) <= 0){
                txtPrice.setError("Requerido");
                hasErrors = true;
            }
        } catch (NumberFormatException nfe) {
            txtPrice.setError("Requerido");
            hasErrors = true;
        }

        /*
        try {
            if (Integer.parseInt(txtType.getText().toString()) <= 0){
                txtType.setError("Requerido");
                hasErrors = true;
            }
        } catch (NumberFormatException nfe) {
            txtType.setError("Requerido");
            hasErrors = true;
        }*/

        /*
        try {
            if (Integer.parseInt(txtPresentationUnit.getText().toString()) <= 0){
                txtPresentationUnit.setError("Requerido");
                hasErrors = true;
            }
        } catch (NumberFormatException nfe) {
            txtPresentationUnit.setError("Requerido");
            hasErrors = true;
        }*/

        return hasErrors;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
