//package com.tn.musego.controllers;
//
//import com.tn.musego.entities.Integration;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//
//public class AjoutIntegrationController {
//
//    @FXML
//    private Button btnadd;
//
//    @FXML
//    private Button btncls;
//
//    @FXML
//    private Label lblerr;
//
//    @FXML
//    private TextField tfidformation;
//
//    @FXML
//    private TextField tfiduser;
//
//    @FXML
//    void addBut(ActionEvent event) {
//        try {
//
//            int id_user = Integer.parseInt(tfiduser.getText());
//            int id_formation = Integer.parseInt(tfidformation.getText());
//
//
////----------------------------------------------------------------------------------------------
//
//            if (id_user == 0) lblerr.setText("Le champs 'id_User'  n'est pas rempli");
//            else if (id_formation == 0) lblerr.setText("Le champs 'id_Formation'  n'est pas rempli");
//            else {
//                Integration integration = new Integration(id_user, id_formation);
//                IntegrationService integrationService = new IntegrationService();
//                integrationService.createEntity(integration);
//
//                lblerr.setText("Ajoutée avec succès!");
//            }
//        } catch (Exception e) {
//            lblerr.setText("Une erreur coté backend");
//            e.printStackTrace();
//        }
//
//    }
//
//    @FXML
//    void annulerBut(ActionEvent event) {
//        tfidformation.clear();
//        tfiduser.clear();
//
//    }
//
//}
