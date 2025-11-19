package com.example.demo.controller;

// Models + DAO
import com.example.demo.models.User;
import com.example.demo.DAO.UserDAO;

// Utils
import com.example.demo.utils.StageSwitch;
import com.example.demo.utils.UserSession;

// Java
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {
    @FXML public PasswordField passwordField;
    @FXML public Button loginButton;
    @FXML public Button registerButton;
    @FXML public TextField usernameField;
    @FXML public Label lblMsg;

    @FXML
    public void initialize() {
        loginButton.setOnAction(e -> handleLogin());
        registerButton.setOnAction(e -> handleRegister());
    }

    // Xử lý nút "Đăng Nhập".
    public  void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(username.isEmpty() || password.isEmpty()) {
            lblMsg.setText("Please enter username and password");
            usernameField.clear();
            passwordField.clear();
            return;
        }
        User currentUser = UserDAO.getUserFromDB(username);
        if(currentUser == null) {
            lblMsg.setText("User not found");
            usernameField.clear();
            passwordField.clear();
            return;
        }
        if(!password.equals(currentUser.getPassword())) {
            lblMsg.setText("Passwords do not match");
            usernameField.clear();
            passwordField.clear();
            return;
        }
        UserSession.setCurrentUser(currentUser);
        if(currentUser.getRole().equals("customer")) {
            StageSwitch.switchSceneFromNode(loginButton, "customer_view.fxml", "Trang chủ");
        }
        if(currentUser.getRole().equals("seller")) {
            StageSwitch.switchSceneFromNode(loginButton, "seller_view.fxml", "Trang chủ");
        }
    }


    // Xử lý nút "Dăng Ký".
    public  void handleRegister() {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Chọn loại tài khoản");
        dialog.setHeaderText("Bạn muốn đăng ký với tư cách?");

        ButtonType customer = new ButtonType("Người mua");
        ButtonType seller = new ButtonType("Người bán");
        ButtonType cancel = new ButtonType("Hủy bỏ", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getButtonTypes().setAll(customer, seller, cancel);

        dialog.showAndWait().ifPresent(type -> {
            if(type == customer){
                StageSwitch.switchSceneFromNode(registerButton, "customer_signup.fxml", "Sign Up");
            }
            if(type == seller){
                StageSwitch.switchSceneFromNode(registerButton, "seller_signup.fxml", "Sign Up");
            }
        });
    }
}
