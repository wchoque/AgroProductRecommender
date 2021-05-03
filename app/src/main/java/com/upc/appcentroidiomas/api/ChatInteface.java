package com.upc.appcentroidiomas.api;

import com.upc.appcentroidiomas.data.model.NewMessageModel;
import com.upc.appcentroidiomas.data.model.NewMessageResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatInteface {
    @POST("ChatMessage")
    Call<NewMessageResponse> SendNewMessage(@Body NewMessageModel loginModel);
}
