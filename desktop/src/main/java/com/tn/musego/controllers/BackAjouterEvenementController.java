package com.tn.musego.controllers;

import com.tn.musego.entities.Evenements;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.EvenementService;
import com.tn.musego.utils.ImageUploadHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class BackAjouterEvenementController implements Initializable {
    ImageUploadHelper imageUploadHelper = new ImageUploadHelper();

    ///////partie ajout /////////
    @FXML
    private Button ajouterButton;


    @FXML
    private File file;

    @FXML
    private Button annulerButton;

    @FXML
    private Stage stage;
    @FXML
    private DatePicker date_debut;

    @FXML
    private DatePicker date_fin;

    @FXML
    private TextArea description_event;

    @FXML
    private Button input_poster;

    @FXML
    private TextField lieu_event;


    @FXML
    private TextField nbParticipantsTextField;

    @FXML
    private TextField nom_event;


    @FXML
    private GridPane root;

    @FXML
    private TextField type_event;

    public boolean controleSaisie() {
        String nom = nom_event.getText();
        String type = type_event.getText();
        String lieu = lieu_event.getText();

        if (nom.isEmpty() || type.isEmpty() || lieu.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Les champs nom, type et lieu sont obligatoires");
            alert.show();
            return false;
        }
        if (!nom.matches("[\\p{L}\\p{Punct} ]+") || !type.matches("[\\p{L}\\p{Punct} ]+") || !lieu.matches("[\\p{L}\\p{Punct} ]+")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Les champs nom, type et lieu doivent être saisis avec des lettres, des espaces et des symboles de ponctuation seulement");
            alert.show();
            return false;
        }


        LocalDate debut = date_debut.getValue();
        LocalDate fin = date_fin.getValue();
        if (debut == null || fin == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Les champs date début et date fin sont obligatoires");
            alert.show();
            return false;
        }

        if (debut.isBefore(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("La date de début doit être postérieure à la date actuelle");
            alert.show();
            return false;
        }

        if (debut.isAfter(fin)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("La date de début doit être antérieure à la date de fin");
            alert.show();
            return false;
        }

        return true;
    }


    @FXML
    void add_poster(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez un fichier PNG");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images PNG", "*.png", "*.jpg"));

        // Afficher la boîte de dialogue et obtenir le fichier sélectionné//
        File fichierSelectionne = fileChooser.showOpenDialog(stage);

        if (fichierSelectionne != null) {
            input_poster.setText("Fichier sélectionné : " + fichierSelectionne.getName());
            file = fichierSelectionne;
        }


    }


    @FXML
    void ajouter_event(ActionEvent event) throws IOException, MyCustomException {

        if (!controleSaisie()) {
            return;
        }
        if (file == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un fichier");
            alert.show();
            return;
        }


        Evenements e = new Evenements();
        e.setNom(nom_event.getText());
        e.setDateDebut(Date.from(date_debut.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        e.setDateFin(Date.from(date_fin.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        e.setType(type_event.getText());
        e.setLieu(lieu_event.getText());
        e.setDescription(description_event.getText());
        e.setPoster("http://localhost/images/events/" + file.getName());

        // Vérification de l'unicité de l'événement
        EvenementService pcd = new EvenementService();
        boolean evenementExiste = pcd.evenementExisteDeja(e.getNom());
        System.out.println("/n ------------------------" + e.getNom());
        System.out.println("/n ------------------------" + evenementExiste);


        if (evenementExiste) {
            // Affichage d'un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Cet événement existe déjà dans la base de données.");
            alert.show();
            return;
        } else if (!evenementExiste) {
            String url = imageUploadHelper.upload(file, "images");
            e.setPoster(url);
            pcd.ajouterEvenement(e);
        }


        //////affichage tee msg//////
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("info evenement");
        alert.setHeaderText(null);
        alert.setContentText("evenement insérée avec succés!");
        alert.show();

        return;
    }


    @FXML
    void annuler(ActionEvent event) {
        nom_event.setText(null);
        date_debut.setValue(null);
        date_fin.setValue(null);
        description_event.setText(null);
        lieu_event.setText(null);
        type_event.setText(null);


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}
