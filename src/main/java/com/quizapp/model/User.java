package com.quizapp.model;

public class User {
    public int id;
    public String username;
    public String passwordHash;
    public User(int id, String username, String passwordHash) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
    }
}
