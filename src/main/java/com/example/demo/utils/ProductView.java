package com.example.demo.utils;

import com.example.demo.models.SanPham;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.List;

public class ProductView{

    public static TextField createSearchBar() {
        TextField searchField = new TextField();
        searchField.setPromptText("Tìm kiếm sản phẩm...");
        searchField.setFont(Font.font(14));
        searchField.setPadding(new Insets(10));

        // Màu đẹp và hiện đại
        searchField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #5483B3;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-prompt-text-fill: #7a7a7a;"
        );

        return searchField;
    }


    public static VBox createFilterButtons() {
        VBox filterBox = new VBox(10);
        filterBox.setPadding(new Insets(15));
        filterBox.setStyle("-fx-background-color: #C1E8FF;");


        Button btn1 = new Button("Tất cả");
        Button btn2 = new Button("Laptop");
        Button btn3 = new Button("Bàn Phím");
        Button btn4 = new Button("Chuột");
        Button btn5 = new Button("Khác");

        btn1.setStyle("-fx-background-color: #1A3D63; -fx-text-fill: white");
        btn2.setStyle("-fx-background-color: #1A3D63; -fx-text-fill: white");
        btn3.setStyle("-fx-background-color: #1A3D63; -fx-text-fill: white");
        btn4.setStyle("-fx-background-color: #1A3D63; -fx-text-fill: white");
        btn5.setStyle("-fx-background-color: #1A3D63; -fx-text-fill: white");

        btn1.setMaxWidth(Double.MAX_VALUE);
        btn2.setMaxWidth(Double.MAX_VALUE);
        btn3.setMaxWidth(Double.MAX_VALUE);
        btn4.setMaxWidth(Double.MAX_VALUE);
        btn5.setMaxWidth(Double.MAX_VALUE);


        filterBox.getChildren().addAll(btn1, btn2, btn3, btn4, btn5);
        return filterBox;
    }


    public static ScrollPane createProductGrid(List<SanPham> products) {
        GridPane productGrid = new GridPane();
        productGrid.setHgap(20);
        productGrid.setVgap(20);
        productGrid.setPadding(new Insets(20));


        ScrollPane scrollPane = new ScrollPane(productGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #4A7FA7;");


        int col = 0;
        int row = 0;
        for (SanPham product : products) {
            productGrid.add(createProductItem(product), col, row++);
            if (row == 4){
                row = 0;
                col += 1;
            }
        }
        return scrollPane;
    }


    public static VBox createProductItem(SanPham sanPham) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5;");

        ImageView img = new ImageView(sanPham.getImage());
        img.setFitWidth(120);
        img.setFitHeight(120);
        img.setPreserveRatio(true);

        Label nameLabel = new Label(sanPham.getProduct_name());
        nameLabel.setFont(Font.font(14));


        Label priceLabel = new Label(String.valueOf(sanPham.getProduct_price()));
        priceLabel.setFont(Font.font(13));

        box.getChildren().addAll(img, nameLabel, priceLabel);
        return box;
    }
}
