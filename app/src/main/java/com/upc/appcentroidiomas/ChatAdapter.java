package com.upc.appcentroidiomas;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upc.appcentroidiomas.data.model.AvailableChatUserDetailResponse;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<AvailableChatUserDetailResponse> availableUsers;
    public void setAvailableUsers(List<AvailableChatUserDetailResponse> availableUsers) {
        this.availableUsers = availableUsers;
    }

    public ChatAdapter(List<AvailableChatUserDetailResponse> availableUsers) {
        this.availableUsers = availableUsers;
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_user_available, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        AvailableChatUserDetailResponse availableChatUserDetailResponse = availableUsers.get(position);

        String displayNameTo = availableChatUserDetailResponse.displayNameTo;
        String roleTo = availableChatUserDetailResponse.roleTo;
        String lastMessageContent = availableChatUserDetailResponse.lastMessageContent;
        String lastMessageSentAt = availableChatUserDetailResponse.lastMessageSentAt;

        holder.chatUserAvailableDisplayNameTo.setText(displayNameTo);
        holder.chatUserAvailableRoleTo.setText("Rol: " + roleTo);
        holder.chatUserAvailableLastMessageContent.setText("Ultimo mensaje: "+ lastMessageContent);
        holder.chatUserAvailableLastMessageSentAt.setText("Hora de ultimo mensaje: " + lastMessageSentAt);

/*        String isPaidMessage;
        if (isPaid){
            isPaidMessage = "Esta boleta ha sido cancelada.";
            holder.isPaid.setTextColor(Color.parseColor("#00FF00"));
        }else {
            isPaidMessage = "Esta boleta no ha sido cancelada aún.";
            holder.isPaid.setTextColor(Color.parseColor("#FF0000"));
        }
        holder.isPaid.setText(isPaidMessage);*/
    }

    @Override
    public int getItemCount() {
        return availableUsers.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView chatUserAvailableDisplayNameTo, chatUserAvailableRoleTo, chatUserAvailableLastMessageContent, chatUserAvailableLastMessageSentAt;

        MyViewHolder(View itemView) {
            super(itemView);
            this.chatUserAvailableDisplayNameTo = itemView.findViewById(R.id.chatUserAvailableDisplayNameTo);
            this.chatUserAvailableRoleTo = itemView.findViewById(R.id.chatUserAvailableRoleTo);
            this.chatUserAvailableLastMessageContent = itemView.findViewById(R.id.chatUserAvailableLastMessageContent);
            this.chatUserAvailableLastMessageSentAt = itemView.findViewById(R.id.chatUserAvailableLastMessageSentAt);
        }
    }
}
