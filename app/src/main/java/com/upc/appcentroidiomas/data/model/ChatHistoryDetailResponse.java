package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

public class ChatHistoryDetailResponse {
    @SerializedName("userIdTo")
    public int userIdTo;
    @SerializedName("displayNameTo")
    public String displayNameTo;
    @SerializedName("messageContent")
    public String messageContent;
    @SerializedName("sentAt")
    public String sentAt;
    @SerializedName("readAt")
    public String readAt;
    @SerializedName("isRead")
    public boolean isRead;
    @SerializedName("isSent")
    public boolean isSent;
}
