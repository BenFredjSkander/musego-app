package com.tn.musego.controllers;

import animatefx.animation.BounceIn;
import com.tn.musego.HelloApplication;
import com.tn.musego.entities.Artiste;
import com.tn.musego.services.impl.ArtisteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class BackArtistesController implements Initializable {
    public ObservableList<Artiste> data = FXCollections.observableArrayList();
    @FXML
    private TableView<Artiste> tableA;
    @FXML
    private TableColumn<Artiste, String> imageA;
    @FXML
    private TableColumn<Artiste, String> nomA;
    @FXML
    private TableColumn<Artiste, String> prenomA;
    @FXML
    private TableColumn<Artiste, Integer> cinA;
    @FXML
    private TableColumn<Artiste, String> emailA;
    @FXML
    private TableColumn<Artiste, Date> datenA;
    @FXML
    private TableColumn<Artiste, String> adresseA;
    @FXML
    private TableColumn<Artiste, String> specialiteA;
    @FXML
    private TableColumn<Artiste, String> nationaliteA;
    @FXML
    private TextField findIdA;

    @FXML
    private Button rechercheBtn;

    @FXML
    public void viewArtiste() {
        fillTable();
        new BounceIn(tableA).play();

    }

    public void InsertArtiste(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("back-add-artiste.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Ajout d'un artiste!");
        stage.setScene(scene);
        stage.showAndWait();
        fillTable();

    }

    @FXML
    private void deleteArtiste() {
        ArtisteService as = new ArtisteService();
        // Récupère la ligne sélectionnée
        Artiste selectedPersonne = tableA.getSelectionModel().getSelectedItem();

// Vérifie que la ligne sélectionnée est valide
        if (selectedPersonne != null) {
            as.supprimerArtiste(selectedPersonne.getId());


            fillTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Suppression d'un artiste");
            alert.setHeaderText("Information");
            alert.setContentText("Veuillez selectionner l'artiste a supprimer !");
            alert.showAndWait();
        }
        fillTable();
    }

    @FXML
    private void updateArtiste(ActionEvent event) throws IOException {
        if (tableA.getSelectionModel().getSelectedItem() != null) {

            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("ModifierArtiste.fxml"));
            Parent root = loader.load();

            ModifierArtisteController controller2 = loader.getController();
            controller2.initArtiste(tableA.getSelectionModel().getSelectedItem());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
            fillTable();

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un artiste");
            alert.show();
        }


    }

    @FXML
    void recherche() {
        FilteredList<Artiste> filteredData = new FilteredList<>(data, b -> true);
        findIdA.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Oeuvre -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null)
                    return true;
                String searchKeyword = newValue.toLowerCase();
                if (Oeuvre.getNom().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Oeuvre.getPrenom().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Oeuvre.getEmail().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Oeuvre.getSpecialite().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Oeuvre.getNationalite().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Oeuvre.getAdresse().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Oeuvre.getDateDeNaissance().toString().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else
                    return false;

            });
        });
        SortedList<Artiste> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableA.comparatorProperty());
        tableA.setItems(sortedData);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nomA.setCellValueFactory(new PropertyValueFactory<Artiste, String>("nom"));
        prenomA.setCellValueFactory(new PropertyValueFactory<Artiste, String>("prenom"));
        cinA.setCellValueFactory(new PropertyValueFactory<Artiste, Integer>("cin"));
        emailA.setCellValueFactory(new PropertyValueFactory<Artiste, String>("email"));
        datenA.setCellValueFactory(new PropertyValueFactory<Artiste, Date>("dateDeNaissance"));
        adresseA.setCellValueFactory(new PropertyValueFactory<Artiste, String>("adresse"));
        specialiteA.setCellValueFactory(new PropertyValueFactory<Artiste, String>("specialite"));
        nationaliteA.setCellValueFactory(new PropertyValueFactory<Artiste, String>("nationalite"));
        imageA.setCellValueFactory(new PropertyValueFactory<Artiste, String>("image"));
        fillTable();
        /*******RECHERCHE********/


        new BounceIn(tableA).play();


    }

    public void fillTable() {
        tableA.getItems().clear();
        ArtisteService artisteService = new ArtisteService();
        data = artisteService.afficherArtistes();
        tableA.setItems(data);

    }
}
