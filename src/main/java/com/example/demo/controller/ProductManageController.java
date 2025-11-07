package com.example.demo.controller;

import com.example.demo.DAO.SanPhamDAO;
import com.example.demo.models.SanPham;
import com.example.demo.utils.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class ProductManageController {
    @FXML private TableView<SanPham> tableProducts;
    @FXML private TableColumn<SanPham, String> colMaSP;
    @FXML private TableColumn<SanPham, String> colTenSP;
    @FXML private TableColumn<SanPham, Integer> colGia;
    @FXML private TableColumn<SanPham, Integer> colSoLuong;

    private ObservableList<SanPham> data;

    @FXML
    private void initialize() {
        colMaSP.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getMaSanPham()));
        colTenSP.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTenSanPham()));
        colGia.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getGia()).asObject());
        colSoLuong.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getSoLuong()).asObject());
        loadProducts();
    }

    private void loadProducts() {
        String seller = UserSession.getCurrentUser().getUsername();
        List<SanPham> list = new SanPhamDAO().getProductionListByUser(seller);
        data = FXCollections.observableArrayList(list);
        tableProducts.setItems(data);
        addAddButtonRow(); // thêm dòng có nút "+"
    }

    @FXML
    private void handleAddProduct() {
        System.out.println("Thêm sản phẩm...");
        // TODO: mở form thêm sản phẩm (có thể là dialog)
    }

    @FXML
    private void handleEditProduct() {
        SanPham selected = tableProducts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Chưa chọn sản phẩm!", "Vui lòng chọn sản phẩm cần sửa.");
            return;
        }
        System.out.println("Sửa: " + selected.getTenSanPham());
        // TODO: hiển thị form sửa
    }

    @FXML
    private void handleDeleteProduct() {
        SanPham selected = tableProducts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Chưa chọn sản phẩm!", "Vui lòng chọn sản phẩm cần xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Xóa sản phẩm " + selected.getTenSanPham() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            boolean success = SanPhamDAO.deleteProduct(selected.getMaSanPham());
            if (success) {
                data.remove(selected);
                showAlert("Thành công", "Đã xóa sản phẩm!");
            } else {
                showAlert("Lỗi", "Không thể xóa sản phẩm.");
            }
        }
    }

    // thêm dòng "thêm sản phẩm" ở cuối bảng
    private void addAddButtonRow() {
        // tạo 1 "sản phẩm ảo" chỉ dùng để hiện nút +
        SanPham addPlaceholder = new SanPham("ADD_BUTTON", "", null, "", 0, 0);
        data.add(addPlaceholder);

        // Cột tên SP hiển thị nút "+"
        colTenSP.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                SanPham sp = getTableRow().getItem();
                if ("ADD_BUTTON".equals(sp.getMaSanPham())) {
                    Button addBtn = new Button("+");
                    addBtn.setStyle("-fx-font-size: 22px; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                    addBtn.setOnAction(e -> handleAddProduct());
                    setGraphic(addBtn);
                    setText(null);
                } else {
                    setText(item);
                    setGraphic(null);
                }
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
}
