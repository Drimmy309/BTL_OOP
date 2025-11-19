package com.example.demo.models;

import javafx.scene.image.Image;

public class SanPham {
    private String product_id;
    private String product_name;
    private String product_category;
    private String username;
    private Image image;
    private int product_quantity;
    private int product_price;
    private String status;

    public SanPham(String product_id, String product_name, String product_category, String username,
                   Image image, int product_quantity, int product_price, String status) {

        this.product_id = product_id;
        this.product_name = product_name;
        this.product_category = product_category;
        this.username = username;
        this.image = image;
        this.product_quantity = product_quantity;
        this.product_price = product_price;
        this.status = status;
    }

    public String getProduct_id() { return this.product_id; }
    public String getProduct_name() { return this.product_name; }
    public String getProduct_category() { return this.product_category; }
    public String getUsername() { return this.username; }
    public Image getImage() { return this.image; }
    public int getProduct_quantity() { return this.product_quantity; }
    public int getProduct_price() { return this.product_price; }
    public String getStatus() { return this.status; }

    public void setProduct_id(String product_id) { this.product_id = product_id; }
    public void setProduct_name(String product_name) { this.product_name = product_name; }
    public void setProduct_category(String product_category) { this.product_category = product_category; }
    public void setUsername(String username) { this.username = username; }
    public void setImage(Image image) { this.image = image; }
    public void setProduct_quantity(int product_quantity) { this.product_quantity = product_quantity; }
    public void setProduct_price(int product_price) { this.product_price = product_price; }
    public void setStatus(String status) { this.status = status; }
}
