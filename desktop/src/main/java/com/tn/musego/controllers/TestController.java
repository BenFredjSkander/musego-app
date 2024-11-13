package com.tn.musego.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TestController {

    @FXML
    public static void createStage(String res, String title) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(TestController.class.getResource(res));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        //stage.initStyle(StageStyle.UNDECORATED);
        //stage.getIcons().add(new Image("/img/M.png"));
        Scene sc = new Scene(root);
        //sc.getStylesheets().add("css/black.css");
        stage.setTitle(title);
        stage.setScene(sc);
        stage.showAndWait();
    }

    public static void createScene(String res, String title, Button btn) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(TestController.class.getResource(res));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btn.getScene().getWindow();
        //stage.getIcons().add(new Image("/img/M.png"));
        Scene sc = new Scene(root, 1280, 720);
        //sc.getStylesheets().add("css/black.css");
        stage.setTitle(title);
        stage.setScene(sc);
    }

}