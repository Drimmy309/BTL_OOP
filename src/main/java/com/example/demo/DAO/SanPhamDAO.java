package com.example.demo.DAO;

import com.example.demo.models.SanPham;
import com.example.demo.utils.DatabaseConnection;
import javafx.scene.image.Image;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {
    // Update lại số lượng sản phẩm sau khi thêm vào giỏ hàng.
    public static void updateSanPham(SanPham sanPham){
        Connection con = DatabaseConnection.getConnection();
        String query = "UPDATE sanpham SET so_luong = ? WHERE ma_san_pham = ?";

        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setInt(1, sanPham.getSoLuong());
            stmt.setString(2, sanPham.getMaSanPham());
            stmt.executeUpdate();
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public static List<SanPham> getProductionList() {
        List<SanPham> list = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM SanPham");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String maSP = rs.getString("ma_san_pham");
                String tenSP = rs.getString("ten_san_pham");
                String imagePath = rs.getString("image");
                String username = rs.getString("username");
                int soLuong = rs.getInt("so_luong");
                int gia = rs.getInt("gia");

                Image image = null;
                try {
                    String path = "/com/example/demo/images/" + imagePath;
                    image = new Image(SanPhamDAO.class.getResource(path).toExternalForm());
                } catch (Exception e) {
                    System.out.println("Không thể tải ảnh: " + imagePath);
                }

                list.add(new SanPham(maSP, tenSP, image, username, soLuong, gia));
            }

        } catch (SQLException e) {
            System.out.println("getProductionList error: " + e.getMessage());
        }

        return list;

    }

    public static List<SanPham> getProductionListByUser(String username) {
        List<SanPham> list = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM SanPham WHERE username = ?")) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Image img = null;
                String imageFile = rs.getString("image");

                try {
                    // 🔹 Nối đúng đường dẫn trong thư mục resources
                    String path = "/com/example/demo/images/" + imageFile;
                    img = new Image(SanPhamDAO.class.getResource(path).toExternalForm());
                } catch (Exception e) {
                    System.out.println("⚠️ Không thể load ảnh trong resources: " + imageFile);
                }

                // 🔸 Nếu ảnh không có trong resources, thử load từ đường dẫn tuyệt đối (nếu có)
                if (img == null && imageFile != null) {
                    java.io.File f = new java.io.File(imageFile);
                    if (f.exists()) {
                        img = new Image(f.toURI().toString());
                        System.out.println("📁 Ảnh được load từ ổ đĩa: " + f.getAbsolutePath());
                    } else {
                        System.out.println("❌ Không tìm thấy ảnh: " + imageFile);
                    }
                }

                // 🧩 Thêm sản phẩm vào danh sách
                list.add(new SanPham(
                        rs.getString("ma_san_pham"),
                        rs.getString("ten_san_pham"),
                        img,
                        rs.getString("username"),
                        rs.getInt("so_luong"),
                        rs.getInt("gia")
                ));
            }

        } catch (SQLException e) {
            System.out.println("getProductionListByUser error: " + e.getMessage());
        }

        return list;
    }


    public static boolean insertProduct(SanPham sp, String fileName) {
        String sql = "INSERT INTO SanPham (ma_san_pham, ten_san_pham, username, image, so_luong, gia) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, sp.getMaSanPham());
            stmt.setString(2, sp.getTenSanPham());
            stmt.setString(3, sp.getUsername());
            stmt.setString(4, fileName); // ✅ chỉ tên file (VD: "snack.jpg")
            stmt.setInt(5, sp.getSoLuong());
            stmt.setInt(6, sp.getGia());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("❌ insertProduct error: " + e.getMessage());
            return false;
        }
    }



    public static boolean updateProduct(SanPham sp) {
        String sql = "UPDATE SanPham SET ten_san_pham=?, so_luong=?, gia=? WHERE ma_san_pham=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, sp.getTenSanPham());
            stmt.setInt(2, sp.getSoLuong());
            stmt.setInt(3, sp.getGia());
            stmt.setString(4, sp.getMaSanPham());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("updateProduct error: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteProduct(String maSP) {
        String sql = "DELETE FROM SanPham WHERE ma_san_pham=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maSP);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("deleteProduct error: " + e.getMessage());
            return false;
        }
    }

    public static SanPham getProductById(String maSP) {
        String sql = "SELECT * FROM SanPham WHERE ma_san_pham=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maSP);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SanPham(
                        rs.getString("ma_san_pham"),
                        rs.getString("ten_san_pham"),
                        null,
                        rs.getString("username"),
                        rs.getInt("so_luong"),
                        rs.getInt("gia")
                );
            }
        } catch (SQLException e) {
            System.out.println("getProductById error: " + e.getMessage());
        }
        return null;
    }
}
