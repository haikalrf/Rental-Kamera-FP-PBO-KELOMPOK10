package com.kelompok10.rentalkamera.model;

public abstract class Person {
    
    protected int id;
    protected String username;
    protected String email;
    protected String password;

    // Constructor
    public Person (int id, String username, String email, String password) { 
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getter dan Setter
    public int getId() { return id; }
    public String getUsername() { return username; } 
    public String getEmail() { return email; } 
    public String getPassword() { return password; } 

    public abstract String getRole();

}
