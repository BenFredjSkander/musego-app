package com.tn.musego.controllers;

import animatefx.animation.BounceIn;
import com.tn.musego.HelloApplication;
import com.tn.musego.entities.Abonnement;
import com.tn.musego.services.impl.AbonnementService;
import com.tn.musego.services.impl.OffreService;
import com.tn.musego.utils.ActionButtonTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class BackListeAbonnementController implements Initializable {
    @FXML
    TableView<Abonnement> tableView;

    @FXML
    private TableColumn<Abonnement, String> abonnementType;
    @FXML
    private TableColumn<Abonnement, Integer> abonnementPrix;
    @FXML
    private TableColumn<Abonnement, Date> abonnementDatedeb;
    @FXML
    private TableColumn<Abonnement, Date> abonnementDatefin;

    @FXML
    private TableColumn<Abonnement, Integer> abonnementOffre;
    @FXML
    private TableColumn<Abonnement, Integer> abonnementUser;
    @FXML
    private TableColumn<Abonnement, Button> actionmodif;
    @FXML
    private TableColumn<Abonnement, Button> actionsupp;

    @FXML
    TextField filterField;

    private ObservableList<Abonnement> abonnementlist = FXCollections.observableArrayList();


    @FXML
    public void searchBarTextDidChange() {
        if (filterField.getText().isEmpty()) {
            tableView.setItems(abonnementlist);
        } else {
            ObservableList<Abonnement> filteredData = FXCollections.observableArrayList();
            for (Abonnement item : abonnementlist) {
                if (item.getType().toString().toLowerCase().contains(filterField.getText().toLowerCase())
                        || item.getPrix().toString().toLowerCase().contains(filterField.getText().toLowerCase())
                        || item.getDateDebut().toString().toLowerCase().contains(filterField.getText().toLowerCase())
                        || item.getDateFin().toString().toLowerCase().contains(filterField.getText().toLowerCase())) {
                    filteredData.add(item);
                }
            }
            tableView.setItems(filteredData);
        }
    }

    public void initList(ObservableList<Abonnement> abonnementlist, TableView<Abonnement> tableView) {

        AbonnementService abonnementService = new AbonnementService();
        abonnementType.setCellValueFactory(new PropertyValueFactory<>("type"));
        abonnementPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        abonnementDatedeb.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        abonnementDatefin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));

        abonnementOffre.setCellValueFactory(new PropertyValueFactory<>("idOffre"));
        abonnementUser.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        actionmodif.setCellFactory(ActionButtonTableCell.forTableColumn("Modifier", (Abonnement p) -> {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("edit-abonnement.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            EditAbonnementController editAbonnementController = loader.getController();
            editAbonnementController.initFields(p);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
            fillTable();
            return p;
        }));
        actionsupp.setCellFactory(ActionButtonTableCell.forTableColumn("Supprimer", (Abonnement p) -> {
            try {
                TestController.createStage("/com/tn/musego/confirmer-action.fxml", "Suppression d'un abonnement");
                if (ConfirmationActionController.sign) {
                    abonnementService.deleteEntityById(p.getId());
                    tableView.getItems().remove(p);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return p;
        }));

        tableView.setItems(abonnementlist);

        FilteredList<Abonnement> filteredList = new FilteredList<>(abonnementlist, b -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(abonnement -> {
                if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (abonnement.getType().toString().toLowerCase().indexOf(lowerCaseFilter) > -1) {
                    return true;
                } else if (abonnement.getPrix().toString().toLowerCase().indexOf(lowerCaseFilter) > -1) {
                    return true;
                } else if (abonnement.getDateDebut().toString().toLowerCase().indexOf(lowerCaseFilter) > -1) {
                    return true;
                } else if (abonnement.getDateFin().toString().toLowerCase().indexOf(lowerCaseFilter) > -1) {
                    return true;
                } else return false;
            });
        });

        SortedList<Abonnement> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedList);

    }

    public void listAll(ObservableList<Abonnement> abonnementlist, TableView<Abonnement> tableView) {
        tableView.getItems().removeAll(abonnementlist);
        abonnementlist.addAll(new AbonnementService().getAll());
        tableView.setItems(abonnementlist);
    }

    @FXML
    private void getAddView() {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-abonnement.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Ajout d'un abonnement!");
        stage.setScene(scene);
        stage.showAndWait();
        fillTable();
    }

    public void fillTable() {
        tableView.getItems().clear();
        AbonnementService abonnementService = new AbonnementService();
        abonnementlist = abonnementService.getAll();
        tableView.setItems(abonnementlist);

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new BounceIn(tableView).play();

        this.initList(abonnementlist, tableView);
        this.listAll(abonnementlist, tableView);

    }
}
