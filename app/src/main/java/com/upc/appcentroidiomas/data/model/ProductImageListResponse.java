package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductImageListResponse {
    @SerializedName("imageUrls")
    public ArrayList<String> imageUrls;
}
