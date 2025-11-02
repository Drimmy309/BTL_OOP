package com.example.demo.utils;
import com.example.demo.models.User;
public class UserSession {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(User user) {
        UserSession.currentUser = user;
    }
    public static void clear() {
        currentUser = null;
    }
}
