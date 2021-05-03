package com.upc.appcentroidiomas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.NotaApi;
import com.upc.appcentroidiomas.api.UserInformationApi;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.CourseDetailedResponse;
import com.upc.appcentroidiomas.data.model.LoggedInUser;
import com.upc.appcentroidiomas.data.model.Nota;
import com.upc.appcentroidiomas.data.model.UserInformationResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotaActivity extends AppCompatActivity {
    private static int courseId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);
        getNotas();

        courseId = 1;
    }
    private void getNotas(){
        // can be launched in a separate asynchronous job
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this.getApplicationContext()).getLoggedUser();

        NotaApi notaApi = retrofit.create(NotaApi.class);
        Call<CourseDetailedResponse> call = notaApi.get(loggedInUser.getUserId(), courseId);

        call.enqueue(new Callback<CourseDetailedResponse>() {
            @ Override
            public void onResponse(Call<CourseDetailedResponse> call, Response<CourseDetailedResponse> response) {
                if (response.isSuccessful()){
                    //Set info to text boxes

                    CourseDetailedResponse _response = response.body();
                    for (Nota nota : _response.notes) {
                    }
                }
            }

            @Override
            public void onFailure(Call<CourseDetailedResponse> call, Throwable t) {

            }
        });
    }
}