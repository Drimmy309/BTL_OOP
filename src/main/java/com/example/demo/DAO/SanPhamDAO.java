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
    public static class DuplicateCheckResult {
        public boolean imageExists = false;

        public boolean hasAnyDuplicate() {
            return imageExists;
        }
    }

    // Lấy ra tất cả sản phẩm.
    public static List<SanPham> getAllProducts() {
        List<SanPham> productList = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM product";
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
        String sql = "SELECT * FROM product WHERE username = ?";
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

    // Lấy ra tất cả sản phẩm theo Category.
    public static List<SanPham> getProductsByFilter(String filter) {
        List<SanPham> productList = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM product WHERE product_category = ?";
        try(PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, filter);
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
    // Cập nhật số lượng sản phẩm sau khi thêm vào giỏ hàng sản phẩm.
    public static void updateProductQuantity(String product_id, int quantity) {
        Connection con = DatabaseConnection.getConnection();
        String sql = "UPDATE product set product_quantity = ? WHERE product_id = ?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, quantity);
            stmt.setString(2, product_id);
            stmt.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Lấy ra thông tin sản phẩm có product_id.
    public static SanPham getProductById(String product_id) {
        Connection con = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM product WHERE product_id = ?";
        SanPham sanPham = null;
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, product_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
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
                sanPham = new SanPham(product_id, product_name,
                        product_category, username,
                        image, product_quantity,
                        product_price, status);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sanPham;
    }
    // Thêm sản phẩm mới.
    public static boolean addProduct(SanPham sanPham, String imageName) {
        Connection con = DatabaseConnection.getConnection();
        String sql = "INSERT INTO product (product_id, product_name, product_category, username, image, product_quantity, product_price, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, sanPham.getProduct_id());
            stmt.setString(2, sanPham.getProduct_name());
            stmt.setString(3, sanPham.getProduct_category());
            stmt.setString(4, sanPham.getUsername());
            stmt.setString(5, imageName);
            stmt.setInt(6, sanPham.getProduct_quantity());
            stmt.setInt(7, sanPham.getProduct_price());
            stmt.setString(8, sanPham.getStatus());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static DuplicateCheckResult checkProductDuplicate(String image, String username) {

        DuplicateCheckResult result = new DuplicateCheckResult();

        String sql = """
            SELECT image
            FROM product
            WHERE username = ?
            """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String dbImg = rs.getString("image");

                if (dbImg != null && dbImg.equalsIgnoreCase(image)) {
                    result.imageExists = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }



    // Cập nhật sản phẩm.
    public static boolean updateProduct(SanPham sanPham, String imageName) {
        Connection con = DatabaseConnection.getConnection();
        String sql = "UPDATE product SET product_name = ?, product_category = ?, username = ?, "
                + "image = ?, product_quantity = ?, product_price = ?, status = ? "
                + "WHERE product_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, sanPham.getProduct_name());
            stmt.setString(2, sanPham.getProduct_category());
            stmt.setString(3, sanPham.getUsername());
            stmt.setString(4, imageName);
            stmt.setInt(5, sanPham.getProduct_quantity());
            stmt.setInt(6, sanPham.getProduct_price());
            stmt.setString(7, sanPham.getStatus());
            stmt.setString(8, sanPham.getProduct_id());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa sản phẩm.
    public static boolean deleteProduct(String product_id) {

        Connection con = DatabaseConnection.getConnection();

        try {
            // 1. Xóa Cart
            PreparedStatement deleteCart = con.prepareStatement(
                    "DELETE FROM Cart WHERE product_id = ?"
            );
            deleteCart.setString(1, product_id);
            deleteCart.executeUpdate();

            // 2. Xóa Bill
            PreparedStatement deleteBill = con.prepareStatement(
                    "DELETE FROM Bill WHERE product_id = ?"
            );
            deleteBill.setString(1, product_id);
            deleteBill.executeUpdate();

            // 3. Xóa sản phẩm
            PreparedStatement deleteProduct = con.prepareStatement(
                    "DELETE FROM product WHERE product_id = ?"
            );
            deleteProduct.setString(1, product_id);

            return deleteProduct.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}