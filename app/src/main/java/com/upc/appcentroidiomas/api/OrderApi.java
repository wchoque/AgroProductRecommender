package com.upc.appcentroidiomas.api;

import com.upc.appcentroidiomas.data.model.OrderResponse;
import com.upc.appcentroidiomas.data.model.UpdateOrderModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderApi {
    @GET("orders/users/{userId}")
    Call<ArrayList<OrderResponse>> get(@Path("userId") int userId);
    @PUT("orders/{id}")
    Call<OrderResponse> update(@Path("id") int orderId, @Body UpdateOrderModel updateOrderModel);
}