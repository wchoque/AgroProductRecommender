package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

public class ProductResponse {
    @SerializedName("id")
    public int id;
    @SerializedName("description")
    public String description;
    @SerializedName("location")
    public String location;
    @SerializedName("quantity")
    public int quantity;
    @SerializedName("price")
    public double price;
    @SerializedName("harvestDate")
    public String harvestDate;
    @SerializedName("createdAt")
    public String createdAt;
    @SerializedName("createdBy")
    public String createdBy;
    @SerializedName("defaultImageUrl")
    public String defaultImageUrl;
    @SerializedName("productTypeId")
    public int productTypeId;
    @SerializedName("productTypeName")
    public String productTypeName;
    @SerializedName("productPresentationId")
    public int productPresentationId;
    @SerializedName("productPresentationUnit")
    public String productPresentationUnit;
}
