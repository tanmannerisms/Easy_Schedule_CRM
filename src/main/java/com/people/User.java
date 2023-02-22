package com.people;

public class User extends Person{
    private String password;

    public User() {
    }
    public User(int id, String username, String password) {
        setId(id);
        setName(username);
        setPassword(password);
    }

    public String getUsername() {
        return name;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
