package com.upc.appcentroidiomas.api;

import com.upc.appcentroidiomas.data.model.InvoiceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface InvoiceApi {
    @GET("Invoice/GetInvoiceByUserId/{userId}")
    Call<InvoiceResponse> get(@Path("userId") int userId);
}
