package com.example.demo.models;

public class HoaDon{
    private String ma_hoa_don;
    private String ma_san_pham;
    private String username;
    private String ten_san_pham;
    int so_luong;
    int gia;
    private String status;
    public HoaDon(String ma_hoa_don, String ma_san_pham, String username, String ten_san_pham, int so_luong, int gia,String status) {
        this.ma_hoa_don = ma_hoa_don;
        this.ma_san_pham = ma_san_pham;
        this.ten_san_pham = ten_san_pham;
        this.username = username;
        this.so_luong = so_luong;
        this.gia = gia;
        this.status = status;
    }

    public String getMa_hoa_don() {
        return this.ma_hoa_don;
    };

    public String getMa_san_pham() {
        return this.ma_san_pham;
    }
    public String getUsername() {
        return this.username;
    }
    public String getTen_san_pham() {
        return this.ten_san_pham;
    }
    public int getSo_luong() {
        return this.so_luong;
    }
    public int getGia() {
        return this.gia;
    }
    public String getStatus() {
        return this.status;
    }
}