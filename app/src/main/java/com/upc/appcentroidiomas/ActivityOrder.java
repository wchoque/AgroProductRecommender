package com.upc.appcentroidiomas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class ActivityOrder extends AppCompatActivity {

    EditText txtOrderCreationDate, txtOrderBuyer, txtOrderProductType, txtOrderProductDescription, txtOrderQuantity, txtOrderHarvestDate, txtOrderTotalAmount;
    Spinner spinnerOrderStatus;
    Button btnUpdateOrder, btnGotoChat;

    int orderId;
    int productChatMessageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        txtOrderCreationDate = findViewById(R.id.txt_order_creation_date);
        txtOrderBuyer = findViewById(R.id.txt_order_buyer);
        txtOrderProductType = findViewById(R.id.txt_order_product_type);
        txtOrderProductDescription = findViewById(R.id.txt_order_product_description);
        txtOrderQuantity = findViewById(R.id.txt_order_quantity);
        txtOrderHarvestDate = findViewById(R.id.txt_order_harvest_date);
        txtOrderTotalAmount = findViewById(R.id.txt_order_total_amount);
        spinnerOrderStatus = findViewById(R.id.spinner_order_status);

        btnUpdateOrder = findViewById(R.id.btn_update_order);
        btnGotoChat = findViewById(R.id.btn_goto_chat);

        ArrayAdapter<OrderStatus> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                OrderStatus.values()
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderStatus.setAdapter(statusAdapter);

        btnUpdateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrder();
            }
        });

        btnGotoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChat();
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
        txtOrderCreationDate.setText(getIntent().getStringExtra("orderDate"));
        txtOrderBuyer.setText(getIntent().getStringExtra("buyerName"));
        txtOrderProductType.setText(getIntent().getStringExtra("productTypeName"));
        txtOrderProductDescription.setText(getIntent().getStringExtra("productDescription"));
        txtOrderQuantity.setText(getIntent().getIntExtra("quantity",0) + "");
        txtOrderHarvestDate.setText(getIntent().getStringExtra("harvestDate"));
        txtOrderTotalAmount.setText(getIntent().getIntExtra("totalAmount",0) + "");

        // Set the order status
        OrderStatus orderStatus = OrderStatus.fromInt(getIntent().getIntExtra("status", 0));
        ArrayAdapter<OrderStatus> adapter = (ArrayAdapter<OrderStatus>) spinnerOrderStatus.getAdapter();
        int position = adapter.getPosition(orderStatus);
        spinnerOrderStatus.setSelection(position);
    }
    private void updateOrder() {
        boolean hasErrors = validate();

        if (!hasErrors) {
            UpdateOrderModel order = new UpdateOrderModel();
            order.orderId = orderId;
            order.quantity = Integer.parseInt(txtOrderQuantity.getText().toString());
            order.harvestDate = txtOrderHarvestDate.getText().toString();
            order.totalAmount = Double.parseDouble(txtOrderTotalAmount.getText().toString());
            order.status = ((OrderStatus) spinnerOrderStatus.getSelectedItem()).ordinal();

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
                        Toast.makeText(ActivityOrder.this, "Orden actualizada correctamente.", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(ActivityOrder.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<OrderResponse> call, Throwable t) {
                    Toast.makeText(ActivityOrder.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean validate() {
        boolean hasErrors = false;
        if (txtOrderQuantity.getText().toString().isEmpty()) {
            txtOrderQuantity.setError("Requerido");
            hasErrors = true;
        }

        if (txtOrderHarvestDate.getText().toString().isEmpty()) {
            txtOrderHarvestDate.setError("Requerido");
            hasErrors = true;
        }

        if (txtOrderTotalAmount.getText().toString().isEmpty()) {
            txtOrderTotalAmount.setError("Requerido");
            hasErrors = true;
        }

        return hasErrors;
    }

    private void goToChat() {
        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), ActivityOrder.this).getLoggedUser();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductChatApi productChatApi = retrofit.create(ProductChatApi.class);
        Call<AvailableChatUserDetailResponse> call = productChatApi.getMessageByUser(loggedInUser.getUserId(), productChatMessageId);
        call.enqueue(new Callback<AvailableChatUserDetailResponse>() {
            @Override
            public void onResponse(Call<AvailableChatUserDetailResponse> call, Response<AvailableChatUserDetailResponse> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), ChatDetailActivity.class);
                    intent.putExtra("userIdTo", response.body().userIdTo);
                    intent.putExtra("displayNameTo", response.body().displayNameTo);
                    intent.putExtra("imageUrl", response.body().imageUrl);

                    startActivity(intent);
                } else {
                    Toast.makeText(ActivityOrder.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AvailableChatUserDetailResponse> call, Throwable t) {
                Toast.makeText(ActivityOrder.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
