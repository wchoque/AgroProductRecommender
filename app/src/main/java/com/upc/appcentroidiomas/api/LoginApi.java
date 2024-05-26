package com.upc.appcentroidiomas.api;

import com.upc.appcentroidiomas.data.model.ChangePasswordModel;
import com.upc.appcentroidiomas.data.model.LoginModel;
import com.upc.appcentroidiomas.data.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginApi {
    @POST("login")
    //@Headers( "Content-Type: application/json" )
    Call<LoginResponse> login(@Body LoginModel loginModel);

    @POST("login/change-password")
        //@Headers( "Content-Type: application/json" )
    Call<LoginResponse> changePassword(@Body ChangePasswordModel model);

    @GET("login/users/{userId}")
    Call<LoginResponse> getLoggedUser(@Path("userId") int userId);
}
