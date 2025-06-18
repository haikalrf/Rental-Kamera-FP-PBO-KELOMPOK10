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

    public List<Camera> getAllCamera() {
        List<Camera> camera = new ArrayList<>();
        String sql = "SELECT * FROM cameras";

        try (Statement stmt = connection.createStatement();
             ResultSet resultset = stmt.executeQuery(sql)) {

            while (resultset.next()) {
                Camera cam = new Camera(
                        resultset.getInt("id"),
                        resultset.getString("brand"),
                        resultset.getString("type"),
                        resultset.getDouble("rental_price"),
                        resultset.getInt("stock")
                );
                camera.add(cam);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return camera;
    }

    public void insertCamera(Camera cam) {
        String sql = "INSERT INTO cameras (brand, type, rental_price, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cam.getBrand());
            pstmt.setString(2, cam.getType());
            pstmt.setDouble(3, cam.getHargaRental());
            pstmt.setInt(4, cam.getStok());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCamera(Camera cam) {
        String sql = "UPDATE cameras SET brand = ?, type = ?, rental_price = ?, stock = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cam.getBrand());
            pstmt.setString(2, cam.getType());
            pstmt.setDouble(3, cam.getHargaRental());
            pstmt.setInt(4, cam.getStok());
            pstmt.setInt(5, cam.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCamera(int id) {
        String sql = "DELETE FROM cameras WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Camera> searchCameras(String keyword) {
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
                            resultset.getDouble("rental_price"),
                            resultset.getInt("stock")
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