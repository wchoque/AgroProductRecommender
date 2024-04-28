package com.upc.appcentroidiomas.models;

import java.util.Date;
public class ChatMessageModel {
    private String message;
    private int senderId;
    private Date timestamp;

    public ChatMessageModel() {
    }

    public ChatMessageModel(String message, int senderId, Date timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}