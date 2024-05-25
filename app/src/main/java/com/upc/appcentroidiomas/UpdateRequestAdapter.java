package com.upc.appcentroidiomas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upc.appcentroidiomas.data.model.BankAccountResponse;
import com.upc.appcentroidiomas.data.model.UpdateRequestResponse;

import java.util.List;

public class UpdateRequestAdapter extends RecyclerView.Adapter<UpdateRequestAdapter.MyViewHolder> {
    private List<UpdateRequestResponse> updateRequests;
    Context context;

    public UpdateRequestAdapter(Context context) {
        this.context = context;
    }

    public void setUpdateRequests(List<UpdateRequestResponse> updateRequests) {
        this.updateRequests = updateRequests;
    }

    public UpdateRequestAdapter(List<UpdateRequestResponse> updateRequests, Context context) {
        this.updateRequests = updateRequests;
        this.context = context;
    }

    @NonNull
    @Override
    public UpdateRequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_update_request, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateRequestAdapter.MyViewHolder holder, int position) {
        UpdateRequestResponse updateRequest = updateRequests.get(position);

        holder.rowUpdateRequestUserName.setText("Usuario: " + updateRequest.userName);
        holder.rowUpdateRequestUserType.setText("Tipo de Usuario: " + updateRequest.userType);
        holder.rowUpdateRequestDate.setText("Fecha de solicitud: " + updateRequest.requestDate);
        holder.rowUpdateRequestChangeCount.setText("Cantidad de cambios: " + updateRequest.changeCount);
    }

    @Override
    public int getItemCount() {
        return updateRequests.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rowUpdateRequestUserName, rowUpdateRequestUserType, rowUpdateRequestDate, rowUpdateRequestChangeCount;

        MyViewHolder(View itemView) {
            super(itemView);
            this.rowUpdateRequestUserName = itemView.findViewById(R.id.row_update_request_username);
            this.rowUpdateRequestUserType = itemView.findViewById(R.id.row_update_request_usertype);
            this.rowUpdateRequestDate = itemView.findViewById(R.id.row_update_request_date);
            this.rowUpdateRequestChangeCount = itemView.findViewById(R.id.row_update_request_change_count);
        }
    }
}
