package com.example.demo.DAO;

import com.example.demo.models.HoaDon;
import com.example.demo.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {
    // Lấy danh sách hóa đơn theo id.
    public static List<HoaDon> getHoaDonList(String id){
        Connection con = DatabaseConnection.getConnection();
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM bill WHERE bill_id=?";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(new HoaDon(
                        rs.getString("bill_id"),
                        rs.getString("username"),
                        rs.getString("product_id"),
                        rs.getInt("quantity"),
                        rs.getString("status")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
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
        String sql = "INSERT INTO Bill(bill_id, username, product_id, quantity, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, bill.getBill_id());
            stmt.setString(2, bill.getUsername());
            stmt.setString(3, bill.getProduct_id());
            stmt.setInt(4, bill.getQuantity());
            stmt.setString(5, bill.getStatus());

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
    // Lấy hóa đơn theo người bán (seller)
    public static List<HoaDon> getBillsForSeller(String sellerUsername) {
        List<HoaDon> bills = new ArrayList<>();

        String sql =
                "SELECT b.bill_id, b.username, b.product_id, b.quantity, b.status " +
                        "FROM Bill b JOIN product p ON b.product_id = p.product_id " +
                        "WHERE p.username = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, sellerUsername);
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

    public static boolean deleteBill(String bill_id) {
        String sql = "DELETE FROM Bill WHERE bill_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, bill_id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static List<HoaDon> getPaidBillsForSeller(String seller) {
        List<HoaDon> list = new ArrayList<>();

        String sql =
                "SELECT b.bill_id, b.username, b.product_id, b.quantity, b.status " +
                        "FROM Bill b JOIN product p ON b.product_id = p.product_id " +
                        "WHERE p.username = ? AND b.status = 'Paid'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, seller);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new HoaDon(
                        rs.getString("bill_id"),
                        rs.getString("username"),
                        rs.getString("product_id"),
                        rs.getInt("quantity"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean markAsPaid(String bill_id) {
        String sql = "UPDATE Bill SET status = 'Paid' WHERE bill_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, bill_id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // ===============================
// LẤY DANH SÁCH BILL GỘP CHO SELLER
// ===============================
    public static List<String> getBillsGroupForSeller(String seller) {
        List<String> billIds = new ArrayList<>();

        String sql = """
        SELECT DISTINCT b.bill_id
        FROM Bill b
        JOIN product p ON b.product_id = p.product_id
        WHERE p.username = ?
        ORDER BY b.bill_id DESC
    """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, seller);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                billIds.add(rs.getString("bill_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return billIds;
    }
    // ===============================
// LẤY DANH SÁCH SẢN PHẨM TRONG 1 HÓA ĐƠN
// ===============================
    public static List<HoaDon> getBillDetailsById(String billId) {
        List<HoaDon> list = new ArrayList<>();

        String sql = "SELECT * FROM Bill WHERE bill_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, billId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new HoaDon(
                        rs.getString("bill_id"),
                        rs.getString("username"),
                        rs.getString("product_id"),
                        rs.getInt("quantity"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    // ===============================
// CẬP NHẬT TRẠNG THÁI TOÀN BỘ BILL (GỘP)
// ===============================
    public static boolean updateBillStatus(String billId, String status) {
        String sql = "UPDATE Bill SET status = ? WHERE bill_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setString(2, billId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getPaidBillIdsForSeller(String seller) {
        List<String> list = new ArrayList<>();

        String sql =
                "SELECT DISTINCT b.bill_id " +
                        "FROM bill b " +
                        "JOIN product p ON b.product_id = p.product_id " +
                        "WHERE p.username = ? AND b.status = 'Paid'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, seller);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("bill_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Lấy toàn bộ chi tiết sản phẩm theo bill_id
    public static List<HoaDon> getBillDetails(String billId) {
        List<HoaDon> list = new ArrayList<>();

        String sql = "SELECT * FROM bill WHERE bill_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, billId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new HoaDon(
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
        return list;
    }

}
