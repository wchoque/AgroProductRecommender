package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

public class NewMessageResponse {
    @SerializedName("id")
    public int id;
    @SerializedName("userIdFrom")
    public String userIdFrom;
    @SerializedName("userIdTo")
    public String userIdTo;
    @SerializedName("messageContent")
    public String messageContent;
}
