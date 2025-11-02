package com.example.demo.DAO;
import com.example.demo.models.User;
import com.example.demo.utils.DatabaseConnection;
import com.example.demo.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public User getUser(String username) {
        Connection con = DatabaseConnection.getConnection();
        String query = "SELECT * FROM User WHERE username = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                String _username = rs.getString("username");
                String _password = rs.getString("password");
                String _role = rs.getString("role");
                return new User(_username, _password, _role);
            }else{
                return null;
            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }
}
