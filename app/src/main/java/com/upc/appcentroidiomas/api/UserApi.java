package com.upc.appcentroidiomas.api;

import com.upc.appcentroidiomas.data.model.UserInformationModel;
import com.upc.appcentroidiomas.data.model.UserInformationResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserApi {
    @Multipart
    @POST("users/{userId}/update-profile")
    Call<UserInformationResponse> updateProfile(
            @Path("userId") int userId,
            @Part("firstName") RequestBody firstName,
            @Part("lastName") RequestBody lastName,
            @Part("email") RequestBody email,
            @Part("phoneNumber") RequestBody phoneNumber,
            @Part("gender") RequestBody gender,
            @Part("bio") RequestBody bio,
            @Part("webpageUrl") RequestBody webpageUrl,
            @Part("dni") RequestBody dni,
            @Part MultipartBody.Part profilePicture
    );
}
