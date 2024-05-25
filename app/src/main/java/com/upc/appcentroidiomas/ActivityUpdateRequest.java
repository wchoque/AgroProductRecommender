package com.upc.appcentroidiomas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.UpdateRequestApi;
import com.upc.appcentroidiomas.data.model.RejectChangesModel;
import com.upc.appcentroidiomas.data.model.UpdateRequestDetailResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityUpdateRequest extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView txtUserName, txtUserType, txtRequestDate;
    Button btnApprove, btnReject;
    TextView txtTitle;

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_request_detail);

        txtTitle = findViewById(R.id.txt_request_response_detail_title);
        txtUserName = findViewById(R.id.txt_request_response_detail_username);
        txtUserType = findViewById(R.id.txt_request_response_detail_usertype);
        txtRequestDate = findViewById(R.id.txt_request_response_detail_request_date);

        btnApprove = findViewById(R.id.btn_update_request_approve);
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveChanges(userId);
            }
        });

        btnReject = findViewById(R.id.btn_reject);
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectChanges(userId, "Test");
            }
        });

        userId = getIntent().getIntExtra("userId", 0);
        txtUserName.setText("Usuario: " + getIntent().getStringExtra("userName"));
        txtUserType.setText("Tipo de Usuario: " + getIntent().getStringExtra("userType"));
        txtRequestDate.setText("Fecha de solicitud: " + getIntent().getStringExtra("requestDate"));

        loadRequestUpdates(userId);
    }

    private void loadRequestUpdates(int userId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UpdateRequestApi updateRequestApi = retrofit.create(UpdateRequestApi.class);
        Call<ArrayList<UpdateRequestDetailResponse>> call = updateRequestApi.getByUser(userId);
        call.enqueue(new Callback<ArrayList<UpdateRequestDetailResponse>>() {
            @ Override
            public void onResponse(Call<ArrayList<UpdateRequestDetailResponse>> call, Response<ArrayList<UpdateRequestDetailResponse>> response) {
                if (response.isSuccessful()){
                    LinearLayout changesLayout = findViewById(R.id.linear_layout_changes);

                    for (UpdateRequestDetailResponse change : response.body()) {
                        View changeView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.change_item, changesLayout, false);

                        TextView fieldTextView = changeView.findViewById(R.id.txt_field);
                        TextView oldValueTextView = changeView.findViewById(R.id.txt_old_value);
                        TextView newValueTextView = changeView.findViewById(R.id.txt_new_value);

                        fieldTextView.setText("Campo modificado: " + change.field);
                        oldValueTextView.setText("Valor anterior: " + change.oldValue);
                        newValueTextView.setText("Valor nuevo: " + change.newValue);

                        changesLayout.addView(changeView);
                    }
                }else {
                    Toast.makeText(ActivityUpdateRequest.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UpdateRequestDetailResponse>> call, Throwable t) {
                Toast.makeText(ActivityUpdateRequest.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void approveChanges(int userId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UpdateRequestApi updateRequestApi = retrofit.create(UpdateRequestApi.class);
        Call<Void> call = updateRequestApi.approve(userId);
        call.enqueue(new Callback<Void>() {
            @ Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(ActivityUpdateRequest.this, "Cambios aprovados correctamente.", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(ActivityUpdateRequest.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ActivityUpdateRequest.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void rejectChanges(int userId, String comment){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RejectChangesModel rejectChangesModel = new RejectChangesModel();
        rejectChangesModel.comment = comment;

        UpdateRequestApi updateRequestApi = retrofit.create(UpdateRequestApi.class);
        Call<Void> call = updateRequestApi.reject(userId, rejectChangesModel);
        call.enqueue(new Callback<Void>() {
            @ Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(ActivityUpdateRequest.this, "Cambios rechazados correctamente.", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(ActivityUpdateRequest.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ActivityUpdateRequest.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
