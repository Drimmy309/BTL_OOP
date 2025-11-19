package com.example.demo.DAO;
// Models + DAO
import com.example.demo.models.SanPham;

// Utils
import com.example.demo.utils.DatabaseConnection;

// Java
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.image.Image;

public class SanPhamDAO {
    // Lấy ra tất cả sản phẩm.
    public static List<SanPham> getAllProducts() {
        List<SanPham> productList = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM san_pham";
        try(PreparedStatement stmt = con.prepareStatement(sql);) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                String product_id = rs.getString("product_id");
                String product_name = rs.getString("product_name");
                String product_category = rs.getString("product_category");
                String username =  rs.getString("username");
                int product_quantity = rs.getInt("product_quantity");
                int product_price = rs.getInt("product_price");
                String status = rs.getString("status");

                String image_path = rs.getString("image");
                Image image = null;
                File file = new File("src/main/resources/com/example/demo/images/" + image_path);
                if (file.exists()) {
                    image = new Image(file.toURI().toString());
                } else {
                    System.out.println("File không tồn tại: " + file.getAbsolutePath());
                }
                SanPham sanPham = new SanPham(
                        product_id,
                        product_name,
                        product_category,
                        username,
                        image,
                        product_quantity,
                        product_price,
                        status);
                productList.add(sanPham);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return productList;
    }

    // Lấy ra sản phẩm của người bán.
    public static List<SanPham> getSellerProducts(String _username) {
        List<SanPham> productList = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM san_pham WHERE username = ?";
        try(PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setString(1, _username);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                String product_id = rs.getString("product_id");
                String product_name = rs.getString("product_name");
                String product_category = rs.getString("product_category");
                String username =  rs.getString("username");
                int product_quantity = rs.getInt("product_quantity");
                int product_price = rs.getInt("product_price");
                String status = rs.getString("status");

                String image_path = rs.getString("image");
                Image image = null;
                File file = new File("src/main/resources/com/example/demo/images/" + image_path);
                if (file.exists()) {
                    image = new Image(file.toURI().toString());
                } else {
                    System.out.println("File không tồn tại: " + file.getAbsolutePath());
                }
                SanPham sanPham = new SanPham(
                        product_id,
                        product_name,
                        product_category,
                        username,
                        image,
                        product_quantity,
                        product_price,
                        status);
                productList.add(sanPham);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return productList;
    }

    // Cập nhật thông tin sản phẩm.

    // Thêm sản phẩm mới.

    // Kiểm tra trùng sản phẩm.
}
