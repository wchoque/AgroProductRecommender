package com.upc.appcentroidiomas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.ui.login.LoginActivity;

public class ProfileActivity extends AppCompatActivity {
    TextView profileFirstName, profileLastName, profileEmail, profileDisplayName;
    Button btnUpdateAvatar, btnLogout;
    private LoginRepository loginRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        loginRepository = LoginRepository.getInstance(new LoginDataSource(), this.getApplicationContext());

        assignReferences();
    }

    private void assignReferences(){
        profileFirstName = findViewById(R.id.profileFirstName);
        profileLastName = findViewById(R.id.profileLastName);
        profileEmail = findViewById(R.id.profileEmail);
        profileDisplayName = findViewById(R.id.profileDisplayName);

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
                //finish();
              /*  Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                myIntent.putExtra("name", "asdasd"); //Optional parameters
                startActivity(myIntent);*/

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);        // Specify any activity here e.g. home or splash or login etc
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("EXIT", true);
                startActivity(i);
                finish();
            }
        });
    }

}