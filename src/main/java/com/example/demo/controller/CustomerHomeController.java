package com.example.demo.controller;

import com.example.demo.DAO.HoaDonDAO;
import com.example.demo.models.HoaDon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;

import com.example.demo.utils.StageSwitch;
import com.example.demo.DAO.SanPhamDAO;
import com.example.demo.models.SanPham;
import com.example.demo.DAO.GioHangDAO;
import com.example.demo.models.GioHang;
import com.example.demo.utils.UserSession;
import com.example.demo.models.User;

public class CustomerHomeController {
    User user = UserSession.getCurrentUser();
    @FXML private Button btnProductList;
    @FXML private Button btnCart;
    @FXML private Button btnOrders;
    @FXML private Button btnLogout;
    @FXML private StackPane contentArea;

    private final List<Button> btnList = new ArrayList<>();

    @FXML
    private void initialize() {
        btnList.add(btnProductList);
        btnList.add(btnCart);
        btnList.add(btnOrders);
        handleShowProductList();
    }

    // Hightlight Button đang được dùng.
    private void hightlightButton(Button button) {
        for (Button btn : btnList) {
            if (btn.equals(button)) {
                btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT");
            } else {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT");
            }
        }
    }

    // XEM DANH SÁCH SẢN PHẨM Ở TAB "DANH SÁCH SẢN PHẨM".
    @FXML
    private void handleShowProductList() {
        hightlightButton(btnProductList);
        List<SanPham> productionList = SanPhamDAO.getProductionList();

        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setPadding(new Insets(30));
        grid.setStyle("-fx-background-color: #f8f9fa;");

        int col = 0, row = 0;
        for (SanPham sanPham : productionList) {
            VBox card = new VBox(10);
            card.setStyle("""
            -fx-background-color: white;
            -fx-padding: 15;
            -fx-alignment: center;
            -fx-background-radius: 12;
            -fx-border-color: #e0e0e0;
            -fx-border-radius: 12;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);
        """);
            card.setPrefWidth(200);

            // Hình ảnh sản phẩm
            ImageView imageView = new ImageView();
            imageView.setFitWidth(160);
            imageView.setFitHeight(160);
            imageView.setPreserveRatio(true);
            imageView.setImage(sanPham.getImage());

            // Tên sản phẩm
            Label productName = new Label(sanPham.getTenSanPham());
            productName.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #2c3e50;");

            // Giá sản phẩm
            Label price = new Label(String.format("%,d VND", sanPham.getGia()));
            price.setStyle("-fx-font-size: 14px; -fx-text-fill: #27ae60; -fx-font-weight: bold;");

            card.getChildren().addAll(imageView, productName, price);
            card.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleViewSanPham(card, sanPham));

            grid.add(card, col++, row);
            if (col == 4) {
                col = 0;
                row++;
            }
        }
        grid.setAlignment(Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");

        contentArea.getChildren().setAll(scrollPane);
    }


    // XEM CHI TIẾT SẢN PHẨM KHI CLICK VÀO SẢN PHẨM.
    private void handleViewSanPham(Node currentNode, SanPham sanPham) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/san_pham_view.fxml"));
            Parent root = loader.load(); // ✅ Load FXML trước!

            SanPhamController sanPhamController = loader.getController();
            sanPhamController.setSanPham(sanPham);

            Stage stage = (Stage) currentNode.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.hide();
            stage.setScene(scene);
            stage.setTitle("Product Details");
            stage.setResizable(true);
            stage.show();
            stage.setMaximized(true);

            Platform.runLater(() -> stage.setMaximized(true));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    // XEM GIỎ HÀNG + QUYẾT ĐỊNH HỦY/MUA HÀNG.
    @FXML
    private void handleShowCart() {
        hightlightButton(btnCart);
        contentArea.getChildren().clear();

        GioHang gh = new GioHang(GioHangDAO.getGioHangFromUser(user));
        List<SanPham> cart = gh.getSanPham();

        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setPadding(new Insets(30));
        grid.setStyle("-fx-background-color: #f8f9fa;");

        int col = 0, row = 0;
        for (SanPham sanPham : cart) {
            VBox card = new VBox(10);
            card.setStyle("""
            -fx-background-color: white;
            -fx-padding: 15;
            -fx-alignment: center;
            -fx-background-radius: 12;
            -fx-border-color: #e0e0e0;
            -fx-border-radius: 12;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);
        """);
            card.setPrefWidth(200);

            ImageView imageView = new ImageView();
            imageView.setFitWidth(160);
            imageView.setFitHeight(160);
            imageView.setPreserveRatio(true);
            imageView.setImage(sanPham.getImage());

            Label lblTen = new Label(sanPham.getTenSanPham());
            lblTen.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #2c3e50;");

            Label lblSoLuong = new Label("Số lượng: " + sanPham.getSoLuong());
            lblSoLuong.setStyle("-fx-font-size: 13px; -fx-text-fill: #555;");

            DecimalFormat df = new DecimalFormat("#,###");
            Label lblTongGia = new Label("Tổng: " + df.format(sanPham.getSoLuong() * sanPham.getGia()) + " VND");
            lblTongGia.setStyle("-fx-font-size: 14px; -fx-text-fill: #27ae60; -fx-font-weight: bold;");

            Button btnBuy = new Button("Mua");
            btnBuy.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
            btnBuy.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handlePurchase(sanPham, user));

            Button btnCancel = new Button("Hủy");
            btnCancel.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
            btnCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleCancel(sanPham, user));

