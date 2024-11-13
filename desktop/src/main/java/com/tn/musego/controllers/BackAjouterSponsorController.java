package com.tn.musego.controllers;

import com.tn.musego.entities.Evenements;
import com.tn.musego.entities.Sponsor;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.EvenementService;
import com.tn.musego.services.impl.SponsorService;
import com.tn.musego.utils.ImageUploadHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BackAjouterSponsorController implements Initializable {
    ImageUploadHelper imageUploadHelper = new ImageUploadHelper();
    @FXML
    private Button ajouterButton;

    @FXML
    private TextField capacitefinan;

    @FXML
    private ComboBox<Evenements> eventsponsorise;

    @FXML
    private Button input_poster;

    @FXML
    private TextField nom;

    @FXML
    private File file;

    @FXML
    private Stage stage;

    public boolean controleSaisie() {
        String nomSponsor = nom.getText().trim();
        String capaciteFinanSponsor = capacitefinan.getText().trim();

        // Vérifier si tous les champs sont remplis
        if (nomSponsor.isEmpty() || capaciteFinanSponsor.isEmpty() || eventsponsorise.getSelectionModel().isEmpty()) {
            return false;
        }

        // Vérifier si le nom contient uniquement des lettres et des espaces
        if (!nomSponsor.matches("^[a-zA-Z ]+$")) {
            return false;
        }

        // Vérifier si la capacité financière est supérieure à 1000
        double capaciteFinan = 0;
        try {
            capaciteFinan = Double.parseDouble(capaciteFinanSponsor);
        } catch (NumberFormatException e) {
            return false;
        }
        if (capaciteFinan < 1000) {
            return false;
        }

        return true;
    }


    @FXML
    void add_poster(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez un fichier PNG");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images PNG", "*.png", "*.jpg"));

        // Afficher la boîte de dialogue et obtenir le fichier sélectionné//
        File fichierSelectionne = fileChooser.showOpenDialog(stage);

        if (fichierSelectionne != null) {
            input_poster.setText("Fichier sélectionné : " + fichierSelectionne.getName());
            file = fichierSelectionne;
        }
    }


    @FXML
    void ajoutersponsor(ActionEvent event) throws IOException, MyCustomException {
        // Vérifier les saisies utilisateur
        if (!controleSaisie()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs correctement.");
            alert.showAndWait();
            return;
        }

        // Récupérer les données saisies dans les champs
        String nomSponsor = nom.getText().trim();
        String url = imageUploadHelper.upload(file, "images");
//        String photo = file.getAbsolutePath();
        int capaciteFinanciere = Integer.parseInt(capacitefinan.getText().trim());
        int idEvenement = (int) eventsponsorise.getSelectionModel().getSelectedItem().getId();

        // Créer un objet Sponsor avec les données récupérées
        Sponsor sponsor = new Sponsor(nomSponsor, url, capaciteFinanciere, idEvenement);

        // Ajouter le sponsor dans la base de données
        new SponsorService().ajouterSponsor(sponsor);

        // Afficher un message de succès
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("Sponsor ajouté avec succès !");
        alert.showAndWait();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Evenements> evenementsList = new EvenementService().listeEvenements();

        // Convert the list of events to an observable list
        ObservableList<Evenements> observableList = FXCollections.observableArrayList(evenementsList);
        eventsponsorise.setValue(observableList.get(0));
        eventsponsorise.setConverter(new StringConverter<>() {
            @Override
            public String toString(Evenements object) {
                if (object.getNom() != null) {
                    return object.getNom();
                } else return "";
            }

            @Override
            public Evenements fromString(String string) {
                return eventsponsorise.getSelectionModel().getSelectedItem();
            }
        });

        // Set the items of the ComboBox to the observable list of events
        eventsponsorise.setItems(observableList);

    }

    @FXML
    void annuler(ActionEvent event) {
        nom.setText(null);
        capacitefinan.setText(null);
        eventsponsorise.setValue(null);


    }


}