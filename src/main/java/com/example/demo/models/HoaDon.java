package com.example.demo.models;

public class HoaDon {
    private String bill_id;
    private String username;
    private String product_id;
    private int quantity;
    private String status;

    public HoaDon(String bill_id, String username, String product_id, int quantity, String status) {
        this.bill_id = bill_id;
        this.username = username;
        this.product_id = product_id;
        this.quantity = quantity;
        this.status = status;
    }

    public String getBill_id() {
        return bill_id;
    }

    public String getUsername() {
        return username;
    }

    public String getProduct_id() {
        return product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }
}
