package com.example.ledger;

public class User {
    private String username;
    private String name;
    public User(){
        username = "username";
        name = "name";
    }
    public User(String paramUsername, String paramName){
        this.username = paramUsername;
        this.name = paramName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
