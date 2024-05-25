package com.upc.appcentroidiomas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.UpdateRequestApi;
import com.upc.appcentroidiomas.data.model.UpdateRequestResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateRequestFragment extends Fragment {
    private List<UpdateRequestResponse> updateRequestResponses;
    private UpdateRequestAdapter updateRequestAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_update_request, container, false);
        super.onCreate(savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewUpdateRequests);

        updateRequestResponses = new ArrayList<>();

        updateRequestAdapter = new UpdateRequestAdapter(updateRequestResponses, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(updateRequestAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override // Un toque sencillo
            public void onClick(View view, int position) {
                UpdateRequestResponse  updateRequestResponse  = updateRequestResponses.get(position);
                Intent intent = new Intent(getContext(), ActivityUpdateRequest.class);
                intent.putExtra("userId", updateRequestResponse.userId);
                intent.putExtra("userName", updateRequestResponse.userName);
                intent.putExtra("userType", updateRequestResponse.userType);
                intent.putExtra("requestDate", updateRequestResponse.requestDate);
                intent.putExtra("changeCount", updateRequestResponse.changeCount);
                startActivity(intent);
            }

            @Override // Un toque largo
            public void onLongClick(View view, int position) {

            }
        }));

        refreshAvailableUsersList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAvailableUsersList();
    }

    public void refreshAvailableUsersList() {
        if (updateRequestAdapter == null) return;

        // can be launched in a separate asynchronous job
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UpdateRequestApi updateRequestApi = retrofit.create(UpdateRequestApi.class);
        Call<ArrayList<UpdateRequestResponse>> call = updateRequestApi.get();

        call.enqueue(new Callback<ArrayList<UpdateRequestResponse>>() {
            @ Override
            public void onResponse(Call<ArrayList<UpdateRequestResponse>> call, Response<ArrayList<UpdateRequestResponse>> response) {
                if (response.isSuccessful()){
                    updateRequestResponses = response.body();
                    updateRequestAdapter.setUpdateRequests(updateRequestResponses);
                    updateRequestAdapter.notifyDataSetChanged();

                    if (updateRequestResponses.size() == 0){
                        Toast.makeText(getContext(), "No se encontraron solicitudes de actualización.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UpdateRequestResponse>> call, Throwable t) {

            }
        });
    }
}