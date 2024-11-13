package com.tn.musego.controllers;

import animatefx.animation.BounceIn;
import com.tn.musego.HelloApplication;
import com.tn.musego.entities.Oeuvre;
import com.tn.musego.services.impl.ArtisteService;
import com.tn.musego.services.impl.OeuvreService;
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
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class BackOeuvresController implements Initializable {
    public ObservableList<Oeuvre> data = FXCollections.observableArrayList();
    Oeuvre o = new Oeuvre();
    ArtisteService arts = new ArtisteService();
    @FXML
    private TableView<Oeuvre> tableO;
    @FXML
    private TableColumn<Oeuvre, Integer> idO;
    @FXML
    private TableColumn<Oeuvre, String> titreO;
    @FXML
    private TableColumn<Oeuvre, Date> datecO;
    @FXML
    private TableColumn<Oeuvre, String> categO;
    @FXML
    private TableColumn<Oeuvre, String> descriptionO;
    @FXML
    private TableColumn<Oeuvre, String> lieuO;
    @FXML
    private TableColumn<Oeuvre, String> nomArt;
    @FXML
    private TableColumn<Oeuvre, Integer> idArt;
    @FXML
    private TableColumn<Oeuvre, String> imageO;
    @FXML
    private Button modifOButton;
    @FXML
    private TextField findIdO;
    private MediaView mediaView;

    @FXML
    private Button rechercheBtn;

    @FXML
    void refreshData(ActionEvent event) {
        fillTable();
        FilteredList<Oeuvre> filteredData = new FilteredList<>(data, b -> true);
        findIdO.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Oeuvre -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null)
                    return true;
                String searchKeyword = newValue.toLowerCase();
                if (Oeuvre.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Oeuvre.getDescription().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                }  else if (Oeuvre.getLieuDeConservartion().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else
                    return false;

            });
        });
        SortedList<Oeuvre> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableO.comparatorProperty());
        tableO.setItems(sortedData);

        /*****ANIMATION*****/
        new BounceIn(tableO).play();


    }

    public void InsertOeuvre(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("back-add-oeuvre.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Ajout d'une Oeuvre!");
        stage.setScene(scene);
        stage.showAndWait();
        fillTable();
        //  tableO.refresh();
    }

    @FXML
    private void deleteOeuvre() {
        OeuvreService os = new OeuvreService();
        // Récupère la ligne sélectionnée
        Oeuvre selectedPersonne = tableO.getSelectionModel().getSelectedItem();

// Vérifie que la ligne sélectionnée est valide
        if (selectedPersonne != null) {
            os.supprimerOeuvre(selectedPersonne.getId());
            // Rafraîchit le TableView pour afficher la mise à jour
            fillTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Suppression d'une oeuvre");
            alert.setHeaderText("Information");
            alert.setContentText("Veuillez selectionner l'oeuvre a supprimer !");
            alert.showAndWait();
        }
    }

    @FXML
    private void updateOeuvre(ActionEvent event) throws IOException {
        if (tableO.getSelectionModel().getSelectedItem() != null) {

            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("ModifierOeuvre.fxml"));
            Parent root = loader.load();
            System.out.println("mmmmmmmmmaaaaaaaaaaaaaaaaa"+tableO.getSelectionModel().getSelectedItem().getId());
            ModifierOeuvreController controller2 = loader.getController();
            controller2.initOeuvre(tableO.getSelectionModel().getSelectedItem());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
            controller2.refreshData(tableO);
            fillTable();


        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une oeuvre");
            alert.show();
        }

    }

    public void fillTable() {
        tableO.getItems().clear();
        OeuvreService oeuvreService = new OeuvreService();
        data = oeuvreService.afficherAllOeuvres();

        tableO.setItems(data);
    }

    @FXML
    void recherche() {
        FilteredList<Oeuvre> filteredData = new FilteredList<>(data, b -> true);
        findIdO.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Oeuvre -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null)
                    return true;
                String searchKeyword = newValue.toLowerCase();
                if (Oeuvre.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Oeuvre.getDescription().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Oeuvre.getLieuDeConservartion().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else
                    return false;

            });
        });
        SortedList<Oeuvre> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableO.comparatorProperty());
        tableO.setItems(sortedData);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // idO.setCellValueFactory(new PropertyValueFactory<Oeuvre, Int>("id"));
        titreO.setCellValueFactory(new PropertyValueFactory<Oeuvre, String>("titre"));
        datecO.setCellValueFactory(new PropertyValueFactory<Oeuvre, Date>("dateCreation"));

        descriptionO.setCellValueFactory(new PropertyValueFactory<Oeuvre, String>("description"));
        lieuO.setCellValueFactory(new PropertyValueFactory<Oeuvre, String>("lieuDeConservartion"));
        //idArt.setCellValueFactory(new PropertyValueFactory<Oeuvre, Integer>("idArtiste"));
        categO.setCellValueFactory(new PropertyValueFactory<Oeuvre, String>("categoriee"));
        nomArt.setCellValueFactory(new PropertyValueFactory<Oeuvre, String>("Artiste"));
        imageO.setCellValueFactory(new PropertyValueFactory<Oeuvre, String>("image"));
        fillTable();
        //   tableO.refresh();
        new BounceIn(tableO).play();


        /*******RECHERCHE********/


    }


}