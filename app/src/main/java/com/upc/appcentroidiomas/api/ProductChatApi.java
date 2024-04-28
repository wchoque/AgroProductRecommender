package com.upc.appcentroidiomas.api;

import com.upc.appcentroidiomas.data.model.HistoryChatResponse;
import com.upc.appcentroidiomas.data.model.NewMessageModel;
import com.upc.appcentroidiomas.data.model.NewMessageResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductChatApi {
    @POST("ProductChatMessage")
    Call<NewMessageResponse> SendNewMessage(@Body NewMessageModel newMessageModel);

    @GET("ProductChatMessage/GetHistoryChat/{userIdFrom}/{userIdTo}")
    Call<HistoryChatResponse> getHistoryChat(@Path("userIdFrom") int userIdFrom, @Path("userIdTo") int userIdTo);
}
