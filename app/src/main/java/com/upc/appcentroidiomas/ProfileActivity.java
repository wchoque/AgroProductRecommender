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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.LoginApi;
import com.upc.appcentroidiomas.api.UserApi;
import com.upc.appcentroidiomas.api.UserInformationApi;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.ChangePasswordModel;
import com.upc.appcentroidiomas.data.model.LoggedInUser;
import com.upc.appcentroidiomas.data.model.LoginResponse;
import com.upc.appcentroidiomas.data.model.UserInformationModel;
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
    TextView profileFirstName, profileLastName, profileEmail, profileDisplayName, profilePhoneNumber, profileGender, profileBio, profileWebpageUrl, profileDni;
    ImageView profileAvatar;
    TextView profileCurrentPassword, profileNewPassword;
    Button btnUpdateProfile, btnChangePassword, btnLogout;
    private LoginRepository loginRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        loginRepository = LoginRepository.getInstance(new LoginDataSource(), this.getApplicationContext());

        LoggedInUser loggedInUser = loginRepository.getLoggedUser();

        assignReferences();
        getProfileInformation(loggedInUser.getUserId(), loggedInUser.getDisplayName());
    }

    private void assignReferences(){
        profileAvatar = findViewById(R.id.profileAvatar);
        profileFirstName = findViewById(R.id.profileFirstName);
        profileLastName = findViewById(R.id.profileLastName);
        profileDisplayName = findViewById(R.id.profileDisplayName);
        profileEmail = findViewById(R.id.profileEmail);
        profilePhoneNumber = findViewById(R.id.profilePhoneNumber);
        profileGender = findViewById(R.id.profileGender);
        profileBio = findViewById(R.id.profileBio);
        profileWebpageUrl = findViewById(R.id.profileWebpageUrl);
        profileDni = findViewById(R.id.profileDni);

        profileCurrentPassword = findViewById(R.id.profileCurrentPassword);
        profileNewPassword = findViewById(R.id.profileNewPassword);

        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoggedInUser loggedInUser = loginRepository.getLoggedUser();
                UserInformationModel userInformationModel = new UserInformationModel();
                userInformationModel.firstName = profileFirstName.getText().toString();
                userInformationModel.lastName = profileLastName.getText().toString();
                userInformationModel.email = profileEmail.getText().toString();
                userInformationModel.phoneNumber = profilePhoneNumber.getText().toString();

                String genderText = profileGender.getText().toString();
                int gender;
                if (genderText.equalsIgnoreCase("Masculino")){
                    gender = 1;
                }else{
                    gender=0;
                }

                userInformationModel.gender = gender;
                userInformationModel.bio = profileBio.getText().toString();
                userInformationModel.webpageUrl = profileWebpageUrl.getText().toString();
                userInformationModel.dni = profileDni.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiContants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                UserApi userApi = retrofit.create(UserApi.class);
                Call<UserInformationResponse> call = userApi.updateProfile(loggedInUser.getUserId(), userInformationModel);

                call.enqueue(new Callback<UserInformationResponse>() {
                    @ Override
                    public void onResponse(Call<UserInformationResponse> call, Response<UserInformationResponse> response) {
                        if (response.isSuccessful()){
                            new DownloadImageTask(profileAvatar)
                                    .execute(response.body().imageUrl);

                            /*profileFirstName.setText("Nombres: " + response.body().firstName);
                            profileLastName.setText("Apellidos: " + response.body().lastName);
                            profileEmail.setText("Email: " + response.body().email);
                            profilePhoneNumber.setText("Telefono: " + response.body().phoneNumber);

                            //TODO convert to masculino or dfemenio
                            int gender = response.body().gender;
                            String genderText= "";
                            if (gender == 1){
                                genderText = "Masculino";
                            }else{
                                genderText = "Femenino";
                            }

                            profileGender.setText("Genero: " + genderText);
                            profileBio.setText("Bio: " + response.body().bio);
                            profileWebpageUrl.setText("Pagina Web: " + response.body().webpageUrl);
                            profileDni.setText("DNI: " + response.body().dni);*/

                            profileFirstName.setText(response.body().firstName);
                            profileLastName.setText(response.body().lastName);
                            profileEmail.setText(response.body().email);
                            profilePhoneNumber.setText(response.body().phoneNumber);

                            int gender = response.body().gender;
                            String genderText= "";
                            if (gender == 1){
                                genderText = "Masculino";
                            }else{
                                genderText = "Femenino";
                            }

                            profileGender.setText(genderText);
                            profileBio.setText(response.body().bio);
                            profileWebpageUrl.setText(response.body().webpageUrl);
                            profileDni.setText(response.body().dni);
                            Toast.makeText(ProfileActivity.this, "Sus datos se han actualizado correctamente", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(ProfileActivity.this, "No se pudo actualizar la información del perfil", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInformationResponse> call, Throwable t) {
                        Toast.makeText(ProfileActivity.this, "No se pudo actualizar la información del perfil", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiContants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                LoggedInUser loggedInUser = loginRepository.getLoggedUser();
                ChangePasswordModel changePasswordModel = new ChangePasswordModel();
                changePasswordModel.setUserName(loggedInUser.getUserName());
                changePasswordModel.setCurrentPassword(profileCurrentPassword.getText().toString());
                changePasswordModel.setNewPassword(profileNewPassword.getText().toString());

                LoginApi loginApi = retrofit.create(LoginApi.class);
                Call<LoginResponse> call = loginApi.changePassword(changePasswordModel);

                call.enqueue(new Callback<LoginResponse>() {
                    @ Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()){
                            profileCurrentPassword.setText("");
                            profileNewPassword.setText("");

                            Toast.makeText(ProfileActivity.this, "Su contraseña fue actualizada correctamente.", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ProfileActivity.this, "Hubo un error al intentar cambiar la contraseña, verifique que los datos sean correctos", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(ProfileActivity.this, "Hubo un error al intentar cambiar la contraseña, verifique que los datos sean correctos", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //user logout
                loginRepository.logout();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getProfileInformation(int userId, String displayName){
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
                    new DownloadImageTask(profileAvatar)
                            .execute(response.body().imageUrl);

                    /*
                      profileFirstName.setText("Nombres: " + response.body().firstName);
                    profileLastName.setText("Apellidos: " + response.body().lastName);
                    profileDisplayName.setText("Apodo: " + displayName);
                    profileEmail.setText("Email: " + response.body().email);
                    profilePhoneNumber.setText("Telefono: " + response.body().phoneNumber);

                    //TODO convert to masculino or dfemenio
                    int gender = response.body().gender;
                    String genderText= "";
                    if (gender == 1){
                        genderText = "Masculino";
                    }else{
                        genderText = "Femenino";
                    }

                    profileGender.setText("Genero: " + genderText);
                    profileBio.setText("Bio: " + response.body().bio);
                    profileWebpageUrl.setText("Pagina Web: " + response.body().webpageUrl);
                    profileDni.setText("DNI: " + response.body().dni);
                    */
                    profileFirstName.setText(response.body().firstName);
                    profileLastName.setText(response.body().lastName);
                    profileDisplayName.setText(displayName);
                    profileEmail.setText(response.body().email);
                    profilePhoneNumber.setText(response.body().phoneNumber);

                    // convert to masculino or dfemenio
                    int gender = response.body().gender;
                    String genderText= "";
                    if (gender == 1){
                        genderText = "Masculino";
                    }else{
                        genderText = "Femenino";
                    }

                    profileGender.setText(genderText);
                    profileBio.setText(response.body().bio);
                    profileWebpageUrl.setText(response.body().webpageUrl);
                    profileDni.setText(response.body().dni);
                }
            }

            @Override
            public void onFailure(Call<UserInformationResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "No se pudo obtener la información del perfil", Toast.LENGTH_SHORT).show();
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