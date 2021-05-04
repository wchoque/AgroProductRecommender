package com.upc.appcentroidiomas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.UserInformationApi;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.LoggedInUser;
import com.upc.appcentroidiomas.data.model.UserInformationResponse;
import com.upc.appcentroidiomas.ui.login.LoginActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {
    TextView profileFirstName, profileLastName, profileEmail, profileDisplayName;
    ImageView profileAvatar;
    Button btnUpdateAvatar, btnLogout;
    private LoginRepository loginRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        loginRepository = LoginRepository.getInstance(new LoginDataSource(), this.getApplicationContext());

        LoggedInUser loggedInUser = loginRepository.getLoggedUser();

        assignReferences();
        updateProfileInformation(loggedInUser.getUserId(), loggedInUser.getDisplayName());
    }

    private void assignReferences(){
        profileFirstName = findViewById(R.id.profileFirstName);
        profileLastName = findViewById(R.id.profileLastName);
        profileEmail = findViewById(R.id.profileEmail);
        profileDisplayName = findViewById(R.id.profileDisplayName);
        profileAvatar = findViewById(R.id.profileAvatar);

        btnUpdateAvatar = findViewById(R.id.btnUpdateAvatar);
        btnUpdateAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //user logout
                loginRepository.logout();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);        // Specify any activity here e.g. home or splash or login etc
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateProfileInformation(int userId, String displayName){
        // can be launched in a separate asynchronous job
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserInformationApi userInformationApi = retrofit.create(UserInformationApi.class);
        Call<UserInformationResponse> call = userInformationApi.get(userId);

        call.enqueue(new Callback<UserInformationResponse>() {
            @ Override
            public void onResponse(Call<UserInformationResponse> call, Response<UserInformationResponse> response) {
                if (response.isSuccessful()){
                    //Set info to text boxes
                    new DownloadImageTask(profileAvatar)
                            .execute(response.body().imageUrl);

                    profileFirstName.setText("Nombres: " + response.body().firstName);
                    profileLastName.setText("Apellidos: " +response.body().lastName);
                    profileEmail.setText("Email: " +response.body().email);
                    profileDisplayName.setText("Apodo: " + displayName);
                }
            }

            @Override
            public void onFailure(Call<UserInformationResponse> call, Throwable t) {

            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}