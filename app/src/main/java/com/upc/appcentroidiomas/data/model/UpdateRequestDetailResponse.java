package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

public class UpdateRequestDetailResponse {
    @SerializedName("field")
    public String field;
    @SerializedName("oldValue")
    public String oldValue;
    @SerializedName("newValue")
    public String newValue;
}
