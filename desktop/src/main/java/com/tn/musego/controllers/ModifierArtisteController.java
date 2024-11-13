package com.tn.musego.controllers;

import com.tn.musego.entities.Artiste;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.ArtisteService;
import com.tn.musego.utils.AlertHelper;
import com.tn.musego.utils.ImageUploadHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class ModifierArtisteController implements Initializable {
    ImageUploadHelper imageUploadHelper = new ImageUploadHelper();
    @FXML
    private TextField txtIdAm;
    @FXML
    private TextField txtNomAm;
    @FXML
    private TextField txtPrenomAm;
    @FXML
    private TextField txtCinAm;
    @FXML
    private TextField txtEmailAm;
    @FXML
    private DatePicker dpdatenAm;
    @FXML
    private TextField txtAdresseAm;
    @FXML
    private TextField txtSpecialiteAm;
    @FXML
    private TextField txtNationaliteAm;
    @FXML
    private TextField txtImageAm;
    @FXML
    private Button btnModifA;
    @FXML
    private Button btnBrowse;
    private Stage stage;
    private Image image;
    private File file;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /***********CONTROLE DE SAISIE***********/
        CntrlSaisieString(txtNomAm);
        CntrlSaisieString(txtPrenomAm);
        CntrlSaisieInteger(txtCinAm);
        CntrlSaisieString(txtAdresseAm);
        CntrlSaisieString(txtSpecialiteAm);
        CntrlSaisieString(txtNationaliteAm);

        btnModifA.disableProperty().bind(txtNomAm.textProperty().isEmpty().or(txtPrenomAm.textProperty().isEmpty().or(txtCinAm.textProperty().isEmpty().or(txtAdresseAm.textProperty().isEmpty().or(txtEmailAm.textProperty().isEmpty().or(txtNationaliteAm.textProperty().isEmpty().or(txtSpecialiteAm.textProperty().isEmpty().or(txtImageAm.textProperty().isEmpty()))))))));

    }

    public boolean CntrlSaisieMailDate(TextField txt) {

        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(gmail\\.com|email\\.com|yahoo\\.com)$";
        // If the character entered is not a digit, consume the event

        if (!dpdatenAm.getValue().isBefore(LocalDate.now())) {
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

            // If the character entered is not a digit, consume the event
            if (!character.matches(regex)) {
                eve.consume();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ajouter Artiste");
                alert.setHeaderText("Information");
                alert.setContentText("Vous ne pouvez ajouter que des lettres !");
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

    public void initArtiste(Artiste artiste) {

        txtIdAm.setText(String.valueOf(artiste.getId()));
        txtNomAm.setText(artiste.getNom());
        txtPrenomAm.setText(artiste.getPrenom());
        txtCinAm.setText(String.valueOf(artiste.getCin()));
        dpdatenAm.setValue(Instant.ofEpochMilli(artiste.getDateDeNaissance().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        txtAdresseAm.setText(artiste.getAdresse());
        txtEmailAm.setText(artiste.getEmail());
        txtSpecialiteAm.setText(artiste.getSpecialite());
        txtNationaliteAm.setText(artiste.getNationalite());
        txtImageAm.setText(artiste.getImage());

    }

    @FXML
    void updateImage(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez un fichier PNG");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Images PNG", ".png", ".jpg"));

        // Afficher la boîte de dialogue et obtenir le fichier sélectionné//
        File fichierSelectionne = fileChooser.showOpenDialog(stage);

        if (fichierSelectionne != null) {
            btnBrowse.setText("Fichier sélectionné : " + fichierSelectionne.getName());
            file = fichierSelectionne;
        }


    }

    @FXML
    void ModifArtiste(ActionEvent event) throws SQLException, IOException, MyCustomException {
        Artiste a = new Artiste();

        Date dateBirth = Date.valueOf(dpdatenAm.getValue());
        String id = txtIdAm.getText();
        String nom = txtNomAm.getText();
        String prenom = txtPrenomAm.getText();
        String cin = txtCinAm.getText();
        String adresse = txtAdresseAm.getText();
        String specialite = txtSpecialiteAm.getText();
        String nationalite = txtNationaliteAm.getText();
        String image = txtImageAm.getText();

        a.setId(Integer.parseInt(id));
        a.setNom(nom);
        a.setPrenom(prenom);
        a.setCin(Integer.parseInt(cin));
        a.setDateDeNaissance(dateBirth);
        a.setAdresse(adresse);
        a.setSpecialite(specialite);
        a.setNationalite(nationalite);
//        a.setImage("http://localhost/images/artistes/"+file.getName());

        if (CntrlSaisieMailDate(txtEmailAm)) {
            String url = imageUploadHelper.upload(file, "images");
            a.setImage(url);
            String email = txtEmailAm.getText();
            a.setEmail(email);
            try {
                ArtisteService artisteService = new ArtisteService();
                artisteService.modifierArtiste(a);
                AlertHelper.showSuccess("Artiste modifié avec succès");
            } catch (Exception e) {
                AlertHelper.showError("Erreur lors de la modification");
            }

        }

    }
}
