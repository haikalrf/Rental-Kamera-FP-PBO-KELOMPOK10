package com.kelompok10.rentalkamera.controller;

import java.util.List;

import com.kelompok10.rentalkamera.dao.CameraDAO;
import com.kelompok10.rentalkamera.model.Camera;

public class CameraController {
    
    private CameraDAO cameraDAO = new CameraDAO();

    public boolean tambahKamera(String brand, String type, double harga, int stok) {
        Camera camera = new Camera(brand, type, harga, stok);
        return cameraDAO.insertCamera(camera);
    }

    public List<Camera> ambilSemuaKamera() {
        return cameraDAO.getAllCameras();
    }

    public boolean ubahKamera(int id, String brand, String type, double harga, int stok) {
        Camera camera = new Camera(id, brand, type, harga, stok);
        return cameraDAO.updateCamera(camera);
    }

    public List<Camera> searchCamera(String keyword) {
        return cameraDAO.searchCamera(keyword);
    }

    public boolean hapusKamera(int id) {
        return cameraDAO.deleteCamera(id);
    }

}
