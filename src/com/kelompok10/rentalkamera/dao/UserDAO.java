package com.kelompok10.rentalkamera.dao;

import com.kelompok10.rentalkamera.config.DBConnection;
import com.kelompok10.rentalkamera.model.Admin;
import com.kelompok10.rentalkamera.model.Person;
import com.kelompok10.rentalkamera.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    
    public Person getByEmail(String email) {
        Person user = null;
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet resultset = stmt.executeQuery();

            if (resultset.next()) {
                String role = resultset.getString("role");
                if ("admin".equalsIgnoreCase(role)) {
                    user = new Admin(
                            resultset.getInt("id"),
                            resultset.getString("username"),
                            resultset.getString("email"),
                            resultset.getString("password")
                    );
                } else {
                    user = new User(
                            resultset.getInt("id"),
                            resultset.getString("username"),
                            resultset.getString("email"),
                            resultset.getString("password")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean insert(Person user) {
        String sql = "INSERT INTO users(username, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            int affected = stmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
