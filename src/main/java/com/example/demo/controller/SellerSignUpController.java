package com.example.demo.controller;

// Models + DAO
import com.example.demo.DAO.UserDAO;
import com.example.demo.models.User;

// Utils
import com.example.demo.utils.StageSwitch;

// Java
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;

public class SellerSignUpController {
    @FXML
    public TextField usernameField;
    @FXML public PasswordField passwordField;
    @FXML public TextField fullnameField;
    @FXML public TextField phoneField;
    @FXML public Label lblMsg;
    @FXML public Button signupButton;
    @FXML public Button backButton;

    @FXML public void initialize(){
        signupButton.setOnAction(e -> handleSignUp());
        backButton.setOnAction(e -> handleBack());
    }

    @FXML public void handleSignUp(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        String fullname = fullnameField.getText();
        String phone = phoneField.getText();

        if(username.isEmpty() || password.isEmpty() || fullname.isEmpty() || phone.isEmpty()) {
            lblMsg.setText("Please fill all the fields");
            usernameField.clear();
            passwordField.clear();
            fullnameField.clear();
            phoneField.clear();
            return;
        }

        if(UserDAO.getUserFromDB(username) != null){
            lblMsg.setText("User already exists");
            usernameField.clear();
            passwordField.clear();
            fullnameField.clear();
            phoneField.clear();
            return;
        }

        User newUser = new User(username, password, fullname, phone, "seller");
        UserDAO.addUserIntoDB(newUser);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText("Đăng ký thành công");
        alert.setContentText("Tài khoản đã được tạo thành công.");
        alert.showAndWait();

        StageSwitch.switchSceneFromNode(signupButton, "login_view.fxml", "Login");
    }

    @FXML public void handleBack(){
        StageSwitch.switchSceneFromNode(backButton, "login_view.fxml", "Login");
    }

}
