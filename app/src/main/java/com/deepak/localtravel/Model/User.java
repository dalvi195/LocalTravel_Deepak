package com.deepak.localtravel.Model;

public class User {
    private String Name;
    private String Password;
    private String email;

    public User() {
    }

    public User(String name, String password, String email) {
        this.Name = name;
        this.Password = password;
        this.email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
