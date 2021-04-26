package com.upc.appcentroidiomas.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.upc.appcentroidiomas.LoginApi;
import com.upc.appcentroidiomas.data.ApiContants;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.LoggedInUser;
import com.upc.appcentroidiomas.R;
import com.upc.appcentroidiomas.data.model.LoginModel;
import com.upc.appcentroidiomas.data.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.UUID.randomUUID;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job

        //Result<LoggedInUser> result = loginRepository.login(username, password);

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
                if (response.isSuccessful()){
                    LoggedInUser loggedInUser = new LoggedInUser(randomUUID().toString(), response.body().displayName);
                    loginRepository.forceLogin(loggedInUser);
                    loginResult.setValue(new LoginResult(new LoggedInUserView(loggedInUser.getDisplayName())));
                } else {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }
                //return fakeUser;
                //return new LoggedInUser(fakeUser);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //return new Result.Error(new IOException("Error logging in"));
                //  new Result.Error(new Exception("asdad"));
                //Toast.makeText(this, "sssssssssssss", Toast.LENGTH_LONG)
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}