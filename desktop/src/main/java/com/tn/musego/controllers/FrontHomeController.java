package com.tn.musego.controllers;

import com.tn.musego.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Skander Ben Fredj
 * @created 06-Mar-23
 * @project musego
 */

public class FrontHomeController {

    public void jouerMusique(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mp3.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Jouons de la musique!");
        stage.setScene(scene);
        stage.show();


    }

}
