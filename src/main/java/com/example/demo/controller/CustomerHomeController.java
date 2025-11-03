package com.example.demo.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.ArrayList;
import com.example.demo.utils.StageSwitch;

import com.example.demo.DAO.SanPhamDAO;
import com.example.demo.utils.UserSession;
import com.example.demo.models.User;
import com.example.demo.models.SanPham;
import javafx.stage.Stage;

public class CustomerHomeController {
    User user = UserSession.getCurrentUser();
    @FXML private Button btnProductList;
    @FXML private Button btnCart;
    @FXML private Button btnOrders;
    @FXML private Button btnLogout;
    @FXML private StackPane contentArea;

    private List<Button> btnList = new ArrayList<>();

    @FXML
    private void initialize(){
        btnList.add(btnProductList);
        btnList.add(btnCart);
        btnList.add(btnOrders);
        handleShowProductList();
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
        List<SanPham> productionList = SanPhamDAO.getProductionList();
        GridPane grid = new  GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(10));

        int col = 0, row = 0;
        for (SanPham sanPham : productionList){
            VBox card = new VBox(10);
            card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-alignment: center;");
            card.setPrefWidth(180);

            ImageView imageView = new ImageView();
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            imageView.setImage(sanPham.getImage());

            Label product_name = new Label(sanPham.getTen_san_pham());
            product_name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            card.getChildren().addAll(imageView, product_name);

            grid.add(card, col++, row);
            if(col == 4){
                row += 1;
                col = 0;
            }
        }
        contentArea.getChildren().setAll(grid);
    }
    @FXML private void handleShowCart(){
        hightlightButton(btnCart);
        contentArea.getChildren().clear();
    }
    @FXML private void handleShowOrders(){
        hightlightButton(btnOrders);
        contentArea.getChildren().clear();
    }
    @FXML private void handleLogout(){
        StageSwitch.switchSceneFromNode(btnLogout, "login_view.fxml", "Login");
    }
}
