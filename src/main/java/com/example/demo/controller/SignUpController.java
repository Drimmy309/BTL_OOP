package com.example.demo.controller;

import com.example.demo.models.User;
import com.example.demo.DAO.UserDAO;
import com.example.demo.utils.StageSwitch;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SignUpController {
    @FXML private Button btnSignUp;
    @FXML private Button btnBack;
    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private Label lblStatus;
    @FXML private RadioButton rdoBuyer;
    @FXML private RadioButton rdoSeller;
    @FXML private ToggleGroup roleGroup;

    private String role = null;

    @FXML
    private void initialize() {
        rdoBuyer.setOnAction(e -> role = "customer");
        rdoSeller.setOnAction(e -> role = "seller");
    }

    @FXML
    private void handleSubmit() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Vui lòng điền đầy đủ thông tin!");
            return;
        }

        if (role == null) {
            lblStatus.setText("Vui lòng chọn vai trò!");
            return;
        }

        UserDAO userDAO = new UserDAO();
        User existingUser = userDAO.getUser(username);

        if (existingUser == null) {
            userDAO.addUser(username, password, role);
            lblStatus.setText("Đăng ký thành công!");
            StageSwitch.switchSceneFromNode(btnSignUp, "login_view.fxml", "Login Page");
        } else {
            lblStatus.setText("Tên đăng nhập đã tồn tại!");
            txtUsername.clear();
            txtPassword.clear();
        }
    }

    @FXML
    private void handleBack() {
        StageSwitch.switchSceneFromNode(btnBack, "login_view.fxml", "Login Page");
    }
}
