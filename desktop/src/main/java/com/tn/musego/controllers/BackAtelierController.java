package com.tn.musego.controllers;

import animatefx.animation.BounceIn;
import com.tn.musego.entities.Atelier;
import com.tn.musego.services.impl.AtelierService;
import com.tn.musego.utils.ActionButtonTableCell;
import com.tn.musego.utils.FunctionHelper;
import com.tn.musego.utils.Routes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class BackAtelierController implements Initializable {

    @FXML
    private Button btnaddatelier;

    @FXML
    private TextField filterField;

    @FXML
    private TableView<Atelier> tablebackatelier;
    private ObservableList<Atelier> atelierlist = FXCollections.observableArrayList();


    @FXML
    void searchBarTextDidChange(ActionEvent event) {
        if (filterField.getText().isEmpty()) {
            tablebackatelier.setItems(atelierlist);
        } else {
            ObservableList<Atelier> filteredData = FXCollections.observableArrayList();
            for (Atelier item : atelierlist) {
                if (item.getNom().toLowerCase().contains(filterField.getText().toLowerCase())
                        || item.getCreated_at().toString().toLowerCase().contains(filterField.getText().toLowerCase())
                ) {
                    filteredData.add(item);
                }
            }
            tablebackatelier.setItems(filteredData);
        }

    }



    private void initList(ObservableList<Atelier> atelierlist, TableView<Atelier> tableView) {
        AtelierService atelierService = new AtelierService();
        TableColumn nom = new TableColumn("nom");
        TableColumn created_at = new TableColumn("created_at");
        TableColumn image = new TableColumn("image");
        TableColumn action1 = new TableColumn("Modifier?");
        TableColumn action2 = new TableColumn("Supprimer?");

        tableView.getColumns().addAll(nom, created_at, image, action1, action2);

        nom.setCellValueFactory(new PropertyValueFactory<Atelier, String>("nom"));
        created_at.setCellValueFactory(new PropertyValueFactory<Atelier, String>("created_at"));
        image.setCellValueFactory(new PropertyValueFactory<Atelier, String>("image"));

        action1.setCellFactory(ActionButtonTableCell.forTableColumn("modifier", (Atelier p) -> {

            ModifAtelierController.atelier = p;
            try {
                affichermodifAtelier();

                this.listAll(atelierlist, tablebackatelier);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return p;
        }));

        action2.setCellFactory(ActionButtonTableCell.forTableColumn("Supprimer", (Atelier p) -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ATTENTION");
            alert.setHeaderText("ATTENTION");
            alert.setContentText("Attention, vous etes sur le point de supprimer un atelier.\nVoulez-vous poursuivre ?");

            ButtonType confirmerButton = new ButtonType("Confirmer");
            ButtonType annulerButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(confirmerButton, annulerButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == confirmerButton) {
                atelierService.deleteEntityById(p.getId());
                tableView.getItems().remove(p);
            }

            return p;
        }));

        tableView.setItems(atelierlist);


    }

    public void listAll(ObservableList<Atelier> atelierlist, TableView<Atelier> tableView) {
        tableView.getItems().removeAll(atelierlist);
        atelierlist.addAll(new AtelierService().getAll());
        tableView.setItems(atelierlist);
    }

    public void afficherajoutAtelier() throws IOException {
        FunctionHelper.openNewStage(Routes.BACK_ADD_ATELIER, "Nouvel Atelier");

        this.listAll(atelierlist, tablebackatelier);
    }

    public void affichermodifAtelier() throws IOException {
        FunctionHelper.openNewStage(Routes.BACK_UPDATE_ATELIER, "Modifier atelier");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initList(atelierlist, tablebackatelier);
        this.listAll(atelierlist, tablebackatelier);
        new BounceIn(tablebackatelier).play();

    }
}