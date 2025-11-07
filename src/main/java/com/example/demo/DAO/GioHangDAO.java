package com.example.demo.DAO;
import com.example.demo.models.SanPham;

import com.example.demo.models.GioHang;
import com.example.demo.models.User;
import com.example.demo.utils.DatabaseConnection;
import javafx.scene.image.Image;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GioHangDAO {
    public static List<SanPham> getGioHangFromUser(User user){
        Connection conn = DatabaseConnection.getConnection();
        List<SanPham> Cart = new ArrayList<>();
        String query = "SELECT gh.ma_san_pham, sp.ten_san_pham, sp.image, sp.username, gh.so_luong, sp.gia " +
                "FROM giohang gh JOIN sanpham sp ON sp.ma_san_pham = gh.ma_san_pham " +
                "WHERE gh.username = ?";

        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1,user.getUsername());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                String ma_san_pham = rs.getString("ma_san_pham");
                String ten_san_pham = rs.getString("ten_san_pham");
                String imagePath = rs.getString("image");
                Image image = null;
                try {
                    image = new Image(GioHangDAO.class.getResourceAsStream("/" + imagePath));
                } catch (Exception e) {
                    System.out.println("Không tìm thấy ảnh: " + imagePath);
                }
                String username = rs.getString("username");
                int so_luong = rs.getInt("so_luong");
                int gia = rs.getInt("gia");
                Cart.add(new SanPham(ma_san_pham, ten_san_pham, image, username, so_luong, gia));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Cart;
    }

    public static void addSanPhamToCart(SanPham sanPham, User user){
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM GioHang WHERE ma_san_pham = ? AND username = ?";
        String update = "UPDATE GioHang SET so_luong = ? WHERE ma_san_pham = ? AND username = ?";
        String insert = "INSERT INTO GioHang VALUES (?, ?, ?)";

        try(PreparedStatement stmt1 = conn.prepareStatement(sql)){
            stmt1.setString(1, sanPham.getMaSanPham());
            stmt1.setString(2, user.getUsername());
            ResultSet rs = stmt1.executeQuery();
            if(rs.next()){
                try(PreparedStatement updateStmt = conn.prepareStatement(update)){
                    int so_luong  = rs.getInt("so_luong");
                    updateStmt.setInt(1, so_luong + sanPham.getSoLuong());
                    updateStmt.setString(2, sanPham.getMaSanPham());
                    updateStmt.setString(3, user.getUsername());
                    updateStmt.executeUpdate();
                }catch(SQLException e){
                    System.out.println(e.getMessage());
                }
            }else{
                try(PreparedStatement insertStmt = conn.prepareStatement(insert)){
                    insertStmt.setString(1, user.getUsername());
                    insertStmt.setString(2, sanPham.getMaSanPham());
                    insertStmt.setInt(3, sanPham.getSoLuong());
                    insertStmt.executeUpdate();
                } catch(SQLException e){
                    System.out.println(e.getMessage());
                }
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void buySanPhamFromCart(SanPham sanPham, User user) {
        Connection conn = DatabaseConnection.getConnection();
        String deleteSQL = "DELETE FROM GioHang WHERE ma_san_pham = ? AND username = ?";
        String insertSQL = "INSERT INTO HoaDon (username, ma_san_pham, so_luong, status) VALUES (?, ?, ?, 'Pending')";

        try {
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                deleteStmt.setString(1, sanPham.getMaSanPham());
                deleteStmt.setString(2, user.getUsername());
                deleteStmt.executeUpdate();
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                insertStmt.setString(1, user.getUsername());
                insertStmt.setString(2, sanPham.getMaSanPham());
                insertStmt.setInt(3, sanPham.getSoLuong());
                insertStmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteSanPhamFromCart(SanPham sanPham, User user) {
        Connection conn = DatabaseConnection.getConnection();
        String deleteSQL = "DELETE FROM GioHang WHERE ma_san_pham = ? AND username = ?";
        String selectSQL = "SELECT so_luong FROM SanPham WHERE ma_san_pham = ?";
        String updateSanPham = "UPDATE SanPham SET so_luong = ? WHERE ma_san_pham = ?";
        int so_luong = 0;
        try {

            // DELETE FROM CART.
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                deleteStmt.setString(1, sanPham.getMaSanPham());
                deleteStmt.setString(2, user.getUsername());
                deleteStmt.executeUpdate();
            }

            // GET SANPHAM FROM SANPHAM.
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {
                selectStmt.setString(1, sanPham.getMaSanPham());
                ResultSet rs = selectStmt.executeQuery();
                if(rs.next()){
                    so_luong = rs.getInt("so_luong");
                }
            }

            // REFILL AMOUNT.
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSanPham)) {
                updateStmt.setInt(1, sanPham.getSoLuong() + so_luong);
                updateStmt.setString(2, sanPham.getMaSanPham());
                updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}