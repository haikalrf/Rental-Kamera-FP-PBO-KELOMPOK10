package com.kelompok10.rentalkamera.dao;

import com.kelompok10.rentalkamera.config.DBConnection;
import com.kelompok10.rentalkamera.model.Camera;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CameraDAO {

    private Connection connection;

    public CameraDAO() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //CREATE
    public boolean insertCamera(Camera camera) {
        String sql = "INSERT INTO cameras (brand, type, harga_rental, stok) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, camera.getBrand());
            pstmt.setString(2, camera.getType());
            pstmt.setDouble(3, camera.getHargaRental());
            pstmt.setInt(4, camera.getStok());
            pstmt.executeUpdate();
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //READ
    public List<Camera> getAllCameras() {
        List<Camera> cameras = new ArrayList<>();
        String sql = "SELECT * FROM cameras";

        try (Statement stmt = connection.createStatement();
             ResultSet resultset = stmt.executeQuery(sql)) {

            while (resultset.next()) {
                Camera cam = new Camera(
                        resultset.getInt("id"),
                        resultset.getString("brand"),
                        resultset.getString("type"),
                        resultset.getDouble("harga_rental"),
                        resultset.getInt("stok")
                );
                cameras.add(cam);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cameras;
    }

    //UPDATE
    public boolean updateCamera(Camera camera) {
        String sql = "UPDATE cameras SET brand = ?, type = ?, harga_rental = ?, stok = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, camera.getBrand());
            pstmt.setString(2, camera.getType());
            pstmt.setDouble(3, camera.getHargaRental());
            pstmt.setInt(4, camera.getStok());
            pstmt.setInt(5, camera.getId());
            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //DELETE
    public boolean deleteCamera(int id) {
        String sql = "DELETE FROM cameras WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //SEARCH
    public List<Camera> searchCamera(String keyword) {
        List<Camera> cameras = new ArrayList<>();
        String sql = "SELECT * FROM cameras WHERE LOWER(brand) LIKE ? OR LOWER(type) LIKE ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String pattern = "%" + keyword.toLowerCase() + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);

            try (ResultSet resultset = pstmt.executeQuery()) {
                while (resultset.next()) {
                    Camera cam = new Camera(
                            resultset.getInt("id"),
                            resultset.getString("brand"),
                            resultset.getString("type"),
                            resultset.getDouble("harga_rental"),
                            resultset.getInt("stok")
                    );
                    cameras.add(cam);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cameras;
    }
}