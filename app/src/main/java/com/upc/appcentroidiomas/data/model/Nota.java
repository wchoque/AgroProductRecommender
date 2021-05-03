package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

public class Nota {
    @SerializedName("id")
    public int id;
    @SerializedName("evaluationName")
    public String evaluationName;
    @SerializedName("evaluationWeightPercentage")
    public int evaluationWeightPercentage;
    @SerializedName("value")
    public String value;
}