package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChatHistoryCompleteResponse {
    @SerializedName("historyMessages")
    public ArrayList<ChatHistoryDetailResponse> historyMessages;
}
