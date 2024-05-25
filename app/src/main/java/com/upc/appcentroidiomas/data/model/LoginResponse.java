package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("id")
    public int id;
    @SerializedName("userName")
    public String userName;
    @SerializedName("userId")
    public int userId;
    @SerializedName("userType")
    public int userType;
    @SerializedName("firstName")
    public String firstName;
    @SerializedName("lastName")
    public String lastName;
    @SerializedName("email")
    public String email;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("displayName")
    public String displayName;
    @SerializedName("profileImageUrl")
    public String profileImageUrl;
}
