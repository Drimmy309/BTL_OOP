package com.example.demo.models;

public class GioHang {
    private String product_id;
    private String username;  // Customer
    private int quantity;

    public GioHang(String product_id, String username, int quantity) {
        this.product_id = product_id;
        this.username = username;
        this.quantity = quantity;
    }

    public String getProduct_id() {
        return this.product_id;
    }
    public int getQuantity() {
        return this.quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getUsername() {
        return this.username;
    }
}
