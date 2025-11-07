package com.example.demo.models;

import com.example.demo.models.SanPham;
import java.util.ArrayList;
import java.util.List;

public class GioHang {
    private List<SanPham> Cart = new ArrayList<>();

    public GioHang(List<SanPham> Cart) {
        this.Cart = Cart;
    }

    public List<SanPham> getSanPham() {
        return this.Cart;
    }
}