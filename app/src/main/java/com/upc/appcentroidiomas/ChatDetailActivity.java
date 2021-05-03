package com.upc.appcentroidiomas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.ChatInteface;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.LoggedInUser;
import com.upc.appcentroidiomas.data.model.NewMessageModel;
import com.upc.appcentroidiomas.data.model.NewMessageResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatDetailActivity extends AppCompatActivity {
    private int userIdTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }
        userIdTo = extras.getInt("userIdTo",0);

        TextView userChatDisplayName = findViewById(R.id.userChatDisplayName);
        TextView userChatNewContentMessage = findViewById(R.id.userChatNewContentMessage);


        Button btnSendNewMessage = findViewById(R.id.btnSendNewMessage);
        btnSendNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiContants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ChatInteface chatInteface  = retrofit.create(ChatInteface.class);
                LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), ChatDetailActivity.this).getLoggedUser();

                NewMessageModel newMessage = new NewMessageModel();
                newMessage.userIdFrom = loggedInUser.getUserId();
                newMessage.userIdTo = userIdTo;
                newMessage.messageContent = userChatNewContentMessage.getText().toString();

                Call<NewMessageResponse> call = chatInteface.SendNewMessage(newMessage);
                call.enqueue(new Callback<NewMessageResponse>() {
                    @ Override
                    public void onResponse(Call<NewMessageResponse> call, Response<NewMessageResponse> response) {
                        if (response.isSuccessful()){
                            userChatNewContentMessage.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<NewMessageResponse> call, Throwable t) {
                    }
                });

                Toast.makeText(ChatDetailActivity.this, "Mensaje enviado", Toast.LENGTH_LONG).show();
            }
        });

        Button btnUserChatBack = findViewById(R.id.btnUserChatBack);
        btnUserChatBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String displayNameTo = extras.getString("displayNameTo", "");
        userChatDisplayName.setText("Usuario: " + displayNameTo);
    }
}