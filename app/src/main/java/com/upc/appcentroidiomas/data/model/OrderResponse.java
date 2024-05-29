package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {
    @SerializedName("orderId")
    public int orderId;
    @SerializedName("productChatMessageId")
    public int productChatMessageId;
    @SerializedName("otherUserName")
    public String otherUserName;
    @SerializedName("otherUserId")
    public String otherUserId;
    @SerializedName("productTypeName")
    public String productTypeName;
    @SerializedName("productDescription")
    public String productDescription;
    @SerializedName("quantity")
    public int quantity;
    @SerializedName("harvestDate")
    public String harvestDate;
    @SerializedName("orderDate")
    public String orderDate;
    @SerializedName("totalAmount")
    public int totalAmount;
    @SerializedName("status")
    public int status;
}
