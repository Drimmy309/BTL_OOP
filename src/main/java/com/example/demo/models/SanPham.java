package com.example.demo.models;

import javafx.scene.image.Image;

public class SanPham {
    private String maSanPham;
    private String tenSanPham;
    private Image image;
    private String username;
    private int soLuong;
    private int gia;

    public SanPham(String maSanPham, String tenSanPham, Image image, String username, int soLuong, int gia) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.image = image;
        this.username = username;
        this.soLuong = soLuong;
        this.gia = gia;
    }

    // --- GETTERs ---
    public String getMaSanPham() {
        return maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public Image getImage() {
        return image;
    }

    public String getUsername() {
        return username;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public int getGia() {
        return gia;
    }

    // --- SETTERs (tùy chọn) ---
    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }
}
