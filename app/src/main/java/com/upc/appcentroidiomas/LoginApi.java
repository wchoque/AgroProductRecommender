package com.upc.appcentroidiomas;

import com.upc.appcentroidiomas.data.model.LoginModel;
import com.upc.appcentroidiomas.data.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApi {
    @POST("login")
    //@Headers( "Content-Type: application/json" )
    Call<LoginResponse> login(@Body LoginModel loginModel);
}
