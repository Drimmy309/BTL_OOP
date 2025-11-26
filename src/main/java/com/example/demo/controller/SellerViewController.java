package com.example.demo.controller;

import com.example.demo.DAO.HoaDonDAO;
import com.example.demo.DAO.SanPhamDAO;
import com.example.demo.models.HoaDon;
import com.example.demo.models.SanPham;
import com.example.demo.utils.StageSwitch;
import com.example.demo.utils.UserSession;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.image.Image;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerViewController {

    @FXML private StackPane contentPane;
    @FXML private Button btnQuanLySanPham;
    @FXML private Button btnXacNhanDonHang;
    @FXML private Button btnThongKeDoanhThu;
    @FXML private Button logoutButton;

    @FXML
    private void initialize() {
        btnQuanLySanPham.setOnAction(e -> showProductManager());
        btnXacNhanDonHang.setOnAction(e -> showOrderConfirmation());
        btnThongKeDoanhThu.setOnAction(e -> showRevenueStatistics());
        logoutButton.setOnAction(e -> handleLogout());
        contentPane.widthProperty().addListener((obs, oldVal, newVal) -> showProductManager());
        contentPane.setStyle(
                "-fx-background-color: #C1E8FF; " + "-fx-padding: 20;"
        );

        Platform.runLater(this::showProductManager);
    }

    private TextFormatter<String> blockNegativeInput(TextField field) {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) return change;

            if (!newText.matches("\\d*")) {
                return null;
            }

            if (newText.contains("-")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sai định dạng");
                alert.setHeaderText(null);
                alert.setContentText("Bạn đã nhập sai định dạng! Số không thể âm.");
                alert.showAndWait();
                return null;
            }

            // Không cho nhập 0
            if (newText.equals("0")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sai định dạng");
                alert.setHeaderText(null);
                alert.setContentText("Giá trị phải lớn hơn 0!");
                alert.showAndWait();
                return null;
            }

            return change;
        });
    }


    private String extractFileName(Image img) {
        if (img == null) return null;
        String url = img.getUrl();
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private void showProductManager() {

        String seller = UserSession.getCurrentUser().getUserName();
        List<SanPham> products = SanPhamDAO.getSellerProducts(seller);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(30);
        grid.setVgap(40);
        grid.setAlignment(Pos.TOP_CENTER); // ⭐ Căn giữa toàn bộ grid

        int col = 0;
        int row = 0;

        for (SanPham sp : products) {

            VBox card = new VBox(10);
            card.setPadding(new Insets(15));
            card.setAlignment(Pos.CENTER);
            card.setPrefWidth(230);  // ⭐ width cố định để xếp đẹp như hình
            card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 15;
            -fx-border-radius: 15;
            -fx-border-color: #d0d0d0;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 3);
            -fx-cursor: hand;
        """);

            ImageView imageView = new ImageView(sp.getImage());
            imageView.setFitWidth(160);
            imageView.setFitHeight(160);
            imageView.setPreserveRatio(true);

            Label name = new Label(sp.getProduct_name());
            name.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

            Label price = new Label("Giá: " + String.format("%,d", sp.getProduct_price()) + "VNĐ");
            price.setStyle("-fx-font-size: 14; -fx-text-fill: #219ebc; -fx-font-weight: bold;");

            Label qty = new Label("Kho: " + sp.getProduct_quantity());
            qty.setStyle("-fx-font-size: 13; -fx-text-fill: #555;");

            Button editBtn = new Button("Sửa");
            Button deleteBtn = new Button("Xóa");

            editBtn.setStyle("""
            -fx-background-color: #8ecae6;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-background-radius: 8;
        """);

            deleteBtn.setStyle("""
            -fx-background-color: #e63946;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-background-radius: 8;
        """);

            editBtn.setOnAction(e -> editProduct(sp));
            deleteBtn.setOnAction(e -> deleteProduct(sp));

            HBox btnBox = new HBox(10, editBtn, deleteBtn);
            btnBox.setAlignment(Pos.CENTER);

            card.getChildren().addAll(imageView, name, price, qty, btnBox);

            // ⭐ ADD CARD TO GRID
            grid.add(card, col, row);

            col++;
            if (col == 4) {   // ⭐ Mỗi hàng 4 sản phẩm giống hình bạn gửi
                col = 0;
                row++;
            }
        }

        // CARD "Thêm sản phẩm"
        VBox addCard = new VBox(10);
        addCard.setPadding(new Insets(20));
        addCard.setAlignment(Pos.CENTER);
        addCard.setPrefWidth(230);
        addCard.setStyle("""
        -fx-background-color: #e9fff1;
        -fx-background-radius: 15;
        -fx-border-radius: 15;
        -fx-border-color: #4caf50;
        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 3);
    """);

        addCard.setOnMouseClicked(e -> addProduct());

        Label plus = new Label("+");
        plus.setStyle("-fx-font-size: 48; -fx-text-fill: #4caf50; -fx-font-weight: bold;");

        Label addText = new Label("Thêm sản phẩm");
        addText.setStyle("-fx-font-size: 16; -fx-text-fill: #2e7d32; -fx-font-weight: bold;");

        addCard.getChildren().addAll(plus, addText);

        grid.add(addCard, col, row);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #C1E8FF;");

        contentPane.getChildren().setAll(scroll);
    }



    private void addProduct() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Thêm sản phẩm mới");
        dialog.setHeaderText("Nhập thông tin sản phẩm");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        TextField name = new TextField();
        TextField category = new TextField();
        TextField quantity = new TextField();
        TextField price = new TextField();

        quantity.setTextFormatter(blockNegativeInput(quantity));
        price.setTextFormatter(blockNegativeInput(price));
        Label imgLabel = new Label("Chưa chọn ảnh");

        // ======== PREVIEW ẢNH ========
        ImageView preview = new ImageView();
        preview.setFitWidth(120);
        preview.setFitHeight(120);
        preview.setPreserveRatio(true);

        final File[] chosenFile = {null};

        // ======== BUTTON CHỌN ẢNH ========
        Button chooseImg = new Button("Chọn ảnh");
        chooseImg.setOnAction(e -> {
            javafx.stage.FileChooser ch = new javafx.stage.FileChooser();
            ch.getExtensionFilters().add(
                    new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg")
            );

            File file = ch.showOpenDialog(dialog.getDialogPane().getScene().getWindow());

            if (file != null) {
                chosenFile[0] = file;
                imgLabel.setText(file.getName());

                preview.setImage(new Image(file.toURI().toString()));
            }
        });

        grid.addRow(0, new Label("Tên:"), name);
        grid.addRow(1, new Label("Loại:"), category);
        grid.addRow(2, new Label("Số lượng:"), quantity);
        grid.addRow(3, new Label("Giá:"), price);
        grid.addRow(4, chooseImg, imgLabel);
        grid.add(preview, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {

                if (chosenFile[0] == null) {
                    showAlert("Thiếu ảnh", "Vui lòng chọn ảnh sản phẩm!");
                    return;
                }
                SanPhamDAO.DuplicateCheckResult dup = SanPhamDAO.checkProductDuplicate(
                        chosenFile[0].getName(),
                        UserSession.getCurrentUser().getUserName()
                );

                if (dup.imageExists) {
                    showAlert("Ảnh bị trùng", "Ảnh này bạn đã dùng ở sản phẩm khác.\nHãy chọn ảnh khác!");
                    return;
                }


                try {
                    int qty = Integer.parseInt(quantity.getText());
                    int pr = Integer.parseInt(price.getText());

                    String id = "SP" + System.currentTimeMillis();

                    File dest = new File("src/main/resources/com/example/demo/images/" + chosenFile[0].getName());
                    Files.copy(chosenFile[0].toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    SanPham sp = new SanPham(id, name.getText(), category.getText(),
                            UserSession.getCurrentUser().getUserName(),
                            null, qty, pr, "Available");

                    boolean ok = SanPhamDAO.addProduct(sp, chosenFile[0].getName());

                    if (ok) showAlert("Thành công", "Đã thêm sản phẩm!");
                    else showAlert("Lỗi", "Không thể thêm sản phẩm.");

                    showProductManager();

                } catch (Exception ex) {
                    showAlert("Lỗi", "Số lượng hoặc giá không đúng!");
                }
            }
        });

    }


    private void editProduct(SanPham sp) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Sửa sản phẩm");

        TextField name = new TextField(sp.getProduct_name());
        TextField cat = new TextField(sp.getProduct_category());
        TextField qty = new TextField(String.valueOf(sp.getProduct_quantity()));
        TextField pr = new TextField(String.valueOf(sp.getProduct_price()));
        qty.setTextFormatter(blockNegativeInput(qty));
        pr.setTextFormatter(blockNegativeInput(pr));

        Label img = new Label("Giữ nguyên");
        final File[] newImg = {null};

        Button choose = new Button("Đổi ảnh");
        choose.setOnAction(e -> {
            javafx.stage.FileChooser ch = new javafx.stage.FileChooser();
            ch.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Image", "*.png", "*.jpg"));
            newImg[0] = ch.showOpenDialog(dialog.getDialogPane().getScene().getWindow());
            if (newImg[0] != null) img.setText(newImg[0].getName());
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.addRow(0, new Label("Tên:"), name);
        grid.addRow(1, new Label("Loại:"), cat);
        grid.addRow(2, new Label("Số lượng:"), qty);
        grid.addRow(3, new Label("Giá:"), pr);
        grid.addRow(4, choose, img);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                SanPhamDAO.DuplicateCheckResult dup = SanPhamDAO.checkProductDuplicate(
                        (newImg[0] != null ? newImg[0].getName() : extractFileName(sp.getImage())),
                        sp.getUsername()
                );

                if (newImg[0] == null || extractFileName(sp.getImage()).equals(newImg[0].getName())) {
                    dup.imageExists = false;
                }

                if (dup.imageExists) {
                    showAlert("Trùng ảnh", "Ảnh này đã được dùng ở sản phẩm khác!");
                    return;
                }


                try {
                    String imgName = extractFileName(sp.getImage());

                    if (newImg[0] != null) {
                        File dest = new File("src/main/resources/com/example/demo/images/" + newImg[0].getName());
                        Files.copy(newImg[0].toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        imgName = newImg[0].getName();
                    }

                    SanPham updated = new SanPham(
                            sp.getProduct_id(),
                            name.getText(),
                            cat.getText(),
                            sp.getUsername(),
                            sp.getImage(),
                            Integer.parseInt(qty.getText()),
                            Integer.parseInt(pr.getText()),
                            sp.getStatus()
                    );

                    boolean ok = SanPhamDAO.updateProduct(updated, imgName);

                    if (ok) showAlert("Thành công", "Sửa thành công!");
                    else showAlert("Lỗi", "Không thể sửa!");

                    showProductManager();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert("Lỗi", "Sai format số!");
                }
            }
        });
    }

    private void deleteProduct(SanPham sp) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xóa sản phẩm");
        alert.setHeaderText("Bạn chắc chắn muốn xóa?");
        alert.setContentText(sp.getProduct_name());

        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {

                if (SanPhamDAO.deleteProduct(sp.getProduct_id())) {
                    showAlert("Hoàn tất", "Đã xóa sản phẩm.");
                } else {
                    showAlert("Lỗi", "Không thể xóa.");
                }

                showProductManager();
            }
        });
    }

    private void showOrderConfirmation() {

        String seller = UserSession.getCurrentUser().getUserName();

        List<String> billIds = HoaDonDAO.getBillsGroupForSeller(seller);

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        if (billIds.isEmpty()) {
            root.getChildren().add(new Label("❌ Chưa có hóa đơn nào."));
            contentPane.getChildren().setAll(root);
            return;
        }

        for (String billId : billIds) {

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

            // Nút hành động
            VBox actions = new VBox(10);

            // Nút xem chi tiết
            Button detailBtn = new Button("Xem chi tiết");
            detailBtn.setStyle("-fx-background-color:#176B87; -fx-text-fill:white; -fx-font-weight:bold;");
            detailBtn.setOnAction(e -> showBillDetailsDialog(billId));

            actions.getChildren().add(detailBtn);

            // Trạng thái Pending → có Duyệt / Từ chối
            if (status.equals("Pending")) {

                Button approve = new Button("Duyệt đơn");
                approve.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight:bold;");

                Button deny = new Button("Từ chối");
                deny.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight:bold;");

                approve.setOnAction(e -> {
                    HoaDonDAO.updateBillStatus(billId, "Approved");
                    showOrderConfirmation();
                });

                deny.setOnAction(e -> {
                    HoaDonDAO.updateBillStatus(billId, "Denied");
                    showOrderConfirmation();
                });

                actions.getChildren().addAll(approve, deny);
            }

            // Trạng thái khác → chỉ hiển thị label
            else if (status.equals("Approved")) {
                Label wait = new Label("✔ Đã duyệt — chờ khách thanh toán");
                wait.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
                actions.getChildren().add(wait);
            }
            else if (status.equals("Denied")) {
                Label denied = new Label("❌ Đơn đã bị từ chối");
                denied.setStyle("-fx-text-fill: red; -fx-font-weight:bold;");
                actions.getChildren().add(denied);
            }
            else if (status.equals("Paid")) {
                Label paid = new Label("💳 Khách đã thanh toán");
                paid.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
                actions.getChildren().add(paid);
            }

            card.getChildren().addAll(info, actions);
            root.getChildren().add(card);
        }

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);

        contentPane.getChildren().setAll(scroll);
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



    private void showRevenueStatistics() {

        contentPane.getChildren().clear();

        String seller = UserSession.getCurrentUser().getUserName();

        List<String> billIds = HoaDonDAO.getPaidBillIdsForSeller(seller);

        VBox root = new VBox(15);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: white;");

        Label title = new Label("📊 Thống kê doanh thu");
        title.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #176B87;");
        root.getChildren().add(title);

        if (billIds.isEmpty()) {
            Label empty = new Label("❌ Chưa có hóa đơn nào đã thanh toán.");
            empty.setStyle("-fx-text-fill: red; -fx-font-size: 16;");
            root.getChildren().add(empty);
            contentPane.getChildren().setAll(root);
            return;
        }

        int totalRevenue = 0;
        Map<String, Integer> revenueMap = new HashMap<>();

        VBox billList = new VBox(12);

        for (String billId : billIds) {

            List<HoaDon> items = HoaDonDAO.getBillDetails(billId);
            int billTotal = 0;

            VBox card = new VBox(8);
            card.setPadding(new Insets(12));
            card.setStyle(
                    "-fx-background-color: #ffffff;" +
                            "-fx-background-radius: 12;" +
                            "-fx-border-color: #dcdcdc;" +
                            "-fx-border-radius: 12;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 6, 0, 0, 2);"
            );

            Label idLabel = new Label("🧾 Mã hóa đơn: " + billId);
            idLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

            VBox detailBox = new VBox(5);

            for (HoaDon item : items) {

                SanPham sp = SanPhamDAO.getProductById(item.getProduct_id());
                if (sp == null) continue;

                int amount = sp.getProduct_price() * item.getQuantity();
                billTotal += amount;

                revenueMap.put(
                        sp.getProduct_name(),
                        revenueMap.getOrDefault(sp.getProduct_name(), 0) + amount
                );

                Label line = new Label(
                        "• " + sp.getProduct_name() +
                                " | SL: " + item.getQuantity() +
                                " | Thành tiền: " + String.format("%,d", amount) + "đ"
                );
                line.setStyle("-fx-font-size: 14;");
                detailBox.getChildren().add(line);
            }

            totalRevenue += billTotal;

            Label total = new Label("➡ Tổng hóa đơn: " + String.format("%,d", billTotal) + "đ");
            total.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #176B87;");

            card.getChildren().addAll(idLabel, detailBox, new Separator(), total);
            billList.getChildren().add(card);
        }

        Label totalAll = new Label("💰 Tổng doanh thu: " + String.format("%,d", totalRevenue) + "đ");
        totalAll.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #0E4D64;");
        root.getChildren().add(totalAll);

        // CHART
        Label chartTitle = new Label("📊 Doanh thu theo sản phẩm");
        chartTitle.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        root.getChildren().add(chartTitle);

        PieChart chart = new PieChart();
        for (var e : revenueMap.entrySet()) {
            chart.getData().add(new PieChart.Data(e.getKey(), e.getValue()));
        }
        chart.setLabelsVisible(true);

        root.getChildren().add(chart);
        root.getChildren().add(new Separator());
        root.getChildren().add(billList);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);

        contentPane.getChildren().setAll(scroll);
    }



    private void handleLogout() {
        UserSession.clear();
        StageSwitch.switchSceneFromNode(logoutButton, "login_view.fxml", "Đăng nhập");
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}