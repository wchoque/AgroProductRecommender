package com.upc.appcentroidiomas;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.BankAccountApi;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.BankAccountResponse;
import com.upc.appcentroidiomas.data.model.LoggedInUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityBankAccount extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText txtAccountNumber, txtCCI;
    Spinner spinnerBankName, spinnerAccountType;
    Button btnConfirmAddProduct;
    TextView txtTitle;

    int id;

    Boolean shouldRegister = true;

    CharSequence spinnerSelectedBankName, spinnerSelectedAccountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_detail);

        txtTitle = findViewById(R.id.txt_bank_account_detail_number);

        spinnerBankName = (Spinner) findViewById(R.id.spinner_bank_account_detail_name);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.banks_array,
                android.R.layout.simple_spinner_item
        );
// Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        spinnerBankName.setAdapter(adapter);
        spinnerBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectedBankName = (CharSequence) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




        spinnerAccountType = (Spinner) findViewById(R.id.spinner_bank_account_detail_type);
        ArrayAdapter<CharSequence> adapterAccountType = ArrayAdapter.createFromResource(
                this,
                R.array.account_types_array,
                android.R.layout.simple_spinner_item
        );
        adapterAccountType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAccountType.setAdapter(adapterAccountType);
        spinnerAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectedAccountType = (CharSequence) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        txtAccountNumber = findViewById(R.id.txt_bank_account_detail_number);
        txtCCI = findViewById(R.id.txt_bank_account_detail_cci);

        btnConfirmAddProduct = findViewById(R.id.btnConfirmAddProduct);
        btnConfirmAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shouldRegister) {
                    register();
                }else {
                    update();
                }
            }
        });

        verificarRegistraroActualizar();
    }
    private void verificarRegistraroActualizar(){
        if (getIntent().hasExtra("id")){
            //actualizar
            shouldRegister = false;
            setTitle("Cuenta Bancaria");
            txtTitle.setText("Cuenta Bancaria");

            id = getIntent().getIntExtra("id", 0);

            String extraBankName = getIntent().getStringExtra("bankName");
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerBankName.getAdapter();
            int position = adapter.getPosition(extraBankName);
            spinnerBankName.setSelection(position);

            String extraAccountType = getIntent().getStringExtra("accountType");
            ArrayAdapter<CharSequence> adapterAccountType = (ArrayAdapter<CharSequence>) spinnerAccountType.getAdapter();
            int positionAccountType = adapterAccountType.getPosition(extraAccountType);
            spinnerAccountType.setSelection(positionAccountType);

            txtAccountNumber.setText(getIntent().getStringExtra("accountNumber"));
            txtCCI.setText(getIntent().getStringExtra("cci"));

            btnConfirmAddProduct.setText("Actualizar");
        }else {
            //registrar
            shouldRegister = true;
            setTitle("Cuenta Bancaria");
            txtTitle.setText("Cuenta Bancaria");
            txtAccountNumber.setText("");
            txtCCI.setText("");

            btnConfirmAddProduct.setText("Registrar");
        }
    }

    private void update(){
        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this).getLoggedUser();
        boolean hasErrors = validate();

        if (!hasErrors){

            BankAccountResponse bankAccount = new BankAccountResponse();
            bankAccount.id = id;
            bankAccount.bankName = spinnerSelectedBankName.toString();
            bankAccount.accountType = spinnerSelectedAccountType.toString();
            bankAccount.accountNumber = txtAccountNumber.getText().toString();
            bankAccount.cci = txtCCI.getText().toString();
            bankAccount.userId = loggedInUser.getUserId();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiContants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            BankAccountApi bankAccountApi = retrofit.create(BankAccountApi.class);
            Call<BankAccountResponse> call = bankAccountApi.update(bankAccount.id, bankAccount);
            call.enqueue(new Callback<BankAccountResponse>() {
                @ Override
                public void onResponse(Call<BankAccountResponse> call, Response<BankAccountResponse> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(ActivityBankAccount.this, "Cuenta Bancaria actualizada con exito.", Toast.LENGTH_LONG).show();
                        finish();
                    }else {
                        Toast.makeText(ActivityBankAccount.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<BankAccountResponse> call, Throwable t) {
                    Toast.makeText(ActivityBankAccount.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void register(){
        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this).getLoggedUser();
        boolean hasErrors = validate();

        if (!hasErrors){
            BankAccountResponse bankAccount = new BankAccountResponse();
            bankAccount.bankName = spinnerSelectedBankName.toString();
            bankAccount.accountType = spinnerSelectedAccountType.toString();
            bankAccount.accountNumber = txtAccountNumber.getText().toString();
            bankAccount.cci = txtCCI.getText().toString();
            bankAccount.userId = loggedInUser.getUserId();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiContants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            BankAccountApi bankAccountApi = retrofit.create(BankAccountApi.class);
            Call<BankAccountResponse> call = bankAccountApi.create(bankAccount);
            call.enqueue(new Callback<BankAccountResponse>() {
                @ Override
                public void onResponse(Call<BankAccountResponse> call, Response<BankAccountResponse> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(ActivityBankAccount.this, "Cuenta Bancaria registrada correctamente.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<BankAccountResponse> call, Throwable t) {
                    Toast.makeText(ActivityBankAccount.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean validate(){
        //TODO I NEED THE IDS i need to add a drowpdown
        //product.setProductPresentationId(txtPresentationUnit.getText().toString());
        //product.setProductTypeId(txtType.getText().toString());

        boolean hasErrors = false;
        if (txtAccountNumber.getText().equals("")){
            txtAccountNumber.setError("Requerido");
            hasErrors = true;
        }

        if (txtCCI.getText().equals("")){
            txtCCI.setError("Requerido");
            hasErrors = true;
        }

        return hasErrors;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
