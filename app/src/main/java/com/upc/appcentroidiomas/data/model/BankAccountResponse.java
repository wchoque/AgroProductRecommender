package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

public class BankAccountResponse {
    @SerializedName("id")
    public int id;
    @SerializedName("bankName")
    public String bankName;
    @SerializedName("accountType")
    public String accountType;
    @SerializedName("accountNumber")
    public String accountNumber;
    @SerializedName("cci")
    public String cci;
    @SerializedName("userId")
    public int userId;
}
