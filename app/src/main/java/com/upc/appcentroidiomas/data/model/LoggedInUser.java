package com.upc.appcentroidiomas.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {
    private int userId;
    private String userName;
    private String displayName;

    public LoggedInUser(int userId, String userName, String displayName) {
        this.userId = userId;
        this.userName = userName;
        this.displayName = displayName;
    }

    public int getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUserName() {
        return userName;
    }
}