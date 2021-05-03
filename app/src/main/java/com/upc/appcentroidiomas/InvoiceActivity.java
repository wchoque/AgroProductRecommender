package com.upc.appcentroidiomas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.InvoiceApi;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.InvoiceDetailResponse;
import com.upc.appcentroidiomas.data.model.InvoiceResponse;
import com.upc.appcentroidiomas.data.model.LoggedInUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvoiceActivity extends AppCompatActivity {
    private List<InvoiceDetailResponse> invoices;
    private InvoiceAdapter invoiceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewInvoices);

        invoices = new ArrayList<>();

        invoiceAdapter = new InvoiceAdapter(invoices);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(invoiceAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override // Un toque sencillo
            public void onClick(View view, int position) {
                InvoiceDetailResponse invoice  = invoices.get(position);
                Intent intent = new Intent(InvoiceActivity.this, InvoiceDetailActivity.class);
                intent.putExtra("id", invoice.id);
                intent.putExtra("description", invoice.description);
                intent.putExtra("semester", invoice.semester);
                intent.putExtra("amount", invoice.amount);
                intent.putExtra("isPaid", invoice.isPaid);
                intent.putExtra("deadline", invoice.deadline);
                intent.putExtra("paidAt", invoice.paidAt);
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

        InvoiceApi invoiceApi = retrofit.create(InvoiceApi.class);
        Call<InvoiceResponse> call = invoiceApi.get(loggedInUser.getUserId());

        call.enqueue(new Callback<InvoiceResponse>() {
            @ Override
            public void onResponse(Call<InvoiceResponse> call, Response<InvoiceResponse> response) {
                if (response.isSuccessful()){
                    invoices = response.body().invoices;
                    invoiceAdapter.setInvoices(invoices);
                    invoiceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<InvoiceResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshInvoiceList();
    }

    public void refreshInvoiceList() {
        if (invoiceAdapter == null) return;

        // can be launched in a separate asynchronous job
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this.getApplicationContext()).getLoggedUser();

        InvoiceApi invoiceApi = retrofit.create(InvoiceApi.class);
        Call<InvoiceResponse> call = invoiceApi.get(loggedInUser.getUserId());

        call.enqueue(new Callback<InvoiceResponse>() {
            @ Override
            public void onResponse(Call<InvoiceResponse> call, Response<InvoiceResponse> response) {
                if (response.isSuccessful()){
                    invoices = response.body().invoices;
                    invoiceAdapter.setInvoices(invoices);
                    invoiceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<InvoiceResponse> call, Throwable t) {

            }
        });
    }
}