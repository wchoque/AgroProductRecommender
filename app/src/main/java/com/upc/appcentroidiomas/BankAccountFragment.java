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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.BankAccountApi;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.BankAccountResponse;
import com.upc.appcentroidiomas.data.model.BankAccountsResponse;
import com.upc.appcentroidiomas.data.model.LoggedInUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BankAccountFragment extends Fragment {
    private List<BankAccountResponse> bankAccounts;
    private BankAccountAdapter bankAccountAdapter;
    FloatingActionButton btnAddBankAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_bank_account, container, false);
        super.onCreate(savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewBankAccounts);

        bankAccounts = new ArrayList<>();

        bankAccountAdapter = new BankAccountAdapter(bankAccounts, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bankAccountAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override // Un toque sencillo
            public void onClick(View view, int position) {
                BankAccountResponse  bankAccountResponse  = bankAccounts.get(position);
                Intent intent = new Intent(getContext(), ActivityBankAccount.class);
                intent.putExtra("id", bankAccountResponse.id);
                intent.putExtra("bankName", bankAccountResponse.bankName);
                intent.putExtra("accountType", bankAccountResponse.accountType);
                intent.putExtra("accountNumber", bankAccountResponse.accountNumber);
                intent.putExtra("cci", bankAccountResponse.cci);
                startActivity(intent);
            }

            @Override // Un toque largo
            public void onLongClick(View view, int position) {

            }
        }));

        btnAddBankAccount = view.findViewById(R.id.btnAddBankAccount);
        btnAddBankAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityBankAccount.class);
                startActivity(intent);
            }
        });

        refreshAvailableUsersList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAvailableUsersList();
    }

    public void refreshAvailableUsersList() {
        if (bankAccountAdapter == null) return;

        // can be launched in a separate asynchronous job
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this.getContext()).getLoggedUser();

        BankAccountApi bankAccountApi = retrofit.create(BankAccountApi.class);
        Call<ArrayList<BankAccountResponse>> call = bankAccountApi.get(loggedInUser.getUserId());

        call.enqueue(new Callback<ArrayList<BankAccountResponse>>() {
            @ Override
            public void onResponse(Call<ArrayList<BankAccountResponse>> call, Response<ArrayList<BankAccountResponse>> response) {
                if (response.isSuccessful()){
                    bankAccounts = response.body();
                    bankAccountAdapter.setBankAccounts(bankAccounts);
                    bankAccountAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BankAccountResponse>> call, Throwable t) {

            }
        });
    }
}