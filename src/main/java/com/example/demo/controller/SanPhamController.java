package com.example.demo.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import com.example.demo.utils.StageSwitch;
import com.example.demo.utils.UserSession;
import com.example.demo.models.User;
import com.example.demo.models.SanPham;
import com.example.demo.DAO.SanPhamDAO;
import com.example.demo.DAO.GioHangDAO;
import javafx.stage.Stage;

import java.text.DecimalFormat;


public class SanPhamController {

    User user = UserSession.getCurrentUser();
    private SanPham sanPham;

    public void setSanPham(SanPham sp) {
        this.sanPham = sp;
        showProductionDetails();
    }
    @FXML private Label lblTenSanPham;
    @FXML private Label lblMaSanPham;
    @FXML private Label lblNguoiBan;
    @FXML private Label lblSoLuong;
    @FXML private Label lblGia;
    @FXML private Button btnAddToCart;
    @FXML private Button btnBack;
    @FXML private ImageView imgSanPham;
    @FXML private HBox quantityBox;
    @FXML private TextField txtQuantity;

    public void showProductionDetails() {
        lblMaSanPham.setText("Mã Sản Phẩm: " + sanPham.getMaSanPham());
        lblTenSanPham.setText("Tên Sản Phẩm: " + sanPham.getTenSanPham());
        lblNguoiBan.setText("Người Bán: " + sanPham.getUsername());
        lblSoLuong.setText("Số Lượng: " + String.valueOf(sanPham.getSoLuong()));
        DecimalFormat df = new DecimalFormat("#,###");
        lblGia.setText("Giá: " + df.format(sanPham.getGia()) + " VND");
        imgSanPham.setImage(sanPham.getImage());
    }

    @FXML
    public void handleAddToCart(){
        if(sanPham.getSoLuong() <= 0){
            return;
        }
        quantityBox.setVisible(true);
        quantityBox.setManaged(true);
        btnAddToCart.setVisible(false);
    }

    @FXML
    public void confirmAddToCart(){
        int quantity = Integer.parseInt(txtQuantity.getText());
        if(quantity <= sanPham.getSoLuong() && quantity > 0) {
            sanPham.setSoLuong(sanPham.getSoLuong() - quantity);
            SanPham addedItem = new SanPham(sanPham.getMaSanPham(), sanPham.getTenSanPham(), sanPham.getImage(), sanPham.getUsername(), quantity, sanPham.getGia());
            GioHangDAO.addSanPhamToCart(addedItem, user);
            SanPhamDAO.updateSanPham(sanPham);
        }
        // Reload Scene.
        showProductionDetails();
        quantityBox.setVisible(false);
        quantityBox.setManaged(false);
        txtQuantity.setText("");
        btnAddToCart.setVisible(true);
    }

    @FXML
    public void handleBack(){
        StageSwitch.switchSceneFromNode(btnBack, "customer_home.fxml", "Home");
    }

}
