package com.upc.appcentroidiomas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upc.appcentroidiomas.data.model.OrderResponse;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private List<OrderResponse> orders;
    Context context;

    public OrderAdapter(Context context) {
        this.context = context;
    }

    public void setOrders(List<OrderResponse> orders) {
        this.orders = orders;
    }

    public OrderAdapter(List<OrderResponse> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.MyViewHolder holder, int position) {
        OrderResponse order = orders.get(position);
        holder.rowOrderCreationDate.setText("Fecha de creación: " + order.orderDate);
        holder.rowOrderBuyerName.setText("Comprador: " + order.buyerName);
        holder.rowOrderProductTypeName.setText("Producto: " + order.productTypeName);
        holder.rowOrderQuantity.setText("Cantidad: " + order.quantity);
        holder.rowOrderTotalAmount.setText("Total a pagar: " + order.totalAmount);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rowOrderCreationDate, rowOrderBuyerName, rowOrderProductTypeName, rowOrderQuantity, rowOrderTotalAmount;

        MyViewHolder(View itemView) {
            super(itemView);
            this.rowOrderCreationDate = itemView.findViewById(R.id.row_order_creation_date);
            this.rowOrderBuyerName = itemView.findViewById(R.id.row_order_buyer_name);
            this.rowOrderProductTypeName = itemView.findViewById(R.id.row_order_product_type_name);
            this.rowOrderQuantity = itemView.findViewById(R.id.row_order_quantity);
            this.rowOrderTotalAmount = itemView.findViewById(R.id.row_order_total_amount);
        }
    }
}
