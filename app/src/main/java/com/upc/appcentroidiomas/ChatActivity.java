package com.upc.appcentroidiomas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.AvailableChatUserApi;
import com.upc.appcentroidiomas.api.InvoiceApi;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.AvailableChatUserDetailResponse;
import com.upc.appcentroidiomas.data.model.AvailableChatUserResponse;
import com.upc.appcentroidiomas.data.model.InvoiceDetailResponse;
import com.upc.appcentroidiomas.data.model.InvoiceResponse;
import com.upc.appcentroidiomas.data.model.LoggedInUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {
    private List<AvailableChatUserDetailResponse> availableUsers;
    private ChatAdapter chatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewAvailableUsers);

        availableUsers = new ArrayList<>();

        chatAdapter = new ChatAdapter(availableUsers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chatAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override // Un toque sencillo
            public void onClick(View view, int position) {
                AvailableChatUserDetailResponse  availableChatUserDetailResponse  = availableUsers.get(position);
                Intent intent = new Intent(ChatActivity.this, UserChatActivity.class);
                intent.putExtra("userIdTo", availableChatUserDetailResponse.userIdTo);
                intent.putExtra("displayNameTo", availableChatUserDetailResponse.displayNameTo);
                intent.putExtra("lastMessageContent", availableChatUserDetailResponse.lastMessageContent);
                intent.putExtra("lastMessageSentAt", availableChatUserDetailResponse.lastMessageSentAt);
                intent.putExtra("roleTo", availableChatUserDetailResponse.roleTo);
                startActivity(intent);
            }

            @Override // Un toque largo
            public void onLongClick(View view, int position) {

            }
        }));

        // can be launched in a separate asynchronous job
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this.getApplicationContext()).getLoggedUser();

        AvailableChatUserApi availableChatUserApi = retrofit.create(AvailableChatUserApi.class);
        Call<AvailableChatUserResponse> call = availableChatUserApi.get(loggedInUser.getUserId());

        call.enqueue(new Callback<AvailableChatUserResponse>() {
            @ Override
            public void onResponse(Call<AvailableChatUserResponse> call, Response<AvailableChatUserResponse> response) {
                if (response.isSuccessful()){
                    availableUsers = response.body().availableUsers;
                    chatAdapter.setAvailableUsers(availableUsers);
                    chatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<AvailableChatUserResponse> call, Throwable t) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshAvailableUsersList();
    }

    public void refreshAvailableUsersList() {
        if (chatAdapter == null) return;

        // can be launched in a separate asynchronous job
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this.getApplicationContext()).getLoggedUser();

        AvailableChatUserApi availableChatUserApi = retrofit.create(AvailableChatUserApi.class);
        Call<AvailableChatUserResponse> call = availableChatUserApi.get(loggedInUser.getUserId());

        call.enqueue(new Callback<AvailableChatUserResponse>() {
            @ Override
            public void onResponse(Call<AvailableChatUserResponse> call, Response<AvailableChatUserResponse> response) {
                if (response.isSuccessful()){
                    availableUsers = response.body().availableUsers;
                    chatAdapter.setAvailableUsers(availableUsers);
                    chatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<AvailableChatUserResponse> call, Throwable t) {

            }
        });
    }
}