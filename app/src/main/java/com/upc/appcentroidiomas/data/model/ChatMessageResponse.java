package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;
import com.upc.appcentroidiomas.models.ChatMessageModel;

import java.util.ArrayList;

public class ChatMessageResponse {
    @SerializedName("messages")
    public ArrayList<ChatMessageModel> messages;
}
