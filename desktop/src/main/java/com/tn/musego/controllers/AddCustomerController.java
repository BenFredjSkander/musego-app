package com.tn.musego.controllers;

import com.tn.musego.entities.PaymentCard;
import com.tn.musego.entities.User;
import com.tn.musego.entities.enums.TypePaymentCard;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.PaymentService;
import com.tn.musego.services.impl.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    @FXML
    private ComboBox<TypePaymentCard> choice;


    public static boolean sign;

    @FXML
    private Button btnadd;

    private User currentUser;

    @FXML
    void addBut(ActionEvent event){
        try {
            PaymentService paymentService = new PaymentService();
            String id = paymentService.createNewCustomer(choice.getValue().toString(), currentUser.getEmail(), new UserService().getCurrentLoggedinUser().getUsername());
            //this method adds 10000 by default to user account
            paymentService.chargeNewCustomer(id, 10000);

            PaymentCard paymentCard = new PaymentCard(null, id, choice.getValue(), 10000D, currentUser.getId());
            paymentService.createEntity(paymentCard);
            sign=true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ajouter carte de paiement");
            alert.setHeaderText("Information");
            alert.setContentText("Votre carte est bien ajouté");
            alert.showAndWait();
            Stage stage = (Stage) btnadd.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ajouter carte de paiement");
            alert.setHeaderText("Information");
            alert.setContentText("Votre carte n'est pas ajouté");
            alert.showAndWait();
        }

    }

    @FXML
    void handleComboBoxAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sign=false;
        ObservableList obList = FXCollections.observableArrayList(TypePaymentCard.values());
        choice.setValue(TypePaymentCard.tok_visa);
        choice.getItems().addAll(obList);
        try {
            currentUser = new UserService().getCurrentLoggedinUser();
        } catch (MyCustomException e) {
            throw new RuntimeException(e);
        }
    }
}
