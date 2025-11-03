package com.example.demo.models;

import javafx.scene.image.Image;

public class SanPham {
    private String ma_san_pham;
    private String ten_san_pham;
    private Image image;
    private String username;
    private int so_luong;
    private int gia;

    public SanPham(String ma_san_pham, String ten_san_pham,  Image image, String username, int so_luong, int gia) {
        this.ten_san_pham = ten_san_pham;
        this.image = image;
        this.ma_san_pham = ma_san_pham;
        this.username = username;
        this.so_luong = so_luong;
        this.gia = gia;
    }

    // Getter
    public String getTen_san_pham() { return this.ten_san_pham; }
    public int getGia() { return this.gia; }
    public int getSoluong() { return this.so_luong; }
    public String getUsername() { return this.username; }
    public String getMaSanPham() { return this.ma_san_pham; }
    public Image getImage() { return this.image; }

    // Setter
    public void setTen_san_pham(String ten_san_pham) { this.ten_san_pham = ten_san_pham; }
    public void setGia(int gia) { this.gia = gia; }
    public void setImage(Image image) {  this.image = image; }
    public void setUsername(String username) { this.username = username; }
    public void setMaSanPham(String ma_san_pham) { this.ma_san_pham = ma_san_pham; }
    public void setSoluong(int so_luong) { this.so_luong = so_luong; }
}
