package com.example.demo.DAO;

import com.example.demo.models.HoaDon;

import com.example.demo.models.User;
import com.example.demo.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {
    public static List<HoaDon> getHoaDonFromUser(User user) {
        List<HoaDon> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT hd.ma_hoa_don, hd.ma_san_pham, hd.username, sp.ten_san_pham, hd.so_luong, sp.gia, hd.status " +
                "FROM HoaDon hd JOIN SanPham sp ON hd.ma_san_pham = sp.ma_san_pham WHERE hd.username = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String maHoaDon = rs.getString("ma_hoa_don");
                String maSanPham = rs.getString("ma_san_pham");
                String username = rs.getString("username");
                String tenSanPham = rs.getString("ten_san_pham");
                int soLuong = rs.getInt("so_luong");
                int gia = rs.getInt("gia");
                String status = rs.getString("status");
                list.add(new HoaDon(maHoaDon, maSanPham, username, tenSanPham,soLuong, gia * soLuong, status));
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
    public static List<HoaDon> getHoaDonFromSeller(User seller) {
        List<HoaDon> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT hd.ma_hoa_don, hd.ma_san_pham, hd.username AS buyer, sp.ten_san_pham, hd.so_luong, sp.gia, hd.status " +
                "FROM HoaDon hd JOIN SanPham sp ON hd.ma_san_pham = sp.ma_san_pham " +
                "WHERE sp.username = ?"; // 🔹 Lọc theo người bán (seller)
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, seller.getUsername());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new HoaDon(
                        rs.getString("ma_hoa_don"),
                        rs.getString("ma_san_pham"),
                        rs.getString("buyer"), // người mua
                        rs.getString("ten_san_pham"),
                        rs.getInt("so_luong"),
                        rs.getInt("gia") * rs.getInt("so_luong"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("getHoaDonFromSeller error: " + e.getMessage());
        }
        return list;
    }
    public static boolean updateStatus(String maHoaDon, String newStatus) {
        String sql = "UPDATE HoaDon SET status = ? WHERE ma_hoa_don = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setString(2, maHoaDon);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("updateStatus error: " + e.getMessage());
            return false;
        }
    }


}