package com.upc.appcentroidiomas.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {
    private int userId;
    private String userName;
    private String displayName;
    private String email;
    private int userType;

    public LoggedInUser(int userId, String userName, String displayName, String email, int userType) {
        this.userId = userId;
        this.userName = userName;
        this.displayName = displayName;
        this.email = email;
        this.userType = userType;
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

    public int getUserType() {
        return userType;
    }

    public String getEmail() {
        return email;
    }
}