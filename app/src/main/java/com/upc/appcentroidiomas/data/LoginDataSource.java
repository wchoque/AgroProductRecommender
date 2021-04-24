package com.upc.appcentroidiomas.data;

import com.upc.appcentroidiomas.data.model.LoggedInUser;

import java.io.IOException;

import static java.util.UUID.randomUUID;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication

            if (!username.equals("admin") && !password.equals("123456")){
                return new Result.Error(new Exception("Username or password are incorrect"));
            }
            String displayName = username;
            LoggedInUser fakeUser = new LoggedInUser(randomUUID().toString(), displayName);
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}