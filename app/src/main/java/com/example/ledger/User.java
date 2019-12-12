package com.example.ledger;

/**
 * Class used to contain a user's information
 */
public class User {
    private String username;
    private String email;

    public User() {
        username = "username";
        email = "name";
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
