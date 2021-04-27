package com.upc.appcentroidiomas.data;

import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.LoginApi;
import com.upc.appcentroidiomas.data.model.LoggedInUser;
import com.upc.appcentroidiomas.data.model.LoginModel;
import com.upc.appcentroidiomas.data.model.LoginResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.UUID.randomUUID;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private LoginResponse loginResult;

    public Result<LoggedInUser> login(String username, String password) {
        try {
            // TODO: handle loggedInUser authentication

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiContants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            LoginApi loginApi = retrofit.create(LoginApi.class);

            LoginModel loginModel = new LoginModel(username, password);
            Call<LoginResponse> call = loginApi.login(loginModel);

           call.enqueue(new Callback<LoginResponse>() {
                @ Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    LoggedInUser fakeUser = new LoggedInUser(0, response.body().displayName);
                    //return fakeUser;
                    //return new LoggedInUser(fakeUser);
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    //return new Result.Error(new IOException("Error logging in"));
                    //  new Result.Error(new Exception("asdad"));
                    //Toast.makeText(this, "sssssssssssss", Toast.LENGTH_LONG);
                }
            });
            LoggedInUser fakeUser = new LoggedInUser(0, "asdasdas");
            return new Result.Success<>(fakeUser);

            //if (!username.equals("admin") && !password.equals("123456")){
            //    return new Result.Error(new Exception("Username or password are incorrect"));
            //}
            //String displayName = username;
            //LoggedInUser fakeUser = new LoggedInUser(randomUUID().toString(), displayName);
            //return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}