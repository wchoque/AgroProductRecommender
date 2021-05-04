package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AvailableChatUserResponse {
    @SerializedName("availableUsers")
    public ArrayList<AvailableChatUserDetailResponse> availableUsers;
}
