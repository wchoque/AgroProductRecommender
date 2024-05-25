package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

public class UpdateRequestResponse {
    @SerializedName("userId")
    public int userId;
    @SerializedName("userName")
    public String userName;
    @SerializedName("userType")
    public String userType;
    @SerializedName("requestDate")
    public String requestDate;
    @SerializedName("changeCount")
    public int changeCount;
}
