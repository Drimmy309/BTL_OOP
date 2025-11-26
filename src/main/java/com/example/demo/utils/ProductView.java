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

    public static GridPane createProductGrid(List<SanPham> products, java.util.function.Consumer<SanPham> onItemClick) {
        GridPane productGrid = new GridPane();
        productGrid.setHgap(20);
        productGrid.setVgap(20);
        productGrid.setPadding(new Insets(20));
        productGrid.setAlignment(Pos.CENTER);

        int col = 0;
        int row = 0;
        for (SanPham product : products) {
            if(product.getStatus().equals("Available")){
                // Truyền lambda vào để xử lý action
                productGrid.add(createProductItem(product, () -> onItemClick.accept(product)), col++, row);
                if (col == 4){
                    col = 0;
                    row += 1;
                }
            }
        }
        productGrid.setStyle("-fx-background-color: #C1E8FF;");
        return productGrid;
    }

    public static VBox createProductItem(SanPham sanPham, Runnable onButtonClick) {
        VBox box = new VBox(5);
        box.setPrefSize(220, 300);
        box.setMaxSize(220, 300);
        box.setMinSize(220, 300);
        box.setAlignment(Pos.TOP_CENTER);
        box.setPadding(new Insets(10));
        box.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 15;
            -fx-border-radius: 15;
            -fx-border-color: #d0d0d0;
            -fx-cursor: hand;
        """);

        box.setOnMouseEntered(e -> {
            box.setStyle("""
                -fx-background-color: #f8faff;
                -fx-background-radius: 15;
                -fx-border-radius: 15;
                -fx-border-color: #8ecae6;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 4);
                -fx-cursor: hand;
            """);
        });
        box.setOnMouseExited(e -> {
            box.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 15;
                -fx-border-radius: 15;
                -fx-border-color: #d0d0d0;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 3);
                -fx-cursor: hand;
            """);
        });

        // Image
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefSize(200, 200);
        imageContainer.setMinSize(200, 200);
        imageContainer.setMaxSize(200, 200);
        imageContainer.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 10;");
        ImageView img = new ImageView(sanPham.getImage());
        img.setPreserveRatio(true);
        img.setFitWidth(180);
        img.setFitHeight(180);
        img.setSmooth(true);
        img.setCache(true);
        imageContainer.getChildren().add(img);
        StackPane.setAlignment(img, Pos.CENTER);


        // Labels
        Label nameLabel = new Label(sanPham.getProduct_name());
        nameLabel.setFont(Font.font(14));

        Label priceLabel = new Label("Giá: " + String.format("%,d", + sanPham.getProduct_price()) + "VNĐ");
        priceLabel.setFont(Font.font(13));

        // Button
        Button actionButton = new Button("Xem");
        actionButton.setStyle("-fx-background-color: #1A3D63; -fx-text-fill: white");
        actionButton.setOnAction(e -> onButtonClick.run());
        VBox.setVgrow(actionButton, Priority.ALWAYS);
        HBox buttonContainer = new HBox(actionButton);
        buttonContainer.setAlignment(Pos.BOTTOM_LEFT); // Góc dưới trái
        buttonContainer.setPadding(new Insets(5, 0, 0, 0));

        // Add tất cả vào box
        box.getChildren().addAll(imageContainer, nameLabel, priceLabel, buttonContainer);

        return box;
    }
}
