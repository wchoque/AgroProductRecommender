package com.upc.appcentroidiomas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.OrderApi;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.OrderResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderFragment extends Fragment {
    private List<OrderResponse> orders;
    private OrderAdapter orderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_order, container, false);
        super.onCreate(savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewOrders);

        orders = new ArrayList<>();

        orderAdapter = new OrderAdapter(orders, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(orderAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override // Un toque sencillo
            public void onClick(View view, int position) {
                OrderResponse orderResponse  = orders.get(position);
                Intent intent = new Intent(getContext(), ActivityOrder.class);

                intent.putExtra("orderId", orderResponse.orderId);
                intent.putExtra("productChatMessageId", orderResponse.productChatMessageId);
                intent.putExtra("status", orderResponse.status);
                intent.putExtra("buyerName", orderResponse.buyerName);
                intent.putExtra("productTypeName", orderResponse.productTypeName);
                intent.putExtra("productDescription", orderResponse.productDescription);
                intent.putExtra("quantity", orderResponse.quantity);
                intent.putExtra("orderDate", orderResponse.orderDate);
                intent.putExtra("harvestDate", orderResponse.harvestDate);
                intent.putExtra("totalAmount", orderResponse.totalAmount);
                startActivity(intent);
            }

            @Override // Un toque largo
            public void onLongClick(View view, int position) {

            }
        }));

        refreshOrdersList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshOrdersList();
    }

    public void refreshOrdersList() {
        if (orderAdapter == null) return;

        // can be launched in a separate asynchronous job
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource(), getContext());

        OrderApi orderApi = retrofit.create(OrderApi.class);
        Call<ArrayList<OrderResponse>> call = orderApi.get(loginRepository.getLoggedUser().getUserId());

        call.enqueue(new Callback<ArrayList<OrderResponse>>() {
            @ Override
            public void onResponse(Call<ArrayList<OrderResponse>> call, Response<ArrayList<OrderResponse>> response) {
                if (response.isSuccessful()){
                    orders = response.body();
                    orderAdapter.setOrders(orders);
                    orderAdapter.notifyDataSetChanged();

                    if (orders.size() == 0){
                        Toast.makeText(getContext(), "No se encontraron ordenes de compra", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<OrderResponse>> call, Throwable t) {

            }
        });
    }
}