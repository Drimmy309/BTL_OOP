package com.example.demo.models;

import java.util.PrimitiveIterator;

public class User {
    private String username;
    private String password;
    private String fullname;
    private String contact;
    private String role;

    public User(String username, String password, String fullname, String contact, String role) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.contact = contact;
        this.role = role;
    }

    public String getUserName() { return this.username; }
    public String getPassword() { return this.password; }
    public String getFullName() { return this.fullname; }
    public String getContact() { return this.contact; }
    public String getRole() { return this.role; }

    public void setUserName(String userName) { this.username = userName; }
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullname = fullName; }
    public void setContact(String contact) { this.contact = contact; }
    public void setRole(String role) { this.role = role; }
}
