package com.upc.appcentroidiomas;

import android.media.Image;

import java.sql.Date;

public class Noticia {
    private String id;
    private String name;
    private String description;
     private Date datePublished;
    private Image imageUrl;
    private String UuserName;
    private String postedBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }

    public Image getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Image imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUuserName() {
        return UuserName;
    }

    public void setUuserName(String uuserName) {
        UuserName = uuserName;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }
}
