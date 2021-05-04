package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("id")
    public int id;
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
}
