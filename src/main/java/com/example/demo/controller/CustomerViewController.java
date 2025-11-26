package com.example.demo.controller;

// Models + DAO
import com.example.demo.DAO.SanPhamDAO;
import com.example.demo.models.GioHang;
import com.example.demo.DAO.GioHangDAO;
import com.example.demo.models.HoaDon;
import com.example.demo.DAO.HoaDonDAO;
import com.example.demo.models.SanPham;
import com.example.demo.models.User;

// Utils
import com.example.demo.utils.StageSwitch;
import com.example.demo.utils.UserSession;
import com.example.demo.utils.ProductView;

// Java
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.io.File;
import java.util.*;

public class CustomerViewController {
    User user = UserSession.getCurrentUser();
    @FXML public Button logoutButton;
    @FXML public StackPane contentPane;
    @FXML public Button btnDanhMuc;
    @FXML public Button btnGioHang;
    @FXML public Button btnDonHang;
    // Product View.
    public List<SanPham> productList;
    public List<SanPham> originalList;
    private VBox filterButtons = new VBox();
    private ScrollPane productView = new ScrollPane();
    private TextField searchBar;
    private String category = "";
    // Cart View.
    private List<GioHang> selectedList = new ArrayList<>();
    @FXML public void initialize() {
        originalList = SanPhamDAO.getAllProducts();
        productList = new ArrayList<>(originalList);

        // Top Buttons.
        logoutButton.setOnAction(e -> handleLogOut());
        btnDanhMuc.setOnAction(e -> {
            productList = SanPhamDAO.getAllProducts();
            handleProductView();
            highlightFilterButtons(filterButtons, (Button) filterButtons.getChildren().get(0));
        });
        btnGioHang.setOnAction(e -> handleGioHangView());
        btnDonHang.setOnAction(e -> handleDonHangView());


        // UI Components.
        searchBar = ProductView.createSearchBar();
        filterButtons = ProductView.createFilterButtons();
        filterButtons.setMaxWidth(Double.MAX_VALUE);

        productView.setContent(ProductView.createProductGrid(productList, product -> showProductDetails(product))); // ScrollPane.
        productView.setFitToWidth(true);
        productView.setStyle(
                "-fx-background: #C1E8FF;" +
                        "-fx-background-color: #C1E8FF;"
        );
        productView.setFitToWidth(true);
        productView.setMinWidth(400);
        productView.setMaxWidth(Double.MAX_VALUE);

        // Set actions cho các filter buttons.
        for (Node node : filterButtons.getChildren()) {
            if (node instanceof Button btn) {
                btn.setOnAction(e -> {
                    handleFilter(btn);
                    highlightFilterButtons(filterButtons, btn);
                    reloadContent();
                });
            }
        }
        handleProductView();
    }
    private void highlightButtons(Button selectedButton) {
        Button[] buttons = { btnDanhMuc, btnGioHang, btnDonHang };
        for (Button btn : buttons) {
            if (btn.equals(selectedButton)) {
                btn.setStyle(
                        "-fx-background-color: #176B87;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-border-color: #0E4D64;" +
                                "-fx-border-width: 1.5;" +
                                "-fx-background-radius: 5;"
                );
            } else {
                btn.setStyle(
                        "-fx-background-color: #021024;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 5;"
                );
            }
        }
    }
    private void highlightFilterButtons(VBox filterBox, Button selectedBtn) {
        for (Node node : filterBox.getChildren()) {
            if (node instanceof Button btn) {
                if (btn.equals(selectedBtn)) {
                    btn.setStyle("-fx-background-color: #176B87;" + "-fx-text-fill: white;");
                } else {
                    btn.setStyle("-fx-background-color: #1A3D63;" + "-fx-text-fill: white;");
                }
            }
        }
    }

    // ===================================== TAB XEM SẢN PHẨM ====================================
    private void handleProductView() {
        contentPane.getChildren().clear();
        highlightButtons(btnDanhMuc);
        originalList = SanPhamDAO.getAllProducts();
        productList = originalList;
        reloadContent();
        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {
            applySearchFilter(newVal);
            reloadContent();
        });

