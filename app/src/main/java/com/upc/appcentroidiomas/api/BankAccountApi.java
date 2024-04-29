package com.upc.appcentroidiomas.api;

import com.upc.appcentroidiomas.data.model.BankAccountResponse;
import com.upc.appcentroidiomas.data.model.NewProductModel;
import com.upc.appcentroidiomas.data.model.ProductResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BankAccountApi {
    @GET("BankAccounts/GetBankAccounts/{userId}")
    Call<ArrayList<BankAccountResponse>> get(@Path("userId") int userId);
    @POST("BankAccounts")
    Call<BankAccountResponse> create(@Body BankAccountResponse model);

    @PUT("BankAccounts/{id}")
    Call<BankAccountResponse> update(@Path("id") int id, @Body BankAccountResponse model);
}