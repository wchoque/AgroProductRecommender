package com.upc.appcentroidiomas.api;

import com.upc.appcentroidiomas.data.model.AvailableChatUserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AvailableProductChatUserApi {
    @GET("ProductChatMessage/GetMessagesByUserId/{userId}")
    Call<AvailableChatUserResponse> get(@Path("userId") int userId);
    @GET("ProductChatMessage/GetMessagesByUserId/{userId}/offers")
    Call<AvailableChatUserResponse> getOffers(@Path("userId") int userId);
}
