package com.tn.musego.controllers;

import animatefx.animation.BounceIn;
import com.tn.musego.HelloApplication;
import com.tn.musego.entities.Offre;
import com.tn.musego.services.impl.ArtisteService;
import com.tn.musego.services.impl.OffreService;
import com.tn.musego.utils.ActionButtonTableCell;
import com.tn.musego.utils.FunctionHelper;
import com.tn.musego.utils.Routes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class BackListeOffreController implements Initializable {
    @FXML
    private TableView<Offre> tableView;

    private ObservableList<Offre> offrelist = FXCollections.observableArrayList();

    @FXML
    private TextField filterField;

    @FXML
    private TableColumn<Offre, String> offreType;
    @FXML
    private TableColumn<Offre, Integer> offrePrix;
    @FXML
    private TableColumn<Offre, Integer> offrePromotion;
    @FXML
    private TableColumn<Offre, Date> offreDatedeb;
    @FXML
    private TableColumn<Offre, Date> offreDatefin;
    @FXML
    private TableColumn<Offre, Button> actionmodif;
    @FXML
    private TableColumn<Offre, Button> actionsupp;

    @FXML
    public void searchBarTextDidChange() {
        if (filterField.getText().isEmpty()) {
            tableView.setItems(offrelist);
        } else {
            ObservableList<Offre> filteredData = FXCollections.observableArrayList();
            for (Offre item : offrelist) {
                if (item.getType().toString().toLowerCase().contains(filterField.getText().toLowerCase())
                        || item.getPrix().toString().toLowerCase().contains(filterField.getText().toLowerCase())
                        || item.getPromotion().toString().toLowerCase().contains(filterField.getText().toLowerCase())
                        || item.getDateDebut().toString().toLowerCase().contains(filterField.getText().toLowerCase())
                        || item.getDateFin().toString().toLowerCase().contains(filterField.getText().toLowerCase())) {
                    filteredData.add(item);
                }
            }
            tableView.setItems(filteredData);
        }
    }

    public void initList(ObservableList<Offre> offrelist, TableView<Offre> tableView) {

        OffreService offreService = new OffreService();

        offreType.setCellValueFactory(new PropertyValueFactory<>("type"));
        offrePrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        offrePromotion.setCellValueFactory(new PropertyValueFactory<>("promotion"));
        offreDatedeb.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        offreDatefin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        actionmodif.setCellFactory(ActionButtonTableCell.forTableColumn("Modifier", (Offre p) -> {
            /*EditOffreController.offre = p;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Routes.BACK_UPDATE_OFFRE));
                Parent root;
                try {
                    root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    Scene sc = new Scene(root, 824, 412);
                    stage.setScene(sc);
                    stage.showAndWait();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (EditOffreController.sign) {
                    this.listAll(offrelist, tableView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return p;*/
            //
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("edit-offre.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            EditOffreController editOffreController = loader.getController();
            editOffreController.initFields(p);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
            fillTable();
            return p;
        }));
        actionsupp.setCellFactory(ActionButtonTableCell.forTableColumn("Supprimer", (Offre p) -> {
            try {
                TestController.createStage("/com/tn/musego/confirmer-action.fxml", "Suppression d'une offre");
                if (ConfirmationActionController.sign) {
                    offreService.deleteEntityById(p.getId());
                    tableView.getItems().remove(p);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return p;
        }));
    }

    public void listAll(ObservableList<Offre> offrelist, TableView<Offre> tableView) {
        tableView.getItems().clear();
        offrelist.addAll(new OffreService().getAll());
        tableView.setItems(offrelist);
    }

    public void getAddView(ActionEvent event) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-offre.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Ajout d'une offre!");
        stage.setScene(scene);
        stage.showAndWait();
        fillTable();

    }

    public void fillTable() {
        tableView.getItems().clear();
        OffreService offreService = new OffreService();
        offrelist = offreService.getAll();
        tableView.setItems(offrelist);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new BounceIn(tableView).play();

        this.initList(offrelist, tableView);
        this.listAll(offrelist, tableView);
    }
}