        VBox filterBox = new VBox(10);
        filterBox.getChildren().add(filterButtons);
        filterBox.setPrefWidth(180);
        filterBox.setMinWidth(180);
        filterBox.setMaxWidth(180);

        HBox mainContent = new HBox(15);
        mainContent.getChildren().addAll(filterBox, productView);
        mainContent.setStyle("-fx-background-color: #C1E8FF;");
        HBox.setHgrow(productView, Priority.ALWAYS);

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(searchBar, mainContent);
        layout.setStyle("-fx-background-color: #C1E8FF;");
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        contentPane.getChildren().add(layout);
    }

    // Lấy list sản phẩm theo phân loại.
    private void handleFilter(Button btn) {
        category = btn.getText();
        searchBar.clear();
        if(btn.getText().equals("Tất cả")) productList = SanPhamDAO.getAllProducts();
        else productList = SanPhamDAO.getProductsByFilter(btn.getText());
    }

    // Xử lý thanh tìm kiếm.
    private void applySearchFilter(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return;
        }
        String lower = keyword.toLowerCase();
        if(category.isEmpty() || category.equals("Tất cả")){
            productList = originalList.stream()
                    .filter(p -> p.getProduct_name().toLowerCase().contains(lower))
                    .toList();
        }else{
            productList = originalList.stream()
                    .filter(p -> p.getProduct_name().toLowerCase().contains(lower)
                            && p.getProduct_category().equals(category))
                    .toList();
        }
    }

    // Re-load ScrollPane.
    public void reloadContent() {
        productView.setContent(ProductView.createProductGrid(productList, product -> showProductDetails(product)));
    }


    // ========================================= TAB GIỎ HÀNG ===============================================
    private void handleGioHangView() {
        contentPane.getChildren().clear();
        highlightButtons(btnGioHang);

        Label lblTotalItems = new Label("Tổng số: 0");
        lblTotalItems.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label lblTotalPrice = new Label("Tổng giá: 0đ");
        lblTotalPrice.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Button btnConfirm = new Button("Xác nhận");

        // Xử lý xác nhận.
        btnConfirm.setOnAction(event -> submitBill());
        btnConfirm.setStyle("-fx-background-color: #0E4D64; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox header = new HBox(20, lblTotalItems, lblTotalPrice, btnConfirm);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #C1E8FF");
        header.setAlignment(Pos.CENTER_LEFT);

        VBox cartList = new VBox(12);
        cartList.setPadding(new Insets(10));

        ScrollPane scroll = new ScrollPane(cartList);
        scroll.setFitToWidth(true);

        VBox root = new VBox(10, header, new Separator(), scroll);
        root.setStyle("-fx-background-color: #C1E8FF;");

        contentPane.getChildren().add(root);

        loadCartItems(cartList, lblTotalItems, lblTotalPrice, btnConfirm);
    }

    // Load các card vào view tổng.
    private void loadCartItems(VBox cartList, Label lblTotalItems, Label lblTotalPrice, Button btnConfirm) {
        List<GioHang> list = GioHangDAO.getGioHangFromUser(user);

        cartList.getChildren().clear();

        for (GioHang item : list) {
            Node cell = createCartItem(item, lblTotalItems, lblTotalPrice, btnConfirm);
            cartList.getChildren().add(cell);
        }
    }

    // Tạo card cho mỗi sản phẩm.
    private Node createCartItem(GioHang item, Label lblTotalItems, Label lblTotalPrice, Button btnConfirm) {

        HBox box = new HBox(20);
        box.setStyle(
                "-fx-padding: 15;" +
                        "-fx-background-color: #ffffff;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #dcdfe6;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 6, 0, 0, 2);"
        );
        box.setAlignment(Pos.CENTER_LEFT);

        // Lấy thông tin sản phẩm
        SanPham sanPham = SanPhamDAO.getProductById(item.getProduct_id());

        // Hiển thị thông tin sản phẩm.
        StackPane imagePane = new StackPane();
        imagePane.setPrefSize(120, 120); // chiều rộng x chiều cao cố định
        imagePane.setMinSize(120, 120);
        imagePane.setMaxSize(120, 120);
        imagePane.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 10;"); // nền khung

        ImageView img = new ImageView(sanPham.getImage());
        img.setFitWidth(100);
        img.setFitHeight(100);
        img.setPreserveRatio(true);
        img.setSmooth(true);
        img.setCache(true);
        imagePane.getChildren().add(img);
        StackPane.setAlignment(img, Pos.CENTER);


        VBox info = new VBox(8);
        Label name = new Label(sanPham.getProduct_name());
        name.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        Label quantity = new Label("Số lượng: " + item.getQuantity());
        quantity.setStyle("-fx-font-size: 18;");
        Label price = new Label(
                "Tổng: " + String.format("%,d", sanPham.getProduct_price() * item.getQuantity()) + " VNĐ"
        );
        price.setStyle("-fx-font-size: 16; -fx-text-fill: #444;");

        info.getChildren().addAll(name, quantity, price);

        // Các nút thao tác.
        Button btnSelect = new Button("Chọn");
        btnSelect.setPrefSize(50, 10);
        btnSelect.setStyle("-fx-background-color: #176B87; -fx-text-fill: white;");

        Button btnDelete = new Button("Hủy");
        btnDelete.setPrefSize(50, 10);
        btnDelete.setStyle("-fx-background-color: #c62828; -fx-text-fill: white;");

        VBox btnBox = new VBox(8, btnSelect, btnDelete);
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(btnBox, Priority.ALWAYS);

        btnSelect.setOnAction(e -> {
            if (!selectedList.contains(item)) {
                selectedList.add(item);
                btnSelect.setText("Bỏ chọn");
                btnSelect.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
            } else {
                selectedList.remove(item);
                btnSelect.setText("Chọn");
                btnSelect.setStyle("-fx-background-color: #176B87; -fx-text-fill: white;");
            }
            updateSelectedInfo(lblTotalItems, lblTotalPrice);
        });

        // Loại sản phẩm khỏi giỏ hàng.
        btnDelete.setOnAction(e -> {
            // Hoàn lại số lượng.
            SanPhamDAO.updateProductQuantity(item.getProduct_id(), item.getQuantity() + sanPham.getProduct_quantity());
            GioHangDAO.removeProductFromCart(item.getProduct_id(), item.getUsername());
            selectedList.remove(item);
            updateSelectedInfo(lblTotalItems, lblTotalPrice);
            handleGioHangView();
        });
        box.getChildren().addAll(imagePane, info, btnBox);
        return box;
    }

    // Cập nhật danh sách sản phẩm khi click btnSelect.
    private void updateSelectedInfo(Label lblTotalItems, Label lblTotalPrice) {
        int totalItems = selectedList.stream()
                .mapToInt(GioHang::getQuantity)
                .sum();

        int totalPrice = selectedList.stream()
                .mapToInt(item -> {
                    SanPham sp = SanPhamDAO.getProductById(item.getProduct_id());
                    return sp.getProduct_price() * item.getQuantity();
                })
                .sum();

        lblTotalItems.setText("Tổng số: " + totalItems);
        lblTotalPrice.setText("Tổng giá: " + totalPrice + "đ");
    }

    // Tạo đơn hàng và cập nhật cart view khi xác nhận.
    public void submitBill(){
        String billID = "";
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        billID = sb.toString();
        for(int i = 0; i < selectedList.size(); i++){
            GioHang gh = selectedList.get(i);
            HoaDon hoadon = new HoaDon(billID, gh.getUsername(), gh.getProduct_id(), gh.getQuantity(), "Pending");
            HoaDonDAO.addBill(hoadon);
            GioHangDAO.removeProductFromCart(gh.getProduct_id(), gh.getUsername());
        }
        handleGioHangView();
    }



    // ========================================== HOA DON ================================================
    private void showQRCode(HoaDon bill) {
        String qrText =
                "BILL:" + bill.getBill_id() +
                        "|USER:" + bill.getUsername() +
                        "|PRODUCT:" + bill.getProduct_id() +
                        "|QTY:" + bill.getQuantity();

        String filePath = com.example.demo.utils.QRCodeGenerator.generateQRCode(qrText, bill.getBill_id());

        ImageView qrImg = new ImageView(new javafx.scene.image.Image(new File(filePath).toURI().toString()));
        qrImg.setFitWidth(250);
        qrImg.setFitHeight(250);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("QR Thanh toán");
        dialog.setHeaderText("Quét mã để thanh toán");

        dialog.getDialogPane().setContent(qrImg);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();

        HoaDonDAO.markAsPaid(bill.getBill_id());

    }

    private void showPaymentOptions(HoaDon bill) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Thanh toán");
        dialog.setHeaderText("Chọn phương thức thanh toán");

        ButtonType cashBtn = new ButtonType("Tiền mặt", ButtonBar.ButtonData.OK_DONE);
        ButtonType qrBtn = new ButtonType("QR Code", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(cashBtn, qrBtn, cancelBtn);

        dialog.showAndWait().ifPresent(type -> {

            if (type == cashBtn) {
                HoaDonDAO.markAsPaid(bill.getBill_id());
                showAlert("Thanh toán", "Thanh toán tiền mặt thành công!");
                handleDonHangView();
            }
            else if (type == qrBtn) {
                showQRCode(bill);
                HoaDonDAO.markAsPaid(bill.getBill_id());
                showAlert("Thanh toán", "Thanh toán QR thành công!");
                handleDonHangView();
            }

        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void cancelBill(List<HoaDon> bills) {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Hủy đơn hàng");
        confirm.setHeaderText("Bạn có chắc muốn hủy đơn?");
        confirm.setContentText("Mã hóa đơn: " + bills.get(0).getBill_id());

        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {

                for(HoaDon bill : bills) {
                    // Hoàn lại số lượng sản phẩm
                    SanPham sp = SanPhamDAO.getProductById(bill.getProduct_id());
                    int updatedQty = sp.getProduct_quantity() + bill.getQuantity();
                    SanPhamDAO.updateProductQuantity(bill.getProduct_id(), updatedQty);

                    // Xóa đơn hàng
                    HoaDonDAO.deleteBill(bill.getBill_id());
                }

                showAlert("Thành công", "Đã hủy đơn hàng.");
                handleDonHangView();
            }
        });
    }
    private void showBillDetailsDialog(String billId) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Chi tiết hóa đơn");
        dialog.setHeaderText("Mã hóa đơn: " + billId);

        VBox box = new VBox(12);
        box.setPadding(new Insets(10));

        List<HoaDon> list = HoaDonDAO.getBillDetailsById(billId);

        for (HoaDon hd : list) {
            SanPham sp = SanPhamDAO.getProductById(hd.getProduct_id());

            Label lbl = new Label(
                    "• " + sp.getProduct_name() +
                            " | SL: " + hd.getQuantity() +
                            " | Giá: " + sp.getProduct_price() + "đ"
            );
            lbl.setStyle("-fx-font-size:14;");
            box.getChildren().add(lbl);
        }

        dialog.getDialogPane().setContent(box);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    private void handleDonHangView() {

        contentPane.getChildren().clear();
        highlightButtons(btnDonHang);

        VBox root = new VBox(15);
        root.setPadding(new Insets(15));

        // Lấy ra các Bill_id riêng biệt.
        List<HoaDon> bills = HoaDonDAO.getUserBills(user.getUserName());
        Set<String> BillIds = new HashSet<String>();
        for (HoaDon bill : bills) {
            BillIds.add(bill.getBill_id());
        }

        if (BillIds.isEmpty()) {
            root.getChildren().add(new Label("❌ Bạn chưa có đơn hàng nào."));
            contentPane.getChildren().add(root);
            return;
        }

        for (String billId : BillIds) {

            // Lấy chi tiết từng sản phẩm trong hóa đơn
            List<HoaDon> details = HoaDonDAO.getBillDetailsById(billId);

            // Tính tổng tiền hóa đơn
            int total = 0;
            String status = details.get(0).getStatus();
            for (HoaDon hd : details) {
                SanPham sp = SanPhamDAO.getProductById(hd.getProduct_id());
                total += sp.getProduct_price() * hd.getQuantity();
            }

            // ========== TẠO CARD HIỂN THỊ HÓA ĐƠN ==========
            HBox card = new HBox(20);
            card.setPadding(new Insets(15));
            card.setStyle("""
            -fx-background-color: #ffffff;
            -fx-background-radius: 12;
            -fx-border-color: #d0d0d0;
            -fx-border-radius: 12;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 3);
        """);

            // Thông tin hóa đơn
            VBox info = new VBox(8);
            info.getChildren().addAll(
                    new Label("📦 Mã hóa đơn: " + billId),
                    new Label("💰 Tổng tiền: " + total + " VNĐ"),
                    new Label("📌 Trạng thái: " + status)
            );

            VBox actions = new VBox(10);

            Button showDetail = new Button("Chi tiết hóa đơn");
            showDetail.setStyle("-fx-background-color:#176B87; -fx-text-fill: white;");
            showDetail.setOnAction(e -> showBillDetailsDialog(billId));
            if (details.get(0).getStatus().equals("Approved")) {

                Button payBtn = new Button("Thanh toán");
                payBtn.setStyle("-fx-background-color:#176B87; -fx-text-fill: white;");
                payBtn.setOnAction(e -> showPaymentOptions(details.get(0)));


                Button cancelBtn = new Button("Hủy đơn");
                cancelBtn.setStyle("-fx-background-color:#c62828; -fx-text-fill:white;");
                cancelBtn.setOnAction(e -> cancelBill(HoaDonDAO.getHoaDonList(billId)));

                actions.getChildren().addAll(payBtn, cancelBtn,  showDetail);
            }

            // Đơn đang chờ seller duyệt → khách cũng có thể hủy
            else if (details.get(0).getStatus().equals("Pending")) {

                Label pending = new Label("Đang chờ người bán duyệt...");
                pending.setStyle("-fx-text-fill:orange;");

                Button cancelBtn = new Button("Hủy đơn");
                cancelBtn.setStyle("-fx-background-color:#c62828; -fx-text-fill:white;");
                cancelBtn.setOnAction(e -> cancelBill(HoaDonDAO.getHoaDonList(billId)));

                actions.getChildren().addAll(pending, cancelBtn, showDetail);
            }

            // Đơn bị seller từ chối
            else if (details.get(0).getStatus().equals("Denied")) {
                Label denied = new Label("Đơn hàng đã bị từ chối ❌");
                denied.setStyle("-fx-text-fill:red; -fx-font-weight:bold;");
                actions.getChildren().add(denied);
            }
            else if (details.get(0).getStatus().equals("Paid")) {
                Label paid = new Label("Đã thanh toán ✔");
                paid.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                actions.getChildren().add(paid);
            }


            card.getChildren().addAll(info, actions);
            root.getChildren().add(card);
        }

        ScrollPane scroll = new ScrollPane(root);
        scroll.setStyle("-fx-background-color: #C1E8FF; -fx-background: #C1E8FF");
        scroll.setFitToWidth(true);

        contentPane.getChildren().add(scroll);
    }



    // Log Out
    private void handleLogOut() {
        StageSwitch.switchSceneFromNode(logoutButton, "login_view.fxml", "Login");
    }

    private void showProductDetails(SanPham sanPham) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/product_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            ProductController controller = loader.getController();
            controller.setProduct(sanPham);
            stage.hide();
            stage.setScene(new Scene(root));
            stage.setTitle("Product Deltails");
            stage.setResizable(true);
            stage.show();
            Platform.runLater(() -> {
                stage.setMaximized(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
