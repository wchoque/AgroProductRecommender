package com.upc.appcentroidiomas;

import android.media.Image;

import java.sql.Date;

public class Noticia {
    private String id;
    private String name;
    private String publishedBy;
    private Date publishedAt;
    private String description;
    private Image imageUrl;

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

    public String getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(String publishedBy) {
        this.publishedBy = publishedBy;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Image imageUrl) {
        this.imageUrl = imageUrl;
    }
}