package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

public class OrderRatingModel {
    @SerializedName("raterUserId")
    public int raterUserId;
    @SerializedName("ratedUserId")
    public int ratedUserId;
    @SerializedName("rating")
    public int rating;
    @SerializedName("comment")
    public String comment;
}
