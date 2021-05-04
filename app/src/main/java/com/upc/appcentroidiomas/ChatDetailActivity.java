package com.upc.appcentroidiomas;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.AvailableChatUserApi;
import com.upc.appcentroidiomas.api.ChatApi;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.AvailableChatUserResponse;
import com.upc.appcentroidiomas.data.model.HistoryChatResponse;
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
    TextView userChatHistoryMessages;
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
        //String historyMessages = extras.getString("historyMessages","");

        TextView userChatDisplayName = findViewById(R.id.userChatDisplayName);
        userChatHistoryMessages = findViewById(R.id.userChatHistoryMessages);
        TextView userChatNewContentMessage = findViewById(R.id.userChatNewContentMessage);

        userChatHistoryMessages.setMovementMethod(new ScrollingMovementMethod());


        // can be launched in a separate asynchronous job
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this.getApplicationContext()).getLoggedUser();

        ChatApi chatApi = retrofit.create(ChatApi.class);
        Call<HistoryChatResponse> call = chatApi.getHistoryChat(loggedInUser.getUserId(), userIdTo);

        call.enqueue(new Callback<HistoryChatResponse>() {
            @ Override
            public void onResponse(Call<HistoryChatResponse> call, Response<HistoryChatResponse> response) {
                if (response.isSuccessful()){
                    userChatHistoryMessages.setText(response.body().historyMessages);
                }
            }

            @Override
            public void onFailure(Call<HistoryChatResponse> call, Throwable t) {

            }
        });


        Button btnSendNewMessage = findViewById(R.id.btnSendNewMessage);
        btnSendNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiContants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ChatApi chatApi = retrofit.create(ChatApi.class);
                LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), ChatDetailActivity.this).getLoggedUser();

                NewMessageModel newMessage = new NewMessageModel();
                newMessage.userIdFrom = loggedInUser.getUserId();
                newMessage.userIdTo = userIdTo;
                newMessage.messageContent = userChatNewContentMessage.getText().toString();

                Call<NewMessageResponse> call = chatApi.SendNewMessage(newMessage);
                call.enqueue(new Callback<NewMessageResponse>() {
                    @ Override
                    public void onResponse(Call<NewMessageResponse> call, Response<NewMessageResponse> response) {
                        if (response.isSuccessful()){
                            userChatNewContentMessage.setText("");
                            refreshHistoryChat();
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

    public void refreshHistoryChat() {
        // can be launched in a separate asynchronous job
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this.getApplicationContext()).getLoggedUser();

        ChatApi chatApi = retrofit.create(ChatApi.class);
        Call<HistoryChatResponse> call = chatApi.getHistoryChat(loggedInUser.getUserId(), userIdTo);

        call.enqueue(new Callback<HistoryChatResponse>() {
            @ Override
            public void onResponse(Call<HistoryChatResponse> call, Response<HistoryChatResponse> response) {
                if (response.isSuccessful()){
                    userChatHistoryMessages.setText(response.body().historyMessages);
                }
            }

            @Override
            public void onFailure(Call<HistoryChatResponse> call, Throwable t) {

            }
        });
    }
}