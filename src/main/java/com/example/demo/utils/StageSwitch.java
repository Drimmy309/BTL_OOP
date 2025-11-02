package com.example.demo.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StageSwitch {

    public static void switchScene(Stage stage, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(StageSwitch.class.getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);

            if (title != null && !title.isEmpty()) {
                stage.setTitle(title);
            }

            stage.show();
        } catch (IOException e) {
            System.err.println("Lỗi load file: " + fxmlPath);
        }
    }

    public static void switchSceneFromNode(javafx.scene.Node anyNode, String fxmlPath, String title) {
        Stage stage = (Stage) anyNode.getScene().getWindow();
        switchScene(stage, fxmlPath, title);
    }
}
