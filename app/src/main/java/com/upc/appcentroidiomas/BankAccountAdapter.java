package com.upc.appcentroidiomas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upc.appcentroidiomas.data.model.BankAccountResponse;

import java.util.List;

public class BankAccountAdapter extends RecyclerView.Adapter<BankAccountAdapter.MyViewHolder> {
    private List<BankAccountResponse> bankAccounts;
    Context context;

    public BankAccountAdapter(Context context) {
        this.context = context;
    }

    public void setBankAccounts(List<BankAccountResponse> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public BankAccountAdapter(List<BankAccountResponse> bankAccounts, Context context) {
        this.bankAccounts = bankAccounts;
        this.context = context;
    }

    @NonNull
    @Override
    public BankAccountAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bank_account, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull BankAccountAdapter.MyViewHolder holder, int position) {
        BankAccountResponse bankAccount = bankAccounts.get(position);

        holder.rowBankAccountName.setText("Banco: " + bankAccount.bankName);
        holder.rowBankAccountType.setText("Tipo de Cuenta: " + bankAccount.accountType);
        holder.rowBankAccountNumber.setText("Numero de cuenta: " + bankAccount.accountNumber);
        holder.rowBankAccountCCI.setText("CCI: " + bankAccount.cci);
    }

    @Override
    public int getItemCount() {
        return bankAccounts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rowBankAccountName, rowBankAccountType, rowBankAccountNumber, rowBankAccountCCI;

        MyViewHolder(View itemView) {
            super(itemView);
            this.rowBankAccountName = itemView.findViewById(R.id.row_bank_account_name);
            this.rowBankAccountType = itemView.findViewById(R.id.row_bank_account_type);
            this.rowBankAccountNumber = itemView.findViewById(R.id.row_bank_account_number);
            this.rowBankAccountCCI = itemView.findViewById(R.id.row_bank_account_cci);
        }
    }
}
