package com.example.demo.controller;

// Models + DAO
import com.example.demo.DAO.SanPhamDAO;
import com.example.demo.models.SanPham;
import com.example.demo.models.User;

// Utils
import com.example.demo.utils.StageSwitch;
import com.example.demo.utils.UserSession;
import com.example.demo.utils.ProductView;

// Java
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class CustomerViewController {
    User user = UserSession.getCurrentUser();
    @FXML public Button logoutButton;
    @FXML public StackPane contentPane;
    @FXML public Button btnDanhMuc;
    @FXML public Button btnGioHang;
    @FXML public Button btnDonHang;

    @FXML public void initialize() {
        logoutButton.setOnAction(e -> handleLogOut());
        btnDanhMuc.setOnAction(e -> handleProductView());
        btnGioHang.setOnAction(e -> handleGioHangView());
        btnDonHang.setOnAction(e -> handleDonHangView());
    }

    private void handleProductView() {
        List<SanPham> productList = SanPhamDAO.getAllProducts();

        TextField searchBar = ProductView.createSearchBar();
        ScrollPane productView = ProductView.createProductGrid(productList);
        VBox filterButtons = ProductView.createFilterButtons();
        filterButtons.setMaxWidth(Double.MAX_VALUE);
        productView.setMaxWidth(Double.MAX_VALUE);
        productView.setFitToWidth(true);

        HBox hbox = new HBox(15);
        hbox.getChildren().addAll(filterButtons, productView);

        // Tỷ lệ giãn
        HBox.setHgrow(filterButtons, Priority.SOMETIMES);
        HBox.setHgrow(productView, Priority.ALWAYS);

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(searchBar, hbox);
        VBox.setVgrow(hbox, Priority.ALWAYS);
        vbox.setFillWidth(true);

        contentPane.getChildren().clear();
        contentPane.getChildren().add(vbox);

        filterButtons.getChildren();
    }


    private void handleGioHangView() {}
    private void handleDonHangView() {}

    // Log Out
    private void handleLogOut() {
        StageSwitch.switchSceneFromNode(logoutButton, "login_view.fxml", "Login");
    }
}
