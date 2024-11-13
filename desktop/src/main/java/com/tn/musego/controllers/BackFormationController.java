package com.tn.musego.controllers;

import animatefx.animation.BounceIn;
import com.tn.musego.entities.Formation;
import com.tn.musego.services.impl.FormationService;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class BackFormationController implements Initializable {
    @FXML
    TableView<Formation> tablebackformation;
    @FXML
    private Button btnaddfor;

    @FXML
    private Button viewCalendar;
    @FXML
    private TextField filterField;
    private ObservableList<Formation> formationlist = FXCollections.observableArrayList();


    @FXML
    public void searchBarTextDidChange() {
        if (filterField.getText().isEmpty()) {
            tablebackformation.setItems(formationlist);
        } else {
            ObservableList<Formation> filteredData = FXCollections.observableArrayList();
            for (Formation item : formationlist) {
                if (item.getNom().toLowerCase().contains(filterField.getText().toLowerCase())
                        || item.getDateDebut().toString().toLowerCase().contains(filterField.getText().toLowerCase())
                        || item.getDateFin().toString().toLowerCase().contains(filterField.getText().toLowerCase())
                        || item.getNiveau().label.toLowerCase().contains(filterField.getText().toLowerCase())
                ) {
                    filteredData.add(item);
                }
            }
            tablebackformation.setItems(filteredData);
        }
    }



    public void initList(ObservableList<Formation> formationlist, TableView<Formation> tableView) {
        FormationService formationService = new FormationService();
        TableColumn nom = new TableColumn("Nom");
        TableColumn atelier = new TableColumn("Atelier");
        TableColumn date_debut = new TableColumn("Date debut");
        TableColumn date_fin = new TableColumn("Date fin");
        TableColumn niveau = new TableColumn("Niveau");
        TableColumn nom_formateur = new TableColumn("Formateur"); //rendre nom formateur
        TableColumn action1 = new TableColumn("Modifier?");
        TableColumn action2 = new TableColumn("Supprimer?");


        tableView.getColumns().addAll(nom, atelier, date_debut, date_fin, niveau,nom_formateur, action1, action2);

//        id.setCellValueFactory(new PropertyValueFactory<Formation, Integer>("id"));
        nom.setCellValueFactory(new PropertyValueFactory<Formation, String>("nom"));
        atelier.setCellValueFactory(new PropertyValueFactory<Formation, Integer>("nomAtelier"));

        date_debut.setCellValueFactory(new PropertyValueFactory<Formation, String>("dateDebut"));
        date_fin.setCellValueFactory(new PropertyValueFactory<Formation, String>("dateFin"));
        niveau.setCellValueFactory(new PropertyValueFactory<Formation, String>("niveau"));
        nom_formateur.setCellValueFactory(new PropertyValueFactory<Formation, Integer>("nomFormateur"));
        //formateur_id.setCellValueFactory(new PropertyValueFactory<Formation, String>("formateur"));
//        formateur_id.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Formation, String>, ObservableValue<String>>) p -> {
//            // p.getValue() returns the Person instance for a particular TableView row
//            return new SimpleStringProperty(returnUserNameByID((int) p.getValue().getId()));
//        });TODO fix this method


        action1.setCellFactory(ActionButtonTableCell.forTableColumn("modifier", (Formation p) -> {

            ModifFormationController.formation = p;
            try {
                affichermodifFormation();

                this.listAll(formationlist, tablebackformation);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return p;
        }));
        action2.setCellFactory(ActionButtonTableCell.forTableColumn("Supprimer", (Formation p) -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ATTENTION");
            alert.setHeaderText("ATTENTION");
            alert.setContentText("Attention, vous etes sur le point de supprimer une formation.\nVoulez-vous poursuivre ?");

            ButtonType confirmerButton = new ButtonType("Confirmer");
            ButtonType annulerButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(confirmerButton, annulerButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == confirmerButton) {
                formationService.deleteEntityById(p.getId());
                tableView.getItems().remove(p);
            }

            return p;
        }));


        tableView.setItems(formationlist);
    }

    public void listAll(ObservableList<Formation> formationlist, TableView<Formation> tableView) {
        tableView.getItems().removeAll(formationlist);
        formationlist.addAll(new FormationService().getAll());
        tableView.setItems(formationlist);
    }

    public void afficherajoutFormation() throws IOException {
        FunctionHelper.openNewStage(Routes.BACK_ADD_FORMATION, "Nouvelle formation");

        this.listAll(formationlist, tablebackformation);
    }

    public void affichermodifFormation() throws IOException {
        FunctionHelper.openNewStage(Routes.BACK_UPDATE_FORMATION, "Modifier formation");

    }

    @FXML
    void viewCalendar(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(Routes.BACK_CALENDAR_FORMATION));
        Parent root = loader.load();

        BackFormationCalendar controller2 = loader.getController();
        controller2.initCalendarEntries(formationlist);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Calendrier des formations");
        stage.getIcons().add(new Image(getClass().getResource("/images/icon.png").openStream()));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.initList(formationlist, tablebackformation);
        this.listAll(formationlist, tablebackformation);
        new BounceIn(tablebackformation).play();
    }
}
