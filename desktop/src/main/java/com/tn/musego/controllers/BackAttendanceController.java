package com.tn.musego.controllers;

import com.tn.musego.entities.Attendance;
import com.tn.musego.services.impl.AttendanceService;
import com.tn.musego.utils.ActionButtonTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class BackAttendanceController {

    @FXML
    TableView<Attendance> tablebackattendance;
    private ObservableList<Attendance> attendancelist = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        this.initList(attendancelist, tablebackattendance);
        this.listAll(attendancelist, tablebackattendance);

    }//initialiser l'interface backAttendance


    public void initList(ObservableList<Attendance> attendancelist, TableView<Attendance> tableView) {
        AttendanceService attendanceService = new AttendanceService();
        TableColumn id_user = new TableColumn("id_user");
        TableColumn id_formation = new TableColumn("id_formation");
        TableColumn action2 = new TableColumn("supprimer?");


        tableView.getColumns().addAll(id_user, id_formation, action2);

        id_user.setCellValueFactory(new PropertyValueFactory<Attendance, Integer>("id_user"));
        id_formation.setCellValueFactory(new PropertyValueFactory<Attendance, Integer>("id_formation"));

        action2.setCellFactory(ActionButtonTableCell.<Attendance>forTableColumn("Supprimer", (Attendance a) -> {
            attendanceService.supprimerAttendance(a.getIdUser(), a.getIdFormation());
            tableView.getItems().remove(a);
            return a;
        }));


        tableView.setItems(attendancelist);
    }        //permet d'initialiser la table view pour  sotcker les données


    public void listAll(ObservableList<Attendance> attendancelist, TableView<Attendance> tableView) {
        attendancelist.addAll(new AttendanceService().getAll());
        tableView.setItems(attendancelist);
    }//permet d'afficher les données de la table view

    public void afficherajoutAttendance() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/tn/musego/ajoutAttendance.fxml")); //redirection
        Parent root = null;
        try {
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            //stage.initStyle(StageStyle.UNDECORATED);
            Scene sc = new Scene(root, 824, 412);
            stage.setScene(sc);
            stage.showAndWait();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }

    }//permet d'afficher le bouton ajout

}
