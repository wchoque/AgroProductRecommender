package com.upc.appcentroidiomas.api;

import com.upc.appcentroidiomas.data.model.CourseDetailedResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NotaApi {
    @GET("Student/{userId}/{courseId}/GetDetailedCourse")
    Call<CourseDetailedResponse> get(@Path("userId") int userId, @Path("courseId") int courseId);
}
