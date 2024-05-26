package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

public class UpdateOrderModel {
    @SerializedName("orderId")
    public int orderId;

    @SerializedName("quantity")
    public int quantity;
    @SerializedName("totalAmount")
    public Double totalAmount;
    @SerializedName("status")
    public int status;
    @SerializedName("harvestDate")
    public String harvestDate;
}
