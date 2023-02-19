package com.people;

public class User extends Person{
    private String username, password;

    public User() {
    }
    public User(int id, String username, String password) {
        setId(id);
        setUsername(username);
        setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
