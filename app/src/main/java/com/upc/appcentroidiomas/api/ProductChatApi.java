package com.upc.appcentroidiomas.api;

import com.upc.appcentroidiomas.data.model.AvailableChatUserDetailResponse;
import com.upc.appcentroidiomas.data.model.ChatMessageResponse;
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

    @GET("ProductChatMessage/GetMessages/{userIdFrom}/{userIdTo}")
    Call<ChatMessageResponse> getMessages(@Path("userIdFrom") int userIdFrom, @Path("userIdTo") int userIdTo);

    @GET("ProductChatMessage/GetMessageByUser/{userId}/{productChatMessageId}")
    Call<AvailableChatUserDetailResponse> getMessageByUser(@Path("userId") int userId, @Path("productChatMessageId") int productChatMessageId);
}
