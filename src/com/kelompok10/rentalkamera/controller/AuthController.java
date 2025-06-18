package com.kelompok10.rentalkamera.controller;

import com.kelompok10.rentalkamera.dao.UserDAO;
import com.kelompok10.rentalkamera.model.Admin;
import com.kelompok10.rentalkamera.model.Person;
import com.kelompok10.rentalkamera.model.User;

public class AuthController {

    private UserDAO userDAO = new UserDAO();

    public boolean register(String username, String email, String password, String role)
    {
       if (userDAO.getByEmail(email) != null) {
            return false;
       }
       
       Person user;

       if ("admin".equalsIgnoreCase(role)) 
       {
            user = new Admin(0, username, email, password);
       }
       else
       {
            user = new User(0, username, email, password);
       }

       return userDAO.insert(user);
    }

    public boolean login(String email, String password) {
        Person user = userDAO.getByEmail(email);
        return user != null && user.getPassword().equals(password);
    }
    
}
