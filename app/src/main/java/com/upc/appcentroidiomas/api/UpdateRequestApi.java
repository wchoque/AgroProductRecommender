package com.upc.appcentroidiomas.api;

import com.upc.appcentroidiomas.data.model.RejectChangesModel;
import com.upc.appcentroidiomas.data.model.UpdateRequestDetailResponse;
import com.upc.appcentroidiomas.data.model.UpdateRequestResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UpdateRequestApi {
    @GET("admin/update-requests")
    Call<ArrayList<UpdateRequestResponse>> get();
    @POST("admin/update-requests/users/{userId}/approve")
    Call<Void> approve(@Path("userId") int userId);
    @POST("admin/update-requests/users/{userId}/reject")
    Call<Void> reject(@Path("userId") int userId, @Body RejectChangesModel comment);
    @GET("admin/update-requests/users/{userId}")
    Call<ArrayList<UpdateRequestDetailResponse>> getByUser(@Path("userId") int userId);
}