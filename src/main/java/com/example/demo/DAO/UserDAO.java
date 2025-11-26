package com.example.demo.DAO;
import com.example.demo.models.User;
import com.example.demo.utils.DatabaseConnection;

import java.sql.*;

public class UserDAO {

    // Lấy dữ liệu người dùng từ DataBase.
    public static User getUserFromDB(String username){
        User user = null;
        Connection conn = DatabaseConnection.getConnection();
        try(PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE username = ?")){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                String _username=rs.getString("username");
                String _password=rs.getString("password");
                String _fullname=rs.getString("fullname");
                String _contact=rs.getString("contact");
                String _role=rs.getString("role");
                user = new User(_username,_password,_fullname,_contact, _role);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    // Thêm người dùng vào DataBase.
    public static void addUserIntoDB(User user){
        Connection conn = DatabaseConnection.getConnection();
        try(PreparedStatement stmt = conn.prepareStatement("INSERT INTO user VALUES (?, ?, ?, ?, ?)")){
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getContact());
            stmt.setString(5, user.getRole());
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
