package com.upc.appcentroidiomas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.AvailableProductChatUserApi;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.AvailableChatUserDetailResponse;
import com.upc.appcentroidiomas.data.model.AvailableChatUserResponse;
import com.upc.appcentroidiomas.data.model.LoggedInUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManageOfferFragment extends Fragment {
    private List<AvailableChatUserDetailResponse> availableUsers;
    private ChatAdapter chatAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_manage_offer, container, false);
        // Configura tus vistas aquí usando view.findViewById(...)
        super.onCreate(savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewManageOffers);

        availableUsers = new ArrayList<>();

        chatAdapter = new ChatAdapter(availableUsers, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chatAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override // Un toque sencillo
            public void onClick(View view, int position) {
                AvailableChatUserDetailResponse  availableChatUserDetailResponse  = availableUsers.get(position);
                Intent intent = new Intent(getContext(), ChatDetailActivity.class);
                intent.putExtra("userIdTo", availableChatUserDetailResponse.userIdTo);
                intent.putExtra("displayNameTo", availableChatUserDetailResponse.displayNameTo);
                intent.putExtra("lastMessageContent", availableChatUserDetailResponse.lastMessageContent);
                intent.putExtra("lastMessageSentAt", availableChatUserDetailResponse.lastMessageSentAt);
                intent.putExtra("roleTo", availableChatUserDetailResponse.roleTo);
                intent.putExtra("imageUrl", availableChatUserDetailResponse.imageUrl);
                startActivity(intent);
            }

            @Override // Un toque largo
            public void onLongClick(View view, int position) {

            }
        }));

        refreshOffersList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshOffersList();
    }

    public void refreshOffersList() {
        if (chatAdapter == null) return;

        // can be launched in a separate asynchronous job
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this.getContext()).getLoggedUser();

        AvailableProductChatUserApi availableProductChatUserApi = retrofit.create(AvailableProductChatUserApi.class);
        Call<AvailableChatUserResponse> call = availableProductChatUserApi.getOffers(loggedInUser.getUserId());

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