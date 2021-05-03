package com.upc.appcentroidiomas.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CourseDetailedResponse {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("semester")
    public String semester;
    @SerializedName("description")
    public String description;
    @SerializedName("teacherName")
    public String teacherName;
    @SerializedName("notes")
    public ArrayList<Nota> notes;
}