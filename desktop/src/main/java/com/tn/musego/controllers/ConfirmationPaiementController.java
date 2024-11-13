package com.tn.musego.controllers;

import com.stripe.exception.StripeException;
import com.tn.musego.entities.Abonnement;
import com.tn.musego.entities.Offre;
import com.tn.musego.entities.PaymentCard;
import com.tn.musego.entities.User;
import com.tn.musego.entities.enums.TypeAbEnum;
import com.tn.musego.entities.enums.TypeOffreEnum;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.AbonnementService;
import com.tn.musego.services.impl.PaymentService;
import com.tn.musego.services.impl.UserService;
import com.tn.musego.utils.DateHelper;
import com.tn.musego.utils.EmailHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ConfirmationPaiementController implements Initializable {

    public static Offre selectedOffre;
    public static boolean sign;
    @FXML
    private Button btnadd;
    @FXML
    private Text lboffre;
    @FXML
    private Text lbmont;
    @FXML
    private ComboBox<TypeAbEnum> choice;
    @FXML
    private ComboBox<PaymentCard> choicePayment;

    private User currentUserId;

    private double priceToPay;

    @FXML
    private void handleComboBoxAction(ActionEvent event) {
        //get the abonnement type then change the price according
        switch (choice.getValue()) {
            case HEBDOMADAIRE -> priceToPay = (5 + selectedOffre.getPrix()) * 7 * (100 - selectedOffre.getPromotion()) / 100;
            case MENSUEL -> priceToPay = (5 + selectedOffre.getPrix()) * 30 * (100 - selectedOffre.getPromotion()) / 100;
            case ANNUEL -> priceToPay = (5 + selectedOffre.getPrix()) * 365 * (100 - selectedOffre.getPromotion()) / 100;
            default -> lbmont.setText(null);
        }
    }

    @FXML
    void addBut(ActionEvent event) throws StripeException {

        AbonnementService abonnementService = new AbonnementService();
        abonnementService.deleteEntityByUserId(currentUserId.getId());
        PaymentService paymentService = new PaymentService();

        Date datefin = null;
        switch (choice.getValue()) {
            case HEBDOMADAIRE -> datefin = DateHelper.dateFromTimestamp(Timestamp.valueOf(LocalDateTime.now().plus(Duration.ofDays(7))));
            case MENSUEL -> datefin = DateHelper.dateFromTimestamp(Timestamp.valueOf(LocalDateTime.now().plus(Duration.ofDays(30))));
            case ANNUEL -> datefin = DateHelper.dateFromTimestamp(Timestamp.valueOf(LocalDateTime.now().plus(Duration.ofDays(365))));
        }
        Abonnement abonnement = new Abonnement(choice.getValue(), Double.parseDouble(lbmont.getText()), DateHelper.dateFromTimestamp(DateHelper.currentTime()), datefin, selectedOffre.getId(), currentUserId.getId());
        abonnementService.createEntity(abonnement);

        paymentService.chargeNewCustomer(choicePayment.getValue().getCustomerId(), (long) Double.parseDouble(lbmont.getText()) * 100);
        PaymentCard paymentCard = new PaymentCard(choicePayment.getValue().getId(), choicePayment.getValue().getCustomerId(), choicePayment.getValue().getType(), choicePayment.getValue().getBalance() - selectedOffre.getPrix(), currentUserId.getId());
        paymentService.updateEntity(paymentCard);


        EmailHelper emailHelper = new EmailHelper();
        emailHelper.confirmationPaiement(currentUserId.getEmail(), currentUserId.getUsername(), selectedOffre.getType(), priceToPay);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation paiement");
        alert.setHeaderText("Information");
        alert.setContentText("Votre paiement est bien confirmé");
        alert.showAndWait();
        sign = true;
        Stage stage = (Stage) btnadd.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sign = false;
        try {
            currentUserId = new UserService().getCurrentLoggedinUser();
        } catch (MyCustomException e) {
            throw new RuntimeException(e);
        }
        lboffre.setText(selectedOffre.getType().toString());
        ObservableList<TypeAbEnum> obList = FXCollections.observableArrayList(TypeAbEnum.values());
        choice.setValue(TypeAbEnum.MENSUEL);
        calculPrice(TypeAbEnum.MENSUEL);
        lbmont.setText(String.valueOf(priceToPay));
        choice.getItems().addAll(obList);
        choice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            calculPrice(newValue);
            lbmont.setText(String.valueOf(priceToPay));
        });
        PaymentService paymentService = new PaymentService();
        List<PaymentCard> paymentCards = paymentService.getAllByUserId(currentUserId.getId());

        choicePayment.setValue(paymentCards.get(0));
        choicePayment.setConverter(new StringConverter<>() {
            @Override
            public String toString(PaymentCard object) {
                if (object != null) {
                    return object.getType().toString();
                } else return "";
            }

            @Override
            public PaymentCard fromString(String string) {
                return choicePayment.getSelectionModel().getSelectedItem();
            }
        });
        choicePayment.setItems(FXCollections.observableList(paymentCards));
    }

    void calculPrice(TypeAbEnum typeAbonnement) {
        switch (typeAbonnement) {
            case HEBDOMADAIRE -> priceToPay = (5 + selectedOffre.getPrix()) * 7 * (100 - selectedOffre.getPromotion()) / 100;
            case MENSUEL -> priceToPay = (5 + selectedOffre.getPrix()) * 30 * (100 - selectedOffre.getPromotion()) / 100;
            case ANNUEL -> priceToPay = (5 + selectedOffre.getPrix()) * 365 * (100 - selectedOffre.getPromotion()) / 100;
        }
    }
}