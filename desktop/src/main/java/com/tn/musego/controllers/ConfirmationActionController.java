package com.tn.musego.controllers;

import com.tn.musego.entities.Abonnement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmationActionController implements Initializable {

    @FXML
    private Button btnsupp;

    public static boolean sign;

    public static Abonnement abonnement = new Abonnement();

    @FXML
    void suppBut(ActionEvent event) {
        sign = true;
        Stage stage = (Stage) btnsupp.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sign = false;
    }
}