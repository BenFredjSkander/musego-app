package com.tn.musego.controllers;

import com.tn.musego.entities.Artiste;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.ArtisteService;
import com.tn.musego.utils.ImageUploadHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BackAjouterArtisteController implements Initializable {
    ImageUploadHelper imageUploadHelper = new ImageUploadHelper();
    @FXML
    private TextField txtIdA;
    @FXML
    private TextField txtNomA;
    @FXML
    private TextField txtPrenomA;
    @FXML
    private TextField txtCinA;
    @FXML
    private TextField txtEmailA;
    @FXML
    private DatePicker dpdatenA;
    @FXML
    private TextField txtAdresseA;
    @FXML
    private TextField txtSpecialiteA;
    @FXML
    private TextField txtNationaliteA;
    @FXML
    private Button btnAjoutA;
    @FXML
    private Button btnBrowse;
    @FXML
    private Stage stage;
    private Image image;
    private File file;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dpdatenA.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0);
            }
        });

        /***********CONTROLE DE SAISIE***********/
        CntrlSaisieString(txtNomA);
        CntrlSaisieString(txtPrenomA);
        CntrlSaisieInteger(txtCinA);
        CntrlSaisieString(txtAdresseA);
        CntrlSaisieString(txtSpecialiteA);
        CntrlSaisieString(txtNationaliteA);

        btnAjoutA.disableProperty().bind(txtNomA.textProperty().isEmpty().or(txtPrenomA.textProperty().isEmpty().or(txtCinA.textProperty().isEmpty().or(txtAdresseA.textProperty().isEmpty().or(txtEmailA.textProperty().isEmpty().or(txtNationaliteA.textProperty().isEmpty().or(txtSpecialiteA.textProperty().isEmpty().or(dpdatenA.valueProperty().isNull()))))))));


    }

    public boolean CntrlSaisieMailDate(TextField txt) {

        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(gmail\\.com|email\\.com|yahoo\\.com)$";
        // If the character entered is not a digit, consume the event

        if (!dpdatenA.getValue().isBefore(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("La date doit être anterieure à la date actuelle");
            alert.show();
            return false;
        }
        if (!txt.getText().matches(regex)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ajouter Artiste");
            alert.setHeaderText("Information");
            alert.setContentText("Veuillez saisir un mail valide !");
            alert.showAndWait();
            alert.close();
            return false;
        } else {
            return true;
        }

    }

    public void CntrlSaisieString(TextField txt) {
        txt.addEventFilter(KeyEvent.KEY_TYPED, eve -> {
            String text = ((TextField) eve.getSource()).getText();
            String character = eve.getCharacter();
            String regex = "[\\p{L}\\p{Punct} ]+";

            // If the character entered is not a letter, punctuation mark, or space, consume the event
            if (!character.matches(regex)) {
                eve.consume();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ajouter Artiste");
                alert.setHeaderText("Information");
                alert.setContentText("Vous ne pouvez ajouter que des lettres et des espaces !");
                alert.showAndWait();
                alert.close();
            }
        });
    }

    public void CntrlSaisieInteger(TextField txt) {
        txt.addEventFilter(KeyEvent.KEY_TYPED, eve -> {
            String text = ((TextField) eve.getSource()).getText();
            String character = eve.getCharacter();
            String regex = "^[0-9]$";

            // If the character entered is not a digit, consume the event
            if (!character.matches(regex)) {
                eve.consume();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ajouter Artiste");
                alert.setHeaderText("Information");
                alert.setContentText("Vous ne pouvez ajouter que des chiffres !");
                alert.showAndWait();
                alert.close();

            }
        });
    }

    @FXML
    void addImage(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez un fichier PNG");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));

        // Afficher la boîte de dialogue et obtenir le fichier sélectionné//
        File fichierSelectionne = fileChooser.showOpenDialog(stage);

        if (fichierSelectionne != null) {
            btnBrowse.setText("Fichier sélectionné : " + fichierSelectionne.getName());
            file = fichierSelectionne;
        }


    }

    public void AjoutArtiste(ActionEvent event) throws SQLException, IOException, MyCustomException {
        Artiste a = new Artiste();
        Date dateBirth = Date.valueOf(dpdatenA.getValue());
        String nom = txtNomA.getText();
        String prenom = txtPrenomA.getText();
        String cin = txtCinA.getText();
        String adresse = txtAdresseA.getText();
        String specialite = txtSpecialiteA.getText();
        String nationalite = txtNationaliteA.getText();

        a.setNom(nom);
        a.setPrenom(prenom);
        a.setCin(Integer.parseInt(cin));
        a.setDateDeNaissance(dateBirth);
        a.setAdresse(adresse);
        a.setSpecialite(specialite);
        a.setNationalite(nationalite);
//        a.setImage("http://localhost/images/artistes/" + file.getName());

        if (CntrlSaisieMailDate(txtEmailA)) {
            String url = imageUploadHelper.upload(file, "images");
            a.setImage(url);
            String email = txtEmailA.getText();
            a.setEmail(email);
            try {
                ArtisteService artisteService = new ArtisteService();
                artisteService.ajouterArtiste(a);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ajouter Artiste");
                alert.setHeaderText("Information");
                alert.setContentText("Artiste est bien ajouté");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ajouter Artiste");
                alert.setHeaderText("Information");
                alert.setContentText("Artiste n'est pas ajouté");
                alert.showAndWait();
            }
        }

    }
}
