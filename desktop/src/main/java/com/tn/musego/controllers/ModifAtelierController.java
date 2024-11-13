package com.tn.musego.controllers;

import com.tn.musego.entities.Atelier;
import com.tn.musego.entities.Formation;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.AtelierService;
import com.tn.musego.services.impl.FormationService;
import com.tn.musego.utils.AlertHelper;
import com.tn.musego.utils.DateHelper;
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

public class ModifAtelierController implements Initializable {
    Stage stage = new Stage();
    private File file;
    ValidatorHelper validatorHelper = new ValidatorHelper();
    ImageUploadHelper imageUploadHelper = new ImageUploadHelper();
    static Atelier atelier = new Atelier();

    @FXML
    private Button btnImage;

    @FXML
    private Button btnadd;

    @FXML
    private Button btncls;

    @FXML
    private TextField tfnom;

    public void initAtelier() throws MyCustomException {
        tfnom.setText(atelier.getNom());
//        comboAtelier.setConverter(new StringConverter<>() {
//            @Override
//            public String toString(Atelier object) {
//                if (object != null) {
//                    return object.getNom();
//                } else return "";
//            }
//
//            @Override
//            public Atelier fromString(String string) {
//                return comboAtelier.getSelectionModel().getSelectedItem();
//            }
//        });

    }

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
//        Atelier atelier = new Atelier();
//        String nom = tfnom.getText();
//        atelier.setNom(nom);
        //TypeOffreEnum type = combotype.getSelectionModel().getSelectedItem();
        try {

            if (validatorHelper.validateFields()) {
                Atelier atelier = new Atelier(
                        tfnom.getText(),
                        btnImage.getText()
                        );
                AtelierService atelierservice = new AtelierService();
                atelierservice.updateEntity(atelier);
                AlertHelper.showSuccess("Atelier modifié avec succès!");
                Stage stage = (Stage) tfnom.getScene().getWindow();
                stage.close();
            }

        } catch (Exception e) {
            AlertHelper.showError("Une erreur s'est produite du coté backend");
            e.printStackTrace();
        }

    }

    @FXML
    void annulerBut(ActionEvent event) {
        Stage stage = (Stage) btncls.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initAtelier();
        } catch (MyCustomException e) {
            throw new RuntimeException(e);
        }
        validatorHelper.addValidator(new TextFieldValidator(tfnom));
    }

}

