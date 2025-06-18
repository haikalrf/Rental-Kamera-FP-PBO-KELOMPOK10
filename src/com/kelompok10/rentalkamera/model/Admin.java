package com.kelompok10.rentalkamera.model;

public class Admin extends Person {

    public Admin (int id, String username, String email, String password) {

        super(id, username, email, password);

    }

    @Override
    public String getRole() {
        return "admin";
    }
    
}
