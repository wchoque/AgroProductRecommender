package com.upc.appcentroidiomas.api;

import com.upc.appcentroidiomas.data.model.AvailableChatUserResponse;
import com.upc.appcentroidiomas.data.model.NewMessageModel;
import com.upc.appcentroidiomas.data.model.NewMessageResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatInteface {
    @POST("ChatMessage")
    Call<NewMessageResponse> SendNewMessage(@Body NewMessageModel loginModel);

    @GET("ChatMessage/GetHistoryChat/{userIdFrom}/{userIdTo}")
    Call<AvailableChatUserResponse> get(@Path("userIdFrom") int userIdFrom, @Path("userIdTo") int userIdTo);
}
