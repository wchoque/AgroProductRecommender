package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

public class AvailableChatUserDetailResponse {
    @SerializedName("userIdTo")
    public int userIdTo;
    @SerializedName("displayNameTo")
    public String displayNameTo;
    @SerializedName("lastMessageContent")
    public String lastMessageContent;
    @SerializedName("lastMessageSentAt")
    public String lastMessageSentAt;
    @SerializedName("roleTo")
    public String roleTo;
}
