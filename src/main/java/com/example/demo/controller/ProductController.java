package com.example.demo.controller;

import com.example.demo.DAO.GioHangDAO;
import com.example.demo.DAO.SanPhamDAO;
import com.example.demo.models.GioHang;
import com.example.demo.models.SanPham;
import com.example.demo.models.User;
import com.example.demo.utils.StageSwitch;
import com.example.demo.utils.UserSession;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ProductController {
    User user = UserSession.getCurrentUser();
    private SanPham sanPham;

    @FXML public Button btnBack;
    @FXML public ImageView imgProduct;
    @FXML public Label lblName;
    @FXML public Label lblCategory;
    @FXML public Label lblQuantity;
    @FXML public Label lblPrice;
    @FXML public Button btnAddToCart;

    @FXML
    public void initialize() {
        btnAddToCart.setOnAction(e -> AddToCart());
        btnBack.setOnAction(e -> Back());
    }

    public void setProduct(SanPham sanPham) {
        this.sanPham = sanPham;
        updateUI();
    }

    private void updateUI() {
        if (sanPham == null) return;

        imgProduct.setImage(sanPham.getImage());
        lblName.setText(sanPham.getProduct_name());
        lblCategory.setText("Phân loại: " + sanPham.getProduct_category());
        if(sanPham.getProduct_quantity() > 0) {
            lblQuantity.setText("Số lượng: " + String.valueOf(sanPham.getProduct_quantity()));
            lblPrice.setStyle("-fx-text-fill: black; -fx-font-size: 16");
        }else{
            lblQuantity.setText("Đã hết hàng");
            lblPrice.setStyle("-fx-text-fill: red; -fx-font-size: 16");
        }
        lblPrice.setText("Giá: " + String.format("%,d", sanPham.getProduct_price()) + " VNĐ");
    }

    private void AddToCart() {
        if(sanPham.getProduct_quantity() <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Thông báo");
            alert.setContentText( "Sản phẩm hiện đã hết hàng");
            alert.showAndWait();
            return;
        }
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Thêm vào giỏ hàng");
        dialog.setHeaderText("Chọn số lượng muốn thêm");

        ButtonType confirmButton = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);

        TextField txtQuantity = new TextField();
        txtQuantity.setPromptText("Nhập số lượng...");
        txtQuantity.setPrefWidth(100);

        VBox box = new VBox(10);
        box.getChildren().addAll(new Label("Chọn số lượng:"), txtQuantity);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButton) {
                try {
                    int quantity = Integer.parseInt(txtQuantity.getText().trim());

                    if (quantity <= 0 || quantity > sanPham.getProduct_quantity()) {
                        throw new IllegalArgumentException();
                    }
                    return quantity;

                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Lỗi nhập liệu");
                    alert.setContentText( "Vui lòng nhập số hợp lệ từ 1 đến " + sanPham.getProduct_quantity());
                    alert.showAndWait();
                    return null;
                }
            }
            return null;
        });

        // Xử lý khi người dùng nhập giá trị hợp lệ.
        dialog.showAndWait().ifPresent(selectedNumber -> {
            GioHang gioHang = GioHangDAO.GetProductFromCart(sanPham.getProduct_id(), user.getUserName());
            if (gioHang != null) {
                GioHangDAO.updateQuantity(sanPham.getProduct_id(), user.getUserName(), selectedNumber + gioHang.getQuantity());
            }else{
                GioHangDAO.AddProductToCart(sanPham.getProduct_id(), user.getUserName(), selectedNumber);
            }
            SanPhamDAO.updateProductQuantity(sanPham.getProduct_id(), sanPham.getProduct_quantity() -  selectedNumber);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Thành công");
            alert.setContentText("Đã thêm " + selectedNumber + " sản phẩm vào giỏ hàng.");
            alert.show();
            sanPham.setProduct_quantity(sanPham.getProduct_quantity() - selectedNumber);
            updateUI();
        });
    }


    public void Back() {
        StageSwitch.switchSceneFromNode(btnBack, "customer_view.fxml", "Home");
    }
}

