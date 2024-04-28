package com.upc.appcentroidiomas;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upc.appcentroidiomas.data.model.AvailableChatUserDetailResponse;
import com.upc.appcentroidiomas.utils.AndroidUtil;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<AvailableChatUserDetailResponse> availableUsers;
    Context context;

    public ChatAdapter(Context context) {
        this.context = context;
    }

    public void setAvailableUsers(List<AvailableChatUserDetailResponse> availableUsers) {
        this.availableUsers = availableUsers;
    }

    public ChatAdapter(List<AvailableChatUserDetailResponse> availableUsers, Context context) {
        this.availableUsers = availableUsers;
        this.context = context;
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
        if (availableChatUserDetailResponse.imageUrl != null){
            AndroidUtil.setProfilePic(context, Uri.parse(availableChatUserDetailResponse.imageUrl), holder.profilePic);
        }
    }

    @Override
    public int getItemCount() {
        return availableUsers.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView chatUserAvailableDisplayNameTo, chatUserAvailableRoleTo, chatUserAvailableLastMessageContent, chatUserAvailableLastMessageSentAt;
        ImageView profilePic;

        MyViewHolder(View itemView) {
            super(itemView);
            this.chatUserAvailableDisplayNameTo = itemView.findViewById(R.id.chatUserAvailableDisplayNameTo);
            this.chatUserAvailableRoleTo = itemView.findViewById(R.id.chatUserAvailableRoleTo);
            this.chatUserAvailableLastMessageContent = itemView.findViewById(R.id.chatUserAvailableLastMessageContent);
            this.chatUserAvailableLastMessageSentAt = itemView.findViewById(R.id.chatUserAvailableLastMessageSentAt);
            this.profilePic = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
