package com.example.demo.DAO;

import com.example.demo.models.HoaDon;
import com.example.demo.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    // Lấy hóa đơn theo user
    public static List<HoaDon> getUserBills(String username) {
        List<HoaDon> bills = new ArrayList<>();
        String sql = "SELECT * FROM Bill WHERE username = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bills.add(new HoaDon(
                        rs.getString("bill_id"),
                        rs.getString("username"),
                        rs.getString("product_id"),
                        rs.getInt("quantity"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    // Thêm hóa đơn
    public static boolean addBill(HoaDon bill) {
        String sql = "INSERT INTO Bill(bill_id, username, product_id, quantity, cost, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, bill.getBill_id());
            stmt.setString(2, bill.getUsername());
            stmt.setString(3, bill.getProduct_id());
            stmt.setInt(4, bill.getQuantity());
            stmt.setString(6, bill.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái hóa đơn
    public static boolean updateStatus(String bill_id, String newStatus) {
        String sql = "UPDATE Bill SET status = ? WHERE bill_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setString(2, bill_id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
