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
import com.upc.appcentroidiomas.data.model.OrderRatingModel;
import com.upc.appcentroidiomas.data.model.OrderResponse;
import com.upc.appcentroidiomas.data.model.UpdateOrderModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityOrderHistory extends AppCompatActivity {

    EditText txtOrderHistoryCreationDate, txtOrderHistoryOtherUser, txtOrderHistoryProductType, txtOrderHistoryProductDescription, txtOrderHistoryQuantity, txtOrderHistoryHarvestDate, txtOrderHistoryTotalAmount;
    Spinner spinnerOrderStatus;
    RatingBar orderHistoryRatingBar;
    Button btnRate;

    int orderId;
    int otherUserId;
    int productChatMessageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_detail);

        txtOrderHistoryCreationDate = findViewById(R.id.txt_order_history_creation_date);
        txtOrderHistoryOtherUser = findViewById(R.id.txt_order_history_other_user);
        txtOrderHistoryProductType = findViewById(R.id.txt_order_history_product_type);
        txtOrderHistoryProductDescription = findViewById(R.id.txt_order_history_product_description);
        txtOrderHistoryQuantity = findViewById(R.id.txt_order_history_quantity);
        txtOrderHistoryHarvestDate = findViewById(R.id.txt_order_history_harvest_date);
        txtOrderHistoryTotalAmount = findViewById(R.id.txt_order_history_total_amount);
        spinnerOrderStatus = findViewById(R.id.spinner_order_status);
        orderHistoryRatingBar = findViewById(R.id.order_history_rating_bar);

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
        otherUserId = getIntent().getIntExtra("otherUserId", 0);

        productChatMessageId = getIntent().getIntExtra("productChatMessageId", 0);

        // Load order details from the intent or make an API call to get the order details
        // For demonstration, setting the intent data
        txtOrderHistoryCreationDate.setText(getIntent().getStringExtra("orderDate"));
        txtOrderHistoryOtherUser.setText(getIntent().getStringExtra("otherUserName"));
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

        // Check the rating value and set the visibility of the button
        int rating = getIntent().getIntExtra("rating", 0);
        orderHistoryRatingBar.setRating(rating);
        if (rating > 0) {
            orderHistoryRatingBar.setVisibility(View.VISIBLE);
            btnRate.setVisibility(View.GONE);
        } else {
            orderHistoryRatingBar.setVisibility(View.GONE);
            btnRate.setVisibility(View.VISIBLE);
        }
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
                submitRating(rating);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void submitRating(int rating) {
        LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource(), getApplicationContext());
        LoggedInUser loggedUser = loginRepository.getLoggedUser();

        OrderRatingModel orderRating = new OrderRatingModel();
        orderRating.raterUserId = loggedUser.getUserId();
        orderRating.ratedUserId = otherUserId;
        orderRating.rating = rating;
        orderRating.comment = "";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OrderApi orderApi = retrofit.create(OrderApi.class);
        Call<Void> call = orderApi.rate(orderId, orderRating);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ActivityOrderHistory.this, "Orden actualizada correctamente.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(ActivityOrderHistory.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ActivityOrderHistory.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
