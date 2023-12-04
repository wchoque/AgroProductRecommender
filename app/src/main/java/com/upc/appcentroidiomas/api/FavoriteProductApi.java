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

public interface FavoriteProductApi {

    @GET("favorite-products/list/{userId}")
    Call<ArrayList<ProductResponse>> getFavorites(@Path("userId") int userId);


    @POST("favorite-products/add")
    Call<ProductResponse> add(@Query("userId") int userId, @Query("productId") int productId);

    @POST("favorite-products/remove")
    Call<ProductResponse> remove(@Query("userId") int userId, @Query("productId") int productId);
}

