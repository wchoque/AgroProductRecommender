package com.upc.appcentroidiomas.api;

import com.upc.appcentroidiomas.data.model.UserInformationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChatInteface {3
    @GET("userinformation/{userId}")
    Call<UserInformationResponse> get(@Path("userId") int userId);
}
