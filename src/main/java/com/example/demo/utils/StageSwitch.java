package com.example.demo.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class StageSwitch {

    public static void switchScene(Stage stage, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(StageSwitch.class.getResource("/com/example/demo/" + fxmlPath));
            Parent root = loader.load();

            stage.hide();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.setResizable(true);
            stage.show();

            Platform.runLater(() -> {
                stage.setMaximized(true);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchSceneFromNode(javafx.scene.Node anyNode, String fxmlPath, String title) {
        Stage stage = (Stage) anyNode.getScene().getWindow();
        switchScene(stage, fxmlPath, title);
    }
}