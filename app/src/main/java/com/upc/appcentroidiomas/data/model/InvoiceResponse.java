package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InvoiceResponse {
    @SerializedName("invoices")
    public ArrayList<InvoiceDetailResponse> invoices;
}