package com.example.demo.controller;

import com.example.demo.DAO.HoaDonDAO;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import com.example.demo.DAO.SanPhamDAO;
import com.example.demo.models.SanPham;
import com.example.demo.utils.StageSwitch;
import com.example.demo.utils.UserSession;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javafx.scene.paint.Color;


public class SellerHomeController {

    @FXML private StackPane contentArea;
    @FXML private Button btnLogout;

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(this::handleShowProductList);
            }).start();
        });
    }

    //Load lại danh sách sản phẩm sau khi thêm, sửa hoặc xóa
    private void loadProducts() {
        handleShowProductList();
    }

    @FXML
    private void handleShowProductList() {
        System.out.println("Hiển thị danh sách sản phẩm của người bán...");

        String seller = UserSession.getCurrentUser().getUsername();
        List<SanPham> products = SanPhamDAO.getProductionListByUser(seller);

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);
        grid.setPadding(new Insets(10));

        int col = 0, row = 0;
        for (SanPham sp : products) {
            VBox card = new VBox(6);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 10; -fx-border-color: #bdc3c7;");
            ImageView imageview = new ImageView(sp.getImage());
            imageview.setFitWidth(120);
            imageview.setFitHeight(120);
            imageview.setPreserveRatio(true);

            Label name = new Label(sp.getTenSanPham());
            name.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            Label price = new Label("Giá: " + sp.getGia() + " đ");
            Label quantity = new Label("Số lượng: " + sp.getSoLuong());

            Button editBtn = new Button("Sửa");
            Button deleteBtn = new Button("Xóa");

            editBtn.setOnAction(e -> handleEditProduct(sp));
            deleteBtn.setOnAction(e -> handleDeleteProduct(sp));

            VBox actions = new VBox(5, editBtn, deleteBtn);
            card.getChildren().addAll(imageview, name, price, quantity, actions);
            grid.add(card, col++, row);
            if (col == 3) { col = 0; row++; }
        }

        // ➕ Tạo card "Thêm sản phẩm"
        VBox addCard = new VBox();
        addCard.setPadding(new Insets(10));
        addCard.setStyle("-fx-background-color: #d5f5e3; -fx-background-radius: 10; -fx-border-color: #2ecc71;");
        addCard.setPrefSize(140, 180);
        addCard.setAlignment(javafx.geometry.Pos.CENTER);

        Button addButton = new Button("+");
        addButton.setStyle(
                "-fx-font-size: 36px; -fx-background-color: #27ae60; -fx-text-fill: white; " +
                        "-fx-background-radius: 50%; -fx-min-width: 80px; -fx-min-height: 80px;"
        );
        addButton.setOnAction(e -> handleAddProduct());

        Label addLabel = new Label("Thêm sản phẩm");
        addLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #145a32; -fx-font-weight: bold;");
        addCard.getChildren().addAll(addButton, addLabel);
        grid.add(addCard, col, row);

        // 🧩 Bọc toàn bộ grid trong ScrollPane để có thể cuộn
        grid.setAlignment(javafx.geometry.Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true); // Cho phép co giãn theo chiều ngang
        scrollPane.setPannable(true);   // Cho phép kéo bằng chuột
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Tự động hiện khi tràn
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);     // Không cuộn ngang
        scrollPane.setStyle("-fx-background-color: transparent;");

        // ➕ Hiển thị ScrollPane trong contentArea
        contentArea.getChildren().setAll(scrollPane);
    }

    @FXML
    private void handleAddProduct() {
        Dialog<SanPham> dialog = new Dialog<>();
        dialog.setTitle("Thêm sản phẩm mới");
        dialog.setHeaderText("Nhập thông tin sản phẩm");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        TextField ten = new TextField();
        TextField soLuong = new TextField();
        TextField gia = new TextField();

        // === Thêm chọn ảnh ===
        Button btnChonAnh = new Button("Chọn ảnh...");
        Label lblTenFile = new Label("Chưa chọn ảnh");
        ImageView preview = new ImageView();
        preview.setFitWidth(100);
        preview.setFitHeight(100);
        preview.setPreserveRatio(true);

        final String[] selectedImagePath = {null};

        btnChonAnh.setOnAction(e -> {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Chọn ảnh sản phẩm");
            fileChooser.getExtensionFilters().addAll(
                    new javafx.stage.FileChooser.ExtensionFilter("Hình ảnh", "*.png", "*.jpg", "*.jpeg", "*.jfif", "*.webp")
            );
            java.io.File file = fileChooser.showOpenDialog(dialog.getDialogPane().getScene().getWindow());
            if (file != null) {
                lblTenFile.setText(file.getName());
                preview.setImage(new javafx.scene.image.Image(file.toURI().toString()));
                selectedImagePath[0] = file.getAbsolutePath();
            }
        });

        grid.addRow(0, new Label("Tên sản phẩm:"), ten);
        grid.addRow(1, new Label("Số lượng:"), soLuong);
        grid.addRow(2, new Label("Giá:"), gia);
        grid.addRow(3, new Label("Ảnh:"), btnChonAnh, lblTenFile);
        grid.add(preview, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                return new SanPham(
                        "SP" + System.currentTimeMillis(),
                        ten.getText(),
                        null,
                        UserSession.getCurrentUser().getUsername(),
                        Integer.parseInt(soLuong.getText()),
                        Integer.parseInt(gia.getText())
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(sp -> {
            if (selectedImagePath[0] == null) {
                showAlert("Thiếu ảnh!", "Vui lòng chọn ảnh sản phẩm trước khi thêm.");
                return;
            }

            try {
                java.nio.file.Path source = java.nio.file.Paths.get(selectedImagePath[0]);
                String fileName = source.getFileName().toString();

                // 🧩 Tự động đổi đuôi file nếu không phải định dạng chuẩn
                if (fileName.endsWith(".jfif")) {
                    fileName = fileName.replace(".jfif", ".jpg");
                } else if (fileName.endsWith(".webp")) {
                    fileName = fileName.replace(".webp", ".png");
                }

                // ✅ Copy ảnh vào thư mục resources (nơi Customer load được)
                java.nio.file.Path dest = java.nio.file.Paths.get("src/main/resources/com/example/demo/images/" + fileName);
                java.nio.file.Files.copy(source, dest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // ✅ Lưu vào DB CHỈ tên file, không có đường dẫn
                boolean success = SanPhamDAO.insertProduct(sp, fileName);

                if (success) {
                    showAlert("Thành công", "Thêm sản phẩm thành công!");
                    loadProducts(); // cập nhật lại danh sách sản phẩm
                } else {
                    showAlert("Lỗi", "Không thể thêm sản phẩm!");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Lỗi", "Không thể lưu ảnh sản phẩm: " + ex.getMessage());
            }
        });
    }


    private void handleEditProduct(SanPham sp) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Chỉnh sửa sản phẩm");

        TextField txtTen = new TextField(sp.getTenSanPham());
        TextField txtGia = new TextField(String.valueOf(sp.getGia()));
        TextField txtSoLuong = new TextField(String.valueOf(sp.getSoLuong()));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.addRow(0, new Label("Tên sản phẩm:"), txtTen);
        grid.addRow(1, new Label("Giá:"), txtGia);
        grid.addRow(2, new Label("Số lượng:"), txtSoLuong);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                sp.setTenSanPham(txtTen.getText());
                sp.setGia(Integer.parseInt(txtGia.getText()));
                sp.setSoLuong(Integer.parseInt(txtSoLuong.getText()));

                boolean updated = SanPhamDAO.updateProduct(sp);
                if (updated) {
                    showAlert("Thành công", "Đã sửa sản phẩm!");
                    handleShowProductList();
                } else {
                    showAlert("Lỗi", "Không thể sửa sản phẩm.");
                }
            }
        });
    }

    private void handleDeleteProduct(SanPham sp) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xóa sản phẩm");
        confirm.setHeaderText("Bạn có chắc muốn xóa?");
        confirm.setContentText(sp.getTenSanPham());

        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                boolean success = SanPhamDAO.deleteProduct(sp.getMaSanPham());
                if (success) {
                    showAlert("Thành công", "Đã xóa sản phẩm!");
                    handleShowProductList();
                } else {
                    showAlert("Lỗi", "Không thể xóa sản phẩm.");
                }
            }
        });
    }

    @FXML
    private void handleShowOrders() {
        System.out.println("📦 Hiển thị danh sách đơn hàng đã bán...");

        // 🔹 Lấy người bán hiện tại
        var currentSeller = UserSession.getCurrentUser();
        if (currentSeller == null) {
            contentArea.getChildren().setAll(new Label("Không thể tải dữ liệu. Vui lòng đăng nhập lại."));
            return;
        }

        // 🔹 Lấy danh sách đơn hàng thuộc sản phẩm của người bán
        var orders = com.example.demo.DAO.HoaDonDAO.getHoaDonFromSeller(currentSeller);

        VBox container = new VBox(12);
        container.setPadding(new Insets(15));

        if (orders.isEmpty()) {
            Label emptyLabel = new Label("Chưa có đơn hàng nào được đặt.");
            emptyLabel.setStyle("-fx-font-size: 15; -fx-text-fill: gray;");
            container.getChildren().add(emptyLabel);
        } else {
            for (var hd : orders) {
                VBox card = new VBox(8);
                card.setPadding(new Insets(10));
                card.setSpacing(5);
                card.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #bdc3c7; -fx-background-radius: 10;");
                card.setPrefWidth(600);

                // 🧾 Thông tin đơn hàng
                Label lblMa = new Label("Mã đơn hàng: " + hd.getMa_hoa_don());
                Label lblTen = new Label("Sản phẩm: " + hd.getTen_san_pham());
                Label lblBuyer = new Label("Người mua: " + hd.getUsername());
                Label lblSoLuong = new Label("Số lượng: " + hd.getSo_luong());
                Label lblTong = new Label("Tổng giá: " + hd.getGia() + " đ");

                // 🟡 Trạng thái đơn hàng
                Label lblStatus = new Label("Trạng thái: " + hd.getStatus());
                String status = hd.getStatus().toLowerCase();
                if (status.contains("pending")) lblStatus.setTextFill(Color.ORANGE);
                else if (status.contains("completed")) lblStatus.setTextFill(Color.GREEN);
                else if (status.contains("denied")) lblStatus.setTextFill(Color.RED);
                else lblStatus.setTextFill(Color.DARKBLUE);

                // 🟢 Các nút hành động (chỉ hiển thị nếu đơn chưa xác nhận)
                HBox actions = new HBox(10);
                actions.setPadding(new Insets(5, 0, 0, 0));

                if (status.contains("pending")) {
                    Button btnConfirm = new Button("✅ Xác nhận");
                    btnConfirm.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
                    btnConfirm.setOnAction(e -> {
                        boolean ok = HoaDonDAO.updateStatus(hd.getMa_hoa_don(), "Completed");
                        if (ok) {
                            showAlert("Thành công", "Đã xác nhận đơn hàng!");
                            handleShowOrders();
                        } else {
                            showAlert("Lỗi", "Không thể cập nhật đơn hàng.");
                        }
                    });

                    Button btnDeny = new Button("❌ Từ chối");
                    btnDeny.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");
                    btnDeny.setOnAction(e -> {
                        boolean ok = HoaDonDAO.updateStatus(hd.getMa_hoa_don(), "Denied");
                        if (ok) {
                            showAlert("Đã từ chối đơn hàng", "Đơn hàng đã bị từ chối.");
                            handleShowOrders();
                        } else {
                            showAlert("Lỗi", "Không thể cập nhật đơn hàng.");
                        }
                    });

                    actions.getChildren().addAll(btnConfirm, btnDeny);
                }

                card.getChildren().addAll(lblMa, lblTen, lblBuyer, lblSoLuong, lblTong, lblStatus, actions);
                container.getChildren().add(card);
            }
        }

        // 🧩 Thêm ScrollPane để có thể cuộn
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");
        contentArea.getChildren().setAll(scrollPane);
    }


    @FXML
    private void handleShowRevenue() {
        System.out.println("📊 Hiển thị thống kê doanh thu...");

        var currentSeller = UserSession.getCurrentUser();
        if (currentSeller == null) {
            contentArea.getChildren().setAll(new Label("Không thể tải dữ liệu. Vui lòng đăng nhập lại."));
            return;
        }

        // ✅ Lấy đúng danh sách hóa đơn từ người bán (seller)
        var orders = com.example.demo.DAO.HoaDonDAO.getHoaDonFromSeller(currentSeller);

        // 🧮 Tính toán thống kê cơ bản
        int tongDoanhThu = 0;
        int tongDon = 0;
        java.util.Map<String, Integer> doanhThuTheoSanPham = new java.util.HashMap<>();

        for (var hd : orders) {
            // ✅ Chỉ tính doanh thu cho các đơn hàng đã hoàn tất
            if (hd.getStatus().equalsIgnoreCase("Completed")) {
                tongDoanhThu += hd.getGia();
                tongDon++;
                doanhThuTheoSanPham.merge(hd.getTen_san_pham(), hd.getGia(), Integer::sum);
            }
        }

        // 🏆 Tìm sản phẩm bán chạy nhất
        String sanPhamBanChay = doanhThuTheoSanPham.entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .orElse("Chưa có sản phẩm nào");

        int doanhThuBanChay = doanhThuTheoSanPham.getOrDefault(sanPhamBanChay, 0);

        // 🧱 Tạo layout hiển thị
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));

        Label title = new Label("📈 Thống kê doanh thu");
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label lblTongDon = new Label("Số đơn hàng đã hoàn tất: " + tongDon);
        Label lblDoanhThu = new Label("Tổng doanh thu: " + tongDoanhThu + " đ");
        Label lblTop = new Label("Sản phẩm bán chạy nhất: " + sanPhamBanChay + " (" + doanhThuBanChay + " đ)");

        lblTongDon.setStyle("-fx-font-size: 14; -fx-text-fill: #2c3e50;");
        lblDoanhThu.setStyle("-fx-font-size: 14; -fx-text-fill: green; -fx-font-weight: bold;");
        lblTop.setStyle("-fx-font-size: 14; -fx-text-fill: #2980b9;");

        container.getChildren().addAll(title, lblTongDon, lblDoanhThu, lblTop);

        // 🔹 Thêm bảng chi tiết doanh thu theo sản phẩm
        if (!doanhThuTheoSanPham.isEmpty()) {
            Label subTitle = new Label("Chi tiết doanh thu theo sản phẩm:");
            subTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #34495e;");
            container.getChildren().add(subTitle);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(6);
            grid.setPadding(new Insets(10));
            grid.addRow(0, new Label("Tên sản phẩm"), new Label("Doanh thu (đ)"));

            int row = 1;
            for (var entry : doanhThuTheoSanPham.entrySet()) {
                grid.addRow(row++, new Label(entry.getKey()), new Label(String.valueOf(entry.getValue())));
            }

            container.getChildren().add(grid);
        } else {
            Label noData = new Label("⚠️ Chưa có đơn hàng nào hoàn tất!");
            noData.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
            container.getChildren().add(noData);
        }

        // 🧩 Thêm ScrollPane cho giao diện có thể cuộn
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");

        contentArea.getChildren().setAll(scrollPane);
    }

    @FXML
    private void handleLogout() {
        UserSession.clear();
        StageSwitch.switchSceneFromNode(btnLogout, "login_view.fxml", "Đăng nhập");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
