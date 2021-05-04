package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

public class InvoiceDetailResponse {
    @SerializedName("id")
    public int id;
    @SerializedName("description")
    public String description;
    @SerializedName("semester")
    public String semester;
    @SerializedName("amount")
    public Double amount;
    @SerializedName("isPaid")
    public boolean isPaid;
    @SerializedName("deadline")
    public String deadline;
    @SerializedName("paidAt")
    public String paidAt;
}