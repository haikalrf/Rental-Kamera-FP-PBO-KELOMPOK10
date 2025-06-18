package com.kelompok10.rentalkamera.model;

public class User extends Person{
    
    public User(int id, String username, String email, String password) {

        super(id, username, email, password);

    }

    @Override
    public String getRole() {
        return "user";
    }
    
}
