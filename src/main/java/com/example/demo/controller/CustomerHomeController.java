package com.example.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.util.List;
import java.util.ArrayList;

import com.example.demo.DAO.SanPhamDAO;
import com.example.demo.utils.UserSession;
import com.example.demo.models.User;
import com.example.demo.models.SanPham;

public class CustomerHomeController {
    User user = UserSession.getCurrentUser();
    @FXML private Button btnProductList;
    @FXML private Button btnCart;
    @FXML private Button btnOrders;
    @FXML private Button btnLogout;
    @FXML private Label lblWelcome;
    @FXML private StackPane contentArea;

    private List<Button> btnList = new ArrayList<>();

    @FXML
    private void initialize(){
        btnList.add(btnProductList);
        btnList.add(btnCart);
        btnList.add(btnOrders);
    }
    private void hightlightButton(Button button) {
        for (Button btn : btnList) {
            if (btn.equals(button)) {
                btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT");
            } else {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT");
            }
        }
    }

    @FXML private void handleShowProductList(){
        hightlightButton(btnProductList);
    }
    @FXML private void handleShowCart(){
        hightlightButton(btnCart);
    }
    @FXML private void handleShowOrders(){
        hightlightButton(btnOrders);
    }
    @FXML private void handleLogout(){
    }
}
