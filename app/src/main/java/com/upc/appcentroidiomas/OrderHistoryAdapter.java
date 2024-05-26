package com.upc.appcentroidiomas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upc.appcentroidiomas.data.model.OrderHistoryResponse;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {
    private List<OrderHistoryResponse> ordersHistory;
    Context context;

    public OrderHistoryAdapter(Context context) {
        this.context = context;
    }

    public void setOrdersHistory(List<OrderHistoryResponse> ordersHistory) {
        this.ordersHistory = ordersHistory;
    }

    public OrderHistoryAdapter(List<OrderHistoryResponse> ordersHistory, Context context) {
        this.ordersHistory = ordersHistory;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_history, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.MyViewHolder holder, int position) {
        OrderHistoryResponse orderHistory = ordersHistory.get(position);
        holder.rowOrderHistoryCreationDate.setText("Fecha de creación: " + orderHistory.orderDate);
        holder.rowOrderHistoryBuyerName.setText("Comprador: " + orderHistory.buyerName);
        holder.rowOrderHistoryProductTypeName.setText("Producto: " + orderHistory.productTypeName);
        holder.rowOrderHistoryQuantity.setText("Cantidad: " + orderHistory.quantity);
        holder.rowOrderHistoryTotalAmount.setText("Total a pagar: " + orderHistory.totalAmount);
        holder.rowOrderHistoryRatingBar.setRating(4f);
    }

    @Override
    public int getItemCount() {
        return ordersHistory.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rowOrderHistoryCreationDate, rowOrderHistoryBuyerName, rowOrderHistoryProductTypeName, rowOrderHistoryQuantity, rowOrderHistoryTotalAmount;
        RatingBar rowOrderHistoryRatingBar;

        MyViewHolder(View itemView) {
            super(itemView);
            this.rowOrderHistoryCreationDate = itemView.findViewById(R.id.row_order_history_creation_date);
            this.rowOrderHistoryBuyerName = itemView.findViewById(R.id.row_order_history_buyer_name);
            this.rowOrderHistoryProductTypeName = itemView.findViewById(R.id.row_order_history_product_type_name);
            this.rowOrderHistoryQuantity = itemView.findViewById(R.id.row_order_history_quantity);
            this.rowOrderHistoryTotalAmount = itemView.findViewById(R.id.row_order_history_total_amount);
            this.rowOrderHistoryRatingBar = itemView.findViewById(R.id.row_order_history_rating);
        }
    }
}