            HBox buttonBox = new HBox(10, btnBuy, btnCancel);
            buttonBox.setStyle("-fx-alignment: center;");

            card.getChildren().addAll(imageView, lblTen, lblSoLuong, lblTongGia, buttonBox);

            grid.add(card, col++, row);
            if (col == 4) {
                col = 0;
                row++;
            }
        }
        grid.setAlignment(Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");

        contentArea.getChildren().setAll(scrollPane);
    }


    // XỬ LÝ BUTTON BUY.
    private void handlePurchase(SanPham sanPham, User user) {
        GioHangDAO.buySanPhamFromCart(sanPham, user);
        contentArea.getChildren().clear();
        handleShowCart();
    }

    // XỬ LÝ BUTTON HỦY.
    private void handleCancel(SanPham sanPham, User user) {
        GioHangDAO.deleteSanPhamFromCart(sanPham, user);
        contentArea.getChildren().clear();
        handleShowCart();
    }

    // XEM ĐƠN HÀNG ĐÃ MUA.
    @FXML
    private void handleShowOrders() {
        hightlightButton(btnOrders);
        contentArea.getChildren().clear();

        List<HoaDon> hoaDons = HoaDonDAO.getHoaDonFromUser(user);

        VBox container = new VBox(15);
        container.setPadding(new Insets(20));

        if (hoaDons.isEmpty()) {
            Label emptyLabel = new Label("Bạn chưa đặt đơn hàng nào.");
            emptyLabel.setStyle("-fx-font-size: 16; -fx-text-fill: gray;");
            container.getChildren().add(emptyLabel);
        } else {
            for (HoaDon hoaDon : hoaDons) {
                VBox card = new VBox(8);
                card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; "
                        + "-fx-border-color: #dcdcdc; -fx-border-radius: 10;");
                card.setPrefWidth(600);

                Label lblMa = new Label("🧾 Mã hóa đơn: " + hoaDon.getMa_hoa_don());
                lblMa.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");

                Label lblTen = new Label("📦 Sản phẩm: " + hoaDon.getTen_san_pham());
                lblTen.setStyle("-fx-font-size: 14px;");

                Label lblSoLuong = new Label("Số lượng: " + hoaDon.getSo_luong());
                lblSoLuong.setStyle("-fx-font-size: 14px;");

                DecimalFormat df = new DecimalFormat("#,###");
                Label lblTongGia = new Label("💰 Tổng giá: " + df.format(hoaDon.getGia()) + " VND");
                lblTongGia.setStyle("-fx-font-size: 14px; -fx-text-fill: #27ae60; -fx-font-weight: bold;");

                Label lblStatus = new Label();
                String status = hoaDon.getStatus();

                switch (status) {
                    case "Pending":
                        lblStatus.setText("Trạng thái: ⏳ Đang chờ xác nhận");
                        lblStatus.setTextFill(Color.ORANGE);
                        break;
                    case "Denied":
                        lblStatus.setText("Trạng thái: ❌ Đơn hàng bị từ chối");
                        lblStatus.setTextFill(Color.RED);
                        break;
                    case "Completed":
                        lblStatus.setText("Trạng thái: ✅ Đơn hàng đã được xác nhận");
                        lblStatus.setTextFill(Color.GREEN);
                        break;
                    default:
                        lblStatus.setText("Trạng thái: " + status);
                        lblStatus.setTextFill(Color.BLACK);
                }

                card.getChildren().addAll(lblMa, lblTen, lblSoLuong, lblTongGia, lblStatus);
                container.getChildren().add(card);
            }
        }
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");

        contentArea.getChildren().setAll(scrollPane);
    }


    // XỬ LÝ LOGOUT.
    @FXML
    private void handleLogout() {
        StageSwitch.switchSceneFromNode(btnLogout, "login_view.fxml", "Login");
    }


}

