package com.tn.musego.controllers;

import com.tn.musego.entities.Artiste;
import com.tn.musego.services.impl.ArtisteService;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class FrontArtistesController implements Initializable {
    // create list view and add data to it
    ArtisteService as = new ArtisteService();

    ListView<String> listView = new ListView<String>();


    // create layout and scene
    @FXML
    private TextField findIdA;


    @FXML
    private VBox myVbox;
    @FXML
    private TextField findA;


    @FXML
    private Button btnRecherche;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //myVbox.getChildren().add(listView);
        //  Scene scene = new Scene(myVbox, 400, 400);

        // show the stage
        //Stage stage=new Stage();
        //     stage.setScene(scene);
        //       stage.show();
        listView.getItems().add(as.afficherArtistes().toString());
        /*******RECHERCHE********/
        // Créer une grille pour organiser les contrôles de recherche
        GridPane searchGrid = new GridPane();
        searchGrid.setHgap(10);
        searchGrid.setVgap(10);
        searchGrid.addRow(0, new Label("Artiste:"), findA);

        //  searchGrid.add(btnRecherche, 2, 3);

        FilteredList<Artiste> filteredData = new FilteredList<>(as.afficherArtistes(), b -> true);
        findA.textProperty().addListener((observable, oldValue, newValue) -> {
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
        // sortedData.comparatorProperty().bind(listView);
        //  listView.setItems(sortedData);
        myVbox.getChildren().addAll(searchGrid, listView);

    }
}
