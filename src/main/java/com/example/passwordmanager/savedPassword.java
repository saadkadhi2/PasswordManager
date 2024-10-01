package com.example.passwordmanager;
import java.util.ArrayList;

public class savedPassword {
    private String name;
    private String password;
    public savedPassword() {
    }
    public void addPassword(String name, String password) {
        this.name = name;
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }

}
