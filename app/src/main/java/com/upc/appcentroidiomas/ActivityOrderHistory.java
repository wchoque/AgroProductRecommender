package com.upc.appcentroidiomas;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.OrderApi;
import com.upc.appcentroidiomas.api.ProductChatApi;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.AvailableChatUserDetailResponse;
import com.upc.appcentroidiomas.data.model.LoggedInUser;
import com.upc.appcentroidiomas.data.model.OrderResponse;
import com.upc.appcentroidiomas.data.model.UpdateOrderModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityOrderHistory extends AppCompatActivity {

    EditText txtOrderHistoryCreationDate, txtOrderHistoryBuyer, txtOrderHistoryProductType, txtOrderHistoryProductDescription, txtOrderHistoryQuantity, txtOrderHistoryHarvestDate, txtOrderHistoryTotalAmount;
    Spinner spinnerOrderStatus;
    Button btnRate;

    int orderId;
    int productChatMessageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_detail);

        txtOrderHistoryCreationDate = findViewById(R.id.txt_order_history_creation_date);
        txtOrderHistoryBuyer = findViewById(R.id.txt_order_history_buyer);
        txtOrderHistoryProductType = findViewById(R.id.txt_order_history_product_type);
        txtOrderHistoryProductDescription = findViewById(R.id.txt_order_history_product_description);
        txtOrderHistoryQuantity = findViewById(R.id.txt_order_history_quantity);
        txtOrderHistoryHarvestDate = findViewById(R.id.txt_order_history_harvest_date);
        txtOrderHistoryTotalAmount = findViewById(R.id.txt_order_history_total_amount);
        spinnerOrderStatus = findViewById(R.id.spinner_order_status);


        ArrayAdapter<OrderStatus> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                OrderStatus.values()
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderStatus.setAdapter(statusAdapter);

        btnRate = findViewById(R.id.btn_rate);
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        setInitialValues();
    }

    private void setInitialValues() {
        // Actualizar
        orderId = getIntent().getIntExtra("orderId", 0);
        productChatMessageId = getIntent().getIntExtra("productChatMessageId", 0);

        // Load order details from the intent or make an API call to get the order details
        // For demonstration, setting the intent data
        txtOrderHistoryCreationDate.setText(getIntent().getStringExtra("orderDate"));
        txtOrderHistoryBuyer.setText(getIntent().getStringExtra("buyerName"));
        txtOrderHistoryProductType.setText(getIntent().getStringExtra("productTypeName"));
        txtOrderHistoryProductDescription.setText(getIntent().getStringExtra("productDescription"));
        txtOrderHistoryQuantity.setText(getIntent().getIntExtra("quantity",0) + "");
        txtOrderHistoryHarvestDate.setText(getIntent().getStringExtra("harvestDate"));
        txtOrderHistoryTotalAmount.setText(getIntent().getIntExtra("totalAmount",0) + "");

        // Set the order status
        OrderStatus orderStatus = OrderStatus.fromInt(getIntent().getIntExtra("status", 0));
        ArrayAdapter<OrderStatus> adapter = (ArrayAdapter<OrderStatus>) spinnerOrderStatus.getAdapter();
        int position = adapter.getPosition(orderStatus);
        spinnerOrderStatus.setSelection(position);
    }

    private void showRatingDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_rate_order);

        final RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        Button btnSubmit = dialog.findViewById(R.id.btn_submit_rating);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = (int) ratingBar.getRating();

                Toast.makeText(ActivityOrderHistory.this, "Calificación enviada correctamente.", Toast.LENGTH_LONG).show();
                //submitRating(rating);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void submitRating(int rating) {
        ;
        UpdateOrderModel order = new UpdateOrderModel();
        order.orderId = orderId;
        order.status = ((OrderStatus) spinnerOrderStatus.getSelectedItem()).ordinal();
        //order.rating = rating;  // Add the rating to the order model TODO

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OrderApi orderApi = retrofit.create(OrderApi.class);
        Call<OrderResponse> call = orderApi.update(orderId, order);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ActivityOrderHistory.this, "Orden actualizada correctamente.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(ActivityOrderHistory.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(ActivityOrderHistory.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
