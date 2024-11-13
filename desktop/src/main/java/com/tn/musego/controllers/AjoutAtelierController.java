package com.tn.musego.controllers;

import com.tn.musego.entities.Atelier;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.AtelierService;
import com.tn.musego.utils.ImageUploadHelper;
import com.tn.musego.utils.ValidatorHelper;
import com.tn.musego.utils.validator.TextFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AjoutAtelierController implements Initializable {
    ImageUploadHelper imageUploadHelper = new ImageUploadHelper();
    ValidatorHelper validatorHelper = new ValidatorHelper();
    Stage stage = new Stage();

    @FXML
    private File file;
    @FXML
    private Button btnImage;

    @FXML
    private Button btnadd;

    @FXML
    private Button btncls;

    @FXML
    private TextField tfnom;

    @FXML
    void Imgbut(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez un fichier PNG");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images PNG", "*.png", "*.jpg"));

        // Afficher la boîte de dialogue et obtenir le fichier sélectionné//
        File fichierSelectionne = fileChooser.showOpenDialog(stage);

        if (fichierSelectionne != null) {
            btnImage.setText("Fichier sélectionné : " + fichierSelectionne.getName());
            file = fichierSelectionne;
        }

    }

    @FXML
    void addBut(ActionEvent event) throws IOException, MyCustomException {

        Atelier atelier = new Atelier();
        String nom = tfnom.getText();
        atelier.setNom(nom);
        //TypeOffreEnum type = combotype.getSelectionModel().getSelectedItem();
        if (validatorHelper.validateFields()) {
            String url = imageUploadHelper.upload(file, "images");
            atelier.setImage(url);
            try {
                AtelierService atelierService = new AtelierService();
                atelierService.createEntity(atelier);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ajouter Atelier");
                alert.setHeaderText("Information");
                alert.setContentText("Ajout Confirmé !");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ajouter Atelier");
                alert.setHeaderText("Information");
                alert.setContentText("Ajout Annulé !");
                alert.showAndWait();
            }

        }
    }

    @FXML
    void annulerBut(ActionEvent event) {
        Stage stage = (Stage) btncls.getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        validatorHelper.addValidator(new TextFieldValidator(tfnom));
    }

}
