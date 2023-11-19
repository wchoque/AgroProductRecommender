package com.upc.appcentroidiomas.api;

import com.upc.appcentroidiomas.data.model.NewMessageModel;
import com.upc.appcentroidiomas.data.model.NewMessageResponse;
import com.upc.appcentroidiomas.data.model.NewProductModel;
import com.upc.appcentroidiomas.data.model.ProductImageListResponse;
import com.upc.appcentroidiomas.data.model.ProductResponse;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApi {
    @POST("products")
    Call<ProductResponse> create(@Body NewProductModel model);

    @PUT("products/{id}")
    Call<ProductResponse> update(@Path("id") int id, @Body NewProductModel model);

    @GET("products/filtered-by-user")
    Call<ArrayList<ProductResponse>> getFilteredProducts(@Query("userId") int userId, @Query("description") String description);

    @GET("products/{id}/get-images")
    Call<ProductImageListResponse> getImages(@Path("id") int id);

    @DELETE("products/{id}")
    Call<ResponseBody> delete(@Path("id") int id);

    @POST("")
    Call<NewMessageResponse> sendNewMessage(@Body NewMessageModel loginModel);
}

