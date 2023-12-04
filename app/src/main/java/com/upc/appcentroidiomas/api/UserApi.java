package com.upc.appcentroidiomas.api;

import com.upc.appcentroidiomas.data.model.UserInformationModel;
import com.upc.appcentroidiomas.data.model.UserInformationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
    @POST("users/{userId}/update-profile")
    Call<UserInformationResponse> updateProfile(@Path("userId") int userId, @Body UserInformationModel model);
}
