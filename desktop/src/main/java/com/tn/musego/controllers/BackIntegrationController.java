//package com.tn.musego.controllers;
//
//import com.tn.musego.entities.Formation;
//import com.tn.musego.entities.Integration;
//import com.tn.musego.services.impl.IntegrationService;
//import com.tn.musego.utils.ActionButtonTableCell;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.HBox;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class BackIntegrationController {
//
//    @FXML
//    private HBox navbar_abonnement;
//
//    @FXML
//    private HBox navbar_ateliers;
//
//    @FXML
//    private HBox navbar_avis;
//
//    @FXML
//    private HBox navbar_compte;
//
//    @FXML
//    private HBox navbar_event;
//
//    @FXML
//    private HBox navbar_home;
//
//    @FXML
//    private HBox oeuvre_id;
//
//    @FXML
//    void goToAbonnement(MouseEvent event) {
//
//    }
//
//    @FXML
//    void goToAtelier(MouseEvent event) {
//
//    }
//
//    @FXML
//    void goToAvis(MouseEvent event) {
//
//    }
//
//    @FXML
//    void goToEvents(MouseEvent event) {
//
//    }
//
//    @FXML
//    void goToHome(MouseEvent event) {
//
//    }
//
//    @FXML
//    void goToOeuvre(MouseEvent event) {
//
//    }
//
//    @FXML
//    void goToProfile(MouseEvent event) {
//
//    }
//
//    @FXML
//    TableView<Integration> tablebackintegration;
//    private ObservableList<Integration> integrationlist = FXCollections.observableArrayList();
//
//    @FXML
//    private void initialize() {
//
//        this.initList(integrationlist, tablebackintegration);
//        this.listAll(integrationlist, tablebackintegration);
//
//    }
//
//
//    public void initList(ObservableList<Integration> integrationlist, TableView<Integration> tableView) {
//        IntegrationService integrationService = new IntegrationService();
//        TableColumn id_user = new TableColumn("id_user");
//        TableColumn id_formation = new TableColumn("id_formation");
//        TableColumn action2 = new TableColumn("supprimer?");
//
//
//        tableView.getColumns().addAll(id_user, id_formation, action2);
//
//        id_user.setCellValueFactory(new PropertyValueFactory<Formation, Integer>("id_user"));
//        id_formation.setCellValueFactory(new PropertyValueFactory<Formation, Integer>("id_formation"));
//        action2.setCellFactory(ActionButtonTableCell.<Integration>forTableColumn("Supprimer", (Integration i) -> {
//            integrationService.supprimerIntegration(i.getIdUser(), i.getIdFormation());
//            tableView.getItems().remove(i);
//            return i;
//        }));
//
//
//        tableView.setItems(integrationlist);
//    }
//
//    public void listAll(ObservableList<Integration> integrationlist, TableView<Integration> tableView) {
//        integrationlist.addAll(new IntegrationService().getAll());
//        tableView.setItems(integrationlist);
//    }
//
//    public void afficherajoutIntegration() throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/tn/musego/ajoutIntegration.fxml"));
//        Parent root = null;
//        try {
//            root = fxmlLoader.load();
//            Stage stage = new Stage();
//            stage.initModality(Modality.APPLICATION_MODAL);
//            //stage.initStyle(StageStyle.UNDECORATED);
//            Scene sc = new Scene(root, 824, 412);
//            stage.setScene(sc);
//            stage.showAndWait();
//        } catch (IOException ex) {
//            System.err.println(ex.getMessage());
//            ex.printStackTrace();
//        }
//
//    }
//
//}
