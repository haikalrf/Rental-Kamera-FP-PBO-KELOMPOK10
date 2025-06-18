package com.kelompok10.rentalkamera.model;

public class Camera {

    protected int id;
    protected String brand;
    protected String type;
    protected double hargaRental;
    protected int stok;

    public Camera () {}

    public Camera (int id, String brand, String type, double hargaRental, int stok)
    {
        this.id = id;
        this.brand = brand;
        this.type = type;
        this.hargaRental = hargaRental;
        this.stok = stok;
    }

    public Camera (String brand, String type, double hargaRental, int stok)
    {
        this.brand = brand;
        this.type = type;
        this.hargaRental = hargaRental;
        this.stok = stok;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

     public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getHargaRental() {
        return hargaRental;
    }

    public void setHargaRental(double hargaRental) {
        this.hargaRental = hargaRental;
    }

    public int getStok() {
        return stok;
    }

    public void setStock(int stok) {
        this.stok = stok;
    }

    public String getInfo() {
        return brand + " " + type + " - Rp" + hargaRental + "/day (Stock: " + stok + ")";
    }
    
}
