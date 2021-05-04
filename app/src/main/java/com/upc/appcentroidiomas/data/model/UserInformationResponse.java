package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

public class UserInformationResponse {
    @SerializedName("firstName")
    public String firstName;
    @SerializedName("lastName")
    public String lastName;
    @SerializedName("phoneNumber")
    public String phoneNumber;
    @SerializedName("email")
    public String email;
    @SerializedName("imageUrl")
    public String imageUrl;
    @SerializedName("dni")
    public String dni;
}
