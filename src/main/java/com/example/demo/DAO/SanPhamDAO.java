package com.example.demo.DAO;
import com.example.demo.models.SanPham;
import com.example.demo.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SanPhamDAO {
    public static List<SanPham> getProductionList(){
        List<SanPham> list = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM SanPham");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String ma_san_pham = rs.getString("ma_san_pham");
                String ten_san_pham = rs.getString("ten_san_pham");
                byte[] image = rs.getBytes("image");
                String username = rs.getString("username");
                int so_luong = rs.getInt("so_luong");
                int gia = rs.getInt("gia");
                list.add(new SanPham(ma_san_pham, ten_san_pham, image, username, so_luong, gia));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return list;
    }
}
