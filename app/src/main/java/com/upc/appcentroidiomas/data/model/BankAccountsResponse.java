package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BankAccountsResponse {
    @SerializedName("bankAccounts")
    public ArrayList<BankAccountResponse> bankAccountResponses;
}