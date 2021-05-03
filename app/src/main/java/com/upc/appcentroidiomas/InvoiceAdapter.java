package com.upc.appcentroidiomas;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upc.appcentroidiomas.data.model.InvoiceDetailResponse;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.MyViewHolder> {
    private List<InvoiceDetailResponse> invoices;
    public void setInvoices(List<InvoiceDetailResponse> invoices) {
        this.invoices = invoices;
    }

    public InvoiceAdapter(List<InvoiceDetailResponse> invoices) {
        this.invoices = invoices;
    }

    @NonNull
    @Override
    public InvoiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_invoice, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceAdapter.MyViewHolder holder, int position) {
        InvoiceDetailResponse invoice = invoices.get(position);

        String description = invoice.description;
        String semester = invoice.semester;
        boolean isPaid = invoice.isPaid;

        holder.description.setText(description);
        holder.semester.setText(semester);
        String isPaidMessage;
        if (isPaid){
            isPaidMessage = "Esta boleta ha sido cancelada.";
            holder.isPaid.setTextColor(Color.parseColor("#00FF00"));
        }else {
            isPaidMessage = "Esta boleta no ha sido cancelada aún.";
            holder.isPaid.setTextColor(Color.parseColor("#FF0000"));
        }
        holder.isPaid.setText(isPaidMessage);
    }

    @Override
    public int getItemCount() {
        return invoices.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView description, semester, isPaid;

        MyViewHolder(View itemView) {
            super(itemView);
            this.description = itemView.findViewById(R.id.invoiceDescription);
            this.semester = itemView.findViewById(R.id.invoiceSemester);
            this.isPaid = itemView.findViewById(R.id.invoiceIsPaid);
        }
    }
}
