package com.upc.appcentroidiomas.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.LoginApi;
import com.upc.appcentroidiomas.api.UserApi;
import com.upc.appcentroidiomas.data.model.LoggedInUser;
import com.upc.appcentroidiomas.data.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;
    private Context context;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;
    private SharedPreferences Loginprefs;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource, Context context)  {
        this.dataSource = dataSource;
        this.context = context;

        Loginprefs = context.getSharedPreferences("logindetail", 0);
    }

    public static LoginRepository getInstance(LoginDataSource dataSource, Context context) {
        if (instance == null) {
            instance = new LoginRepository(dataSource, context);
            //activity = new MainActivity();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        //return user != null;
        //Loginprefs = activity.getApplicationContext().getSharedPreferences("logindetail", 0);

        String userLoginStatus = Loginprefs.getString("userLoginStatus", null);
        if (userLoginStatus == null){
            return false;
        }
        return userLoginStatus.toString().equals("yes");
    }

    public void logout() {
        user = null;
        //dataSource.logout();
        //prefs = getSharedPreferences("logindetail", 0);
        SharedPreferences.Editor edit = Loginprefs.edit();
        edit.clear();
        edit.apply();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;

        //prefs = getSharedPreferences("logindetail", 0);
        SharedPreferences.Editor edit = Loginprefs.edit();
        edit.putString("userId", Integer.toString(user.getUserId()));
        edit.putString("userName", user.getUserName());
        edit.putString("email", user.getEmail());
        edit.putInt("userType", user.getUserType());
        edit.putString("displayName", user.getDisplayName());
        edit.putString("profileImageUrl", user.getProfileImageUrl());
        edit.putInt("userAccountStatus", user.getUserAccountStatus());
        edit.putString("userLoginStatus", "yes");
        edit.apply();
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public LoggedInUser getLoggedUser() {
        String userId = Loginprefs.getString("userId", null);
        String userName = Loginprefs.getString("userName", null);
        String email = Loginprefs.getString("email", null);
        int userType = Loginprefs.getInt("userType", 0);
        String displayName = Loginprefs.getString("displayName", null);
        String profileImageUrl = Loginprefs.getString("profileImageUrl", null);
        int userAccountStatus = Loginprefs.getInt("userAccountStatus", 0);

        if (userId == null){
            this.user = new LoggedInUser(0, userName, displayName, email, 0, profileImageUrl, 0);
        }else{
            this.user = new LoggedInUser(Integer.parseInt(userId), userName, displayName, email, userType, profileImageUrl, userAccountStatus);
        }

        return this.user;
    }

    public Result<LoggedInUser> login(String username, String password) {
        // handle login
        Result<LoggedInUser> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }

    public void forceLogin(LoggedInUser loggedInUser) {
        setLoggedInUser(loggedInUser);
    }

    public interface RefreshUserCallback {
        void onRefresh(LoggedInUser updatedUser);
        void onFailure(Exception e);
    }

    public void refreshLoggedUser(RefreshUserCallback callback) {
        // Call the API to get the updated user data
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginApi loginApi = retrofit.create(LoginApi.class);
        Call<LoginResponse> call = loginApi.getLoggedUser(user.getUserId());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoggedInUser loggedInUser = new LoggedInUser(response.body().id, response.body().userName, response.body().displayName, response.body().email, response.body().userType, response.body().profileImageUrl, response.body().userAccountStatus);
                    setLoggedInUser(loggedInUser);
                    callback.onRefresh(loggedInUser);
                } else {
                    callback.onFailure(new Exception("Failed to refresh user data"));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

}