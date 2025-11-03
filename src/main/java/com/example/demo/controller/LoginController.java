package com.example.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import com.example.demo.models.User;
import com.example.demo.DAO.UserDAO;
import com.example.demo.utils.UserSession;
import com.example.demo.utils.StageSwitch;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private Label lblStatus;
    @FXML private Button btnLogin;
    @FXML private void handleLogin(){
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()){
            lblStatus.setText("Vui lòng điền đầy đủ thông tin!");
            return;
        }else{
            UserDAO userDAO = new UserDAO();
            User currentUser = userDAO.getUser(username);
            if (currentUser == null){
                lblStatus.setText("Sai Username. Thử lại!");
                txtUsername.clear();
                txtPassword.clear();
                return;
            }else if (!currentUser.getPassword().equals(password)){
                lblStatus.setText("Sai Password. Thử lại!");
                txtUsername.clear();
                txtPassword.clear();
                return;
            }else{
                UserSession.setCurrentUser(currentUser);
                if (currentUser.getRole().equals("mua")){
                    StageSwitch.switchSceneFromNode(btnLogin, "customer_home.fxml", "HomePage");
                }else{
                    StageSwitch.switchSceneFromNode(btnLogin, "seller_home.fxml", "Login");
                }
                return;
            }
        }


    };
}
