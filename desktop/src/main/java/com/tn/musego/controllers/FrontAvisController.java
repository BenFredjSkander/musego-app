package com.tn.musego.controllers;

import animatefx.animation.BounceIn;
import com.tn.musego.entities.Avis;
import com.tn.musego.services.impl.AvisService;
import com.tn.musego.services.impl.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


public class FrontAvisController {
    private final ObservableList<Avis> Avislist = FXCollections.observableArrayList();
    Avis a = new Avis();
    @FXML
    TextField filterField;
    @FXML
    private TableView<Avis> tableView;
    @FXML
    private TableColumn<Avis, String> typee;
    @FXML
    private TableColumn<Avis, String> description;
    @FXML
    private TableColumn<Avis, String> avis_sur;
    @FXML
    private TextField typetext;
    @FXML
    private TextField avissurtext;
    @FXML
    private TextArea descriptiontext;
    @FXML
    private TextField id_usertext;
    @FXML
    private TextField id_rech;
    @FXML
    private TextField new_type;
    @FXML
    private TextField new_description;
    @FXML
    private TextField new_avis_sur;
    @FXML
    private TextField user;
    @FXML
    private Button selectbutt;
    @FXML
    private ComboBox<String> combobox;

    @FXML
    void ajouterAvis(MouseEvent mouseEvent) {

        AvisService avisService = new AvisService();
        Avis a = new Avis();

        String type = typetext.getText();
        String description = descriptiontext.getText();
        String avis_sur = avissurtext.getText();

        a.setType(type);
        a.setDescription(description);
        a.setAvisSur(avis_sur);
        a.setIdUser(new UserService().getCurrentUserID().intValue());

        avisService.ajouterAvis(a);

        tableView.getItems().addAll(a);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajout d'un avis");
        alert.setHeaderText("Information");
        alert.setContentText("Avis ajouté !");
        alert.showAndWait();


    }

    @FXML
    void supprimerAvis(MouseEvent mouseEvent) {
        AvisService as = new AvisService();
        // Récupère la ligne sélectionnée
        Avis selectedPersonne = tableView.getSelectionModel().getSelectedItem();

// Vérifie que la ligne sélectionnée est valide
        if (selectedPersonne != null) {
            as.supprimerAvis(selectedPersonne.getId());

            // Rafraîchit le TableView pour afficher la mise à jour
            tableView.getItems().remove(selectedPersonne);
            tableView.refresh();
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

    @FXML
    private void initialize() {

        new BounceIn(tableView).play();

        this.initList(Avislist, tableView);
        this.listAll(Avislist, tableView);

    }


    public void initList(ObservableList<Avis> avislist, TableView<Avis> tableView) {

//combobox.setItems( FXCollections.observableArrayList("evenement","formation","oeuvre"));
        AvisService avisService = new AvisService();

        TableColumn type = new TableColumn("type");
        TableColumn description = new TableColumn("description");
        TableColumn avis_sur = new TableColumn("avis_sur");


        tableView.getColumns().addAll(type, description, avis_sur);


        type.setCellValueFactory(new PropertyValueFactory<Avis, String>("type"));
        description.setCellValueFactory(new PropertyValueFactory<Avis, String>("description"));
        avis_sur.setCellValueFactory(new PropertyValueFactory<Avis, String>("avis_sur"));
        tableView.setItems(avislist);


    }
    /*public void selectUser() {
        Avislist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                new_type.setText(newValue.getType());
                new_description.setText(newValue.getDescription());
                new_avis_sur.setText(newValue.getAvis_sur());
            }
        });*/


    public void listAll(ObservableList<Avis> avislist, TableView<Avis> tableView) {
        tableView.getItems().removeAll(avislist);
        avislist.addAll(new AvisService().afficherAvis());
        tableView.setItems(Avislist);
        tableView.refresh();

    }

    public void rechercherAvis() {


        int id_rechValue = Integer.parseInt(id_rech.getText());
        int row = -1;
        ObservableList<Avis> data = tableView.getItems();

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
            tableView.getSelectionModel().select(row);
        }
    }

    @FXML
    private void selectitems(ActionEvent actionEvent) {
        try {

            Avis selectedObject = tableView.getSelectionModel().getSelectedItem();

            // Vérifie que la ligne sélectionnée n'est pas nulle
            if (selectedObject != null) {
                // Affiche les informations dans les TextField correspondants
                new_type.setText(selectedObject.getType());
                new_description.setText(selectedObject.getDescription());
                new_avis_sur.setText(selectedObject.getAvisSur());

            }
        } finally {

        }
    }


    @FXML
    void modifierAvis(MouseEvent mouseEvent) {


        Avis a = new Avis();

        String type = new_type.getText();
        String description = new_description.getText();
        String avis_sur = new_avis_sur.getText();
        AvisService avisService = new AvisService();
        avisService.modifierAvis(a);
        tableView.refresh();
        avisService.afficherAvis();

    }


}




