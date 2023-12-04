package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

public class UserInformationResponse {
    @SerializedName("firstName")
    public String firstName;
    @SerializedName("lastName")
    public String lastName;
    @SerializedName("email")
    public String email;
    @SerializedName("phoneNumber")
    public String phoneNumber;
     @SerializedName("imageUrl")
    public String imageUrl;
    @SerializedName("dni")
    public String dni;
    @SerializedName("gender")
    public int gender;
    @SerializedName("bio")
    public String bio;
    @SerializedName("webpageUrl")
    public String webpageUrl;
}
