package com.tn.musego.controllers;

import animatefx.animation.BounceIn;
import com.tn.musego.entities.Avis;
import com.tn.musego.services.impl.AvisService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


public class BackAvisController {

    @FXML
    private TableView<Avis> tableview2;
    @FXML
    private TableColumn<Avis, String> type;
    @FXML
    private TableColumn<Avis, String> description;
    @FXML
    private TableColumn<Avis, String> avis_sur;
    @FXML
    private TextField typet;
    @FXML
    private TextField recht;

    @FXML
    private TextField dest;
    @FXML
    private TextField avist;


    @FXML
    void ajouterAvis(MouseEvent mouseEvent) {

        AvisService avisService = new AvisService();
        Avis a = new Avis();

        String type = typet.getText();
        String description = dest.getText();
        String avis_sur = avist.getText();

        avisService.ajouterAvis(a);


        a.setType(type);
        a.setDescription(description);
        a.setAvisSur(avis_sur);

        tableview2.getItems().addAll(a);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajout d'une rep");
        alert.setHeaderText("Information");
        alert.setContentText("rep ajouté !");
        alert.showAndWait();


    }

    public void listAll(ObservableList<Avis> avislist, TableView<Avis> tableView) {
        tableView.getItems().removeAll(avislist);
        avislist.addAll(new AvisService().afficherAvis());
        tableView.setItems(replist);
        tableView.refresh();

    }

    private final ObservableList<Avis> replist = FXCollections.observableArrayList();


    @FXML
    private void initialize() {
        new BounceIn(tableview2).play();


        this.initList(replist, tableview2);
        this.listAll(replist, tableview2);

    }


    public void initList(ObservableList<Avis> avislist, TableView<Avis> tableView) {

//combobox.setItems( FXCollections.observableArrayList("evenement","formation","oeuvre"));
        AvisService avisService = new AvisService();

        TableColumn type = new TableColumn("type");
        TableColumn description = new TableColumn("description");
        TableColumn avis_sur = new TableColumn("avis_sur");


        tableview2.getColumns().addAll(type, description, avis_sur);


        type.setCellValueFactory(new PropertyValueFactory<Avis, String>("type"));
        description.setCellValueFactory(new PropertyValueFactory<Avis, String>("description"));
        avis_sur.setCellValueFactory(new PropertyValueFactory<Avis, String>("avis_sur"));

        tableView.setItems(avislist);


    }

    public void supprimerAvis(ActionEvent event) {
        AvisService as = new AvisService();
        // Récupère la ligne sélectionnée
        Avis selectedPersonne = tableview2.getSelectionModel().getSelectedItem();

// Vérifie que la ligne sélectionnée est valide
        if (selectedPersonne != null) {
            as.supprimerAvis(selectedPersonne.getId());

            // Rafraîchit le TableView pour afficher la mise à jour
            tableview2.getItems().remove(selectedPersonne);
            tableview2.refresh();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Suppression d'un avis");
            alert.setHeaderText("Information");
            alert.setContentText("avis supprimé !");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Suppression d'un avis");
            alert.setHeaderText("Information");
            alert.setContentText("Veuillez selectionner l'avis a supprimer !");
            alert.showAndWait();
        }


    }

    public void rechercherAvis(ActionEvent event) {
        int id_rechValue = Integer.parseInt(recht.getText());
        int row = -1;
        ObservableList<Avis> data = tableview2.getItems();

        // Recherche de la ligne avec l'id correspondant
        for (int i = 0; i < data.size(); i++) {
            Avis ligne = data.get(i);
            if (ligne.getIdUser() == id_rechValue) {
                row = i;
                break;
            }
        }

        // Sélection de la ligne correspondante
        if (row >= 0) {
            tableview2.getSelectionModel().select(row);
        }
    }


}
