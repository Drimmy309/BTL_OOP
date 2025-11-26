package com.example.demo.DAO;

import com.example.demo.models.GioHang;
import com.example.demo.models.User;
import com.example.demo.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GioHangDAO {
    public static List<GioHang> getGioHangFromUser(User user) {
        List<GioHang> Cart = new ArrayList<GioHang>();
        Connection connection = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM cart WHERE username = ?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, user.getUserName());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Cart.add(new GioHang(rs.getString("product_id"),
                                     rs.getString("username"),
                                     rs.getInt("quantity")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return Cart;
    }

    // Nếu sản phẩm chưa có trong giỏ hàng -> Insert dữ liệu mới.
    public static void AddProductToCart (String product_id, String username, int quantity) {
        Connection connection = DatabaseConnection.getConnection();
        String sql = "INSERT INTO cart VALUES (?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, product_id);
            stmt.setString(2, username);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // Nếu sản phẩm được người dùng thêm vào trước đó -> Update lại số lượng.
    public static void updateQuantity(String product_id, String username, int quantity) {
        Connection connection = DatabaseConnection.getConnection();
        String sql = "UPDATE cart SET quantity = ? WHERE username = ? AND  product_id = ?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, quantity);
            stmt.setString(2, username);
            stmt.setString(3, product_id);
            stmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // Kiểm tra sản phẩm đã được thêm vào giỏ hàng chưa.
    public static GioHang GetProductFromCart(String product_id, String username) {
        Connection connection = DatabaseConnection.getConnection();
        GioHang res = null;
        String sql = "SELECT * FROM cart WHERE username = ? AND  product_id = ?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, product_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                res = new GioHang(rs.getString("product_id"), rs.getString("username"), rs.getInt("quantity"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    // Loại sản phẩm ra khỏi giỏ hàng sau khi đã xác nhận hoặc hủy bỏ.
    public static void removeProductFromCart(String product_id, String username){
        Connection connection = DatabaseConnection.getConnection();
        String sql = "DELETE FROM cart WHERE username = ? AND  product_id = ?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, product_id);
            stmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
