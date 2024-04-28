package com.upc.appcentroidiomas;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
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
import com.upc.appcentroidiomas.utils.AndroidUtil;

import java.io.File;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragmentNew extends Fragment {
    TextView profileFirstName, profileLastName, profileEmail, profileDisplayName, profilePhoneNumber, profileGender, profileBio, profileWebpageUrl, profileDni;
    ImageView profileAvatar;
    TextView profileCurrentPassword, profileNewPassword;
    Button btnUpdateProfile, btnChangePassword, btnLogout;

    ProgressBar btnUpdateProfileProgressBar, btnChangePasswordProgressBar;

    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;

    private LoginRepository loginRepository;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            AndroidUtil.setProfilePic(getContext(), selectedImageUri, profileAvatar);
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        loginRepository = LoginRepository.getInstance(new LoginDataSource(), getContext());
        LoggedInUser loggedInUser = loginRepository.getLoggedUser();

        View view = inflater.inflate(R.layout.activity_profile, container, false);

        assignReferences(view);
        getProfileInformation(loggedInUser.getUserId(), loggedInUser.getDisplayName());

        super.onCreate(savedInstanceState);

        return view;
    }

    private void assignReferences(View view) {
        profileAvatar = view.findViewById(R.id.profileAvatar);
        profileAvatar.setOnClickListener((v) -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });

        profileFirstName = view.findViewById(R.id.profileFirstName);
        profileLastName = view.findViewById(R.id.profileLastName);
        profileDisplayName = view.findViewById(R.id.profileDisplayName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profilePhoneNumber = view.findViewById(R.id.profilePhoneNumber);
        profileGender = view.findViewById(R.id.profileGender);
        profileBio = view.findViewById(R.id.profileBio);
        profileWebpageUrl = view.findViewById(R.id.profileWebpageUrl);
        profileDni = view.findViewById(R.id.profileDni);

        profileCurrentPassword = view.findViewById(R.id.profileCurrentPassword);
        profileNewPassword = view.findViewById(R.id.profileNewPassword);

        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInProgressUpdateProfile(true);

                LoggedInUser loggedInUser = loginRepository.getLoggedUser();
                UserInformationModel userInformationModel = new UserInformationModel();
                userInformationModel.firstName = profileFirstName.getText().toString();
                userInformationModel.lastName = profileLastName.getText().toString();
                userInformationModel.email = profileEmail.getText().toString();
                userInformationModel.phoneNumber = profilePhoneNumber.getText().toString();

                String genderText = profileGender.getText().toString();
                int gender;
                if (genderText.equalsIgnoreCase("Masculino")) {
                    gender = 1;
                } else {
                    gender = 0;
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

                RequestBody requestBodyFirstName = RequestBody.create(MediaType.parse("text/plain"), userInformationModel.firstName);
                RequestBody requestBodyLastName = RequestBody.create(MediaType.parse("text/plain"), userInformationModel.lastName);
                RequestBody requestBodyEmail = RequestBody.create(MediaType.parse("text/plain"), userInformationModel.email);
                RequestBody requestBodyPhoneNumber = RequestBody.create(MediaType.parse("text/plain"), userInformationModel.phoneNumber);
                RequestBody requestBodyGender = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userInformationModel.gender));
                RequestBody requestBodyBio = RequestBody.create(MediaType.parse("text/plain"), userInformationModel.bio);
                RequestBody requestBodyWebpageUrl = RequestBody.create(MediaType.parse("text/plain"), userInformationModel.webpageUrl);
                RequestBody requestBodyDni = RequestBody.create(MediaType.parse("text/plain"), userInformationModel.dni);

                MultipartBody.Part filePart = null;
                // Preparar el archivo si existe
                if (selectedImageUri != null){
                    File file = new File(selectedImageUri.getPath());
                    if (file.exists()) {
                        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
                        filePart = MultipartBody.Part.createFormData("profilePicture", file.getName(), fileBody);
                    }
                }

                Call<UserInformationResponse> call = userApi.updateProfile(
                        loggedInUser.getUserId(),
                        requestBodyFirstName,
                        requestBodyLastName,
                        requestBodyEmail,
                        requestBodyPhoneNumber,
                        requestBodyGender,
                        requestBodyBio,
                        requestBodyWebpageUrl,
                        requestBodyDni,
                        filePart
                );

                call.enqueue(new Callback<UserInformationResponse>() {
                    @Override
                    public void onResponse(Call<UserInformationResponse> call, Response<UserInformationResponse> response) {
                        if (response.isSuccessful()) {
                            AndroidUtil.setProfilePic(getContext(), Uri.parse(response.body().imageUrl), profileAvatar);

                            profileFirstName.setText(response.body().firstName);
                            profileLastName.setText(response.body().lastName);
                            profileEmail.setText(response.body().email);
                            profilePhoneNumber.setText(response.body().phoneNumber);

                            int gender = response.body().gender;
                            String genderText = "";
                            if (gender == 1) {
                                genderText = "Masculino";
                            } else {
                                genderText = "Femenino";
                            }

                            profileGender.setText(genderText);
                            profileBio.setText(response.body().bio);
                            profileWebpageUrl.setText(response.body().webpageUrl);
                            profileDni.setText(response.body().dni);
                            Toast.makeText(getContext(), "Sus datos se han actualizado correctamente", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "No se pudo actualizar la información del perfil", Toast.LENGTH_LONG).show();
                        }
                        setInProgressUpdateProfile(false);
                    }

                    @Override
                    public void onFailure(Call<UserInformationResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "No se pudo actualizar la información del perfil", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnUpdateProfileProgressBar = view.findViewById(R.id.btnUpdateProfileProgressBar);

        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInProgressChangePassword(true);

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
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            profileCurrentPassword.setText("");
                            profileNewPassword.setText("");

                            Toast.makeText(getContext(), "Su contraseña fue actualizada correctamente.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Hubo un error al intentar cambiar la contraseña, verifique que los datos sean correctos", Toast.LENGTH_LONG).show();
                        }
                        setInProgressChangePassword(false);
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Hubo un error al intentar cambiar la contraseña, verifique que los datos sean correctos", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        btnChangePasswordProgressBar = view.findViewById(R.id.btnChangePasswordProgressBar);

        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setVisibility(View.GONE);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //user logout
                loginRepository.logout();

                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                //finish();
            }
        });
    }

    private void getProfileInformation(int userId, String displayName) {
        setInProgressUpdateProfile(true);
        setInProgressChangePassword(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserInformationApi userInformationApi = retrofit.create(UserInformationApi.class);
        Call<UserInformationResponse> call = userInformationApi.get(userId);

        call.enqueue(new Callback<UserInformationResponse>() {
            @Override
            public void onResponse(Call<UserInformationResponse> call, Response<UserInformationResponse> response) {
                if (response.isSuccessful()) {
                    AndroidUtil.setProfilePic(getContext(), Uri.parse(response.body().imageUrl), profileAvatar);

                    profileFirstName.setText(response.body().firstName);
                    profileLastName.setText(response.body().lastName);
                    profileDisplayName.setText(displayName);
                    profileEmail.setText(response.body().email);
                    profilePhoneNumber.setText(response.body().phoneNumber);

                    // convert to masculino or dfemenio
                    int gender = response.body().gender;
                    String genderText = "";
                    if (gender == 1) {
                        genderText = "Masculino";
                    } else {
                        genderText = "Femenino";
                    }

                    profileGender.setText(genderText);
                    profileBio.setText(response.body().bio);
                    profileWebpageUrl.setText(response.body().webpageUrl);
                    profileDni.setText(response.body().dni);
                }
                setInProgressUpdateProfile(false);
                setInProgressChangePassword(false);
            }

            @Override
            public void onFailure(Call<UserInformationResponse> call, Throwable t) {
                Toast.makeText(getContext(), "No se pudo obtener la información del perfil", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setInProgressUpdateProfile(boolean inProgress){
        if(inProgress){
            btnUpdateProfileProgressBar.setVisibility(View.VISIBLE);
            btnUpdateProfile.setVisibility(View.GONE);
        }else{
            btnUpdateProfileProgressBar.setVisibility(View.GONE);
            btnUpdateProfile.setVisibility(View.VISIBLE);
        }
    }

    void setInProgressChangePassword(boolean inProgress){
        if(inProgress){
            btnChangePasswordProgressBar.setVisibility(View.VISIBLE);
            btnChangePassword.setVisibility(View.GONE);
        }else{
            btnChangePasswordProgressBar.setVisibility(View.GONE);
            btnChangePassword.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}