package com.tn.musego.controllers;

import animatefx.animation.BounceIn;
import com.tn.musego.entities.Offre;
import com.tn.musego.entities.PaymentCard;
import com.tn.musego.services.impl.*;
import com.tn.musego.utils.Routes;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;

public class OffreFrontController implements Initializable {

    OffreService offreService = new OffreService();
    HBox offresHbox;
    @FXML
    private ScrollPane scrollPane;
    Long curentUserId;

    @FXML
    private Button paybtn;


    public HBox displayOffresHbox() {


        List<Offre> offreList = offreService.getAll();

        HBox hBox = new HBox();
        hBox.setSpacing(20); // espacement vertical entre les éléments

        Separator sep=new Separator();
        sep.setMaxWidth(80);
        sep.setValignment(VPos.CENTER);


        //affichage des offres
        for (Offre offre : offreList) {
            HBox hbox = new HBox();

            VBox detailsVBox = new VBox();

            Label nomLabel = new Label("Offre: " + offre.getType().toString());
            nomLabel.setStyle("-fx-font-size: 25px; -fx-text-fill: black; -fx-font-weight: bold;");

            Label prixLabel = new Label("Prix: " + (offre.getPrix().toString()+5) + "€");
            prixLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-weight: bold;");

            Label promLabel = new Label("Promotion: "+offre.getPromotion().toString() + "%");
            promLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-weight: bold;");

            Label descLabel = new Label(offre.getDescription());
            descLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-weight: bold;");

            LocalDate currentDate = LocalDate.now();
            Date datedeb = offre.getDateDebut();
            Timestamp timestampDebut = new Timestamp(datedeb.getTime());

            Date datefin = offre.getDateFin();
            Timestamp timestampFin = new Timestamp(datefin.getTime());

            Label statusLabel = new Label();
            Circle statusIndicator = new Circle(8);
            VBox buttonsVBox = new VBox();
            buttonsVBox.setSpacing(8); // espace 8 pixels


            HBox statusHBox = new HBox();
            statusHBox.setSpacing(5);
            statusHBox.setAlignment(Pos.CENTER_LEFT);
            statusHBox.getChildren().addAll(statusIndicator, statusLabel);

            Label datedebutLabel = new Label("Date début: " + offre.getDateDebut().toString().substring(0, 10));
            datedebutLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: black;");

            Label datefinLabel = new Label("Date fin: " + offre.getDateFin().toString().substring(0, 10));
            datefinLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: black;");

            //
            AbonnementService p = new AbonnementService();


            long offreId = offre.getId();
            boolean isUserRegistered = p.isUserSubscribed(curentUserId, offreId);
            ObservableList<PaymentCard> observableList= new PaymentService().getAllByUserId(curentUserId);


            if (currentDate.isBefore(timestampFin.toLocalDateTime().toLocalDate())) {

                statusLabel.setText("Offre disponible");
                statusIndicator.setFill(Color.GREEN);
                if (!isUserRegistered) {
                    if(!observableList.isEmpty()) {
                        Button abonnerButton = new Button("S'abonner");
                        abonnerButton.setStyle("-fx-background-color: #378d2c; -fx-text-fill: white; -fx-font-weight: bold;");
                        buttonsVBox.getChildren().add(abonnerButton);

                        abonnerButton.setOnAction(event -> {
                            addAbonnement(curentUserId, offre);
                        });
                    }
                    else {
                        Button abonnerButton = new Button("Ajouter une carte");
                        abonnerButton.setStyle("-fx-background-color: #000; -fx-text-fill: white; -fx-font-weight: bold;");
                        buttonsVBox.getChildren().add(abonnerButton);

                        abonnerButton.setOnAction(event -> {
                            try {
                                TestController.createStage("/com/tn/musego/add-customer.fxml", "Ajouter une carte de paiement");
                                if (AddCustomerController.sign) {
                                    scrollPane.setContent(offresHbox);
                                    Parent root = null;
                                    try {
                                        AuthService authService = new AuthService();
                                        authService.logout();
                                        root = FXMLLoader.load(getClass().getResource(Routes.LOGIN));
                                    } catch (IOException | BackingStoreException e) {
                                        throw new RuntimeException(e);
                                    }

                                    Scene scene = new Scene(root);
                                    Stage stage = (Stage) paybtn.getScene().getWindow();
                                    stage.setScene(scene);
                                    stage.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }


                if (isUserRegistered) {
                    Button supprimerButton = new Button("Annuler");
                    supprimerButton.setStyle("-fx-background-color: #b31d1d; -fx-text-fill: white; -fx-font-weight: bold;");
                    buttonsVBox.getChildren().add(supprimerButton);

                    supprimerButton.setOnAction(event -> {
                        deleteAbonnement(curentUserId, offreId);
                    });


                    buttonsVBox.setAlignment(Pos.CENTER_RIGHT);
                    buttonsVBox.setPadding(new Insets(10));
                }


            } else if (currentDate.isAfter(timestampFin.toLocalDateTime().toLocalDate())) {
                statusLabel.setText("Abonnement Terminée");
                statusIndicator.setFill(Color.RED);
            } else {
                statusLabel.setText("Abonnement Terminée");
                statusIndicator.setFill(Color.RED);
            }


            detailsVBox.getChildren().addAll(nomLabel, descLabel, prixLabel,promLabel, statusHBox, datedebutLabel, datefinLabel, buttonsVBox);

            if(offre.getImage()!=null){

                ImageView imageView = new ImageView(new Image(offre.getImage()));
                imageView.setFitWidth(150);
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(150);
                new BounceIn(imageView).play();
                hbox.getChildren().addAll(imageView,detailsVBox,sep);
            }
            else hbox.getChildren().addAll(detailsVBox,sep);
            hbox.setSpacing(20);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setPadding(new Insets(10));

            hBox.getChildren().add(hbox);

        }
        return hBox;
    }

    public void addAbonnement(Long userId, Offre offre) {
        AbonnementService abonnementService = new AbonnementService();

        ////////verification tee user  si existe ou non /////////
        if (abonnementService.isUserSubscribed(userId, offre.getId())) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Vous êtes déjà abonné à cette offre !");
            alert.showAndWait();


        } else {
            try {
                ConfirmationPaiementController.selectedOffre = offre;
                TestController.createStage("/com/tn/musego/confirmer-paiement.fxml", "Confirmation paiement (Gold)");

                if (ConfirmationPaiementController.sign) {
                    scrollPane.setContent(offresHbox);
                    Parent root = null;
                    try {
                        AuthService authService = new AuthService();
                        authService.logout();
                        root = FXMLLoader.load(getClass().getResource(Routes.LOGIN));
                    } catch (IOException | BackingStoreException e) {
                        throw new RuntimeException(e);
                    }

                    Scene scene = new Scene(root);
                    Stage stage = (Stage) paybtn.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteAbonnement(Long userId, long offreId) {
        AbonnementService abonnementService = new AbonnementService();

        if (abonnementService.isUserSubscribed(userId, offreId)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Etes-vous certain de vouloir annuler votre abonnement?");

            ButtonType confirmerButton = new ButtonType("Confirmer");
            ButtonType annulerButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(confirmerButton, annulerButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == confirmerButton) {
                abonnementService.deleteEntityByUserIdOffreId(userId, offreId);
                scrollPane.setContent(offresHbox);

                Parent root = null;
                try {
                    AuthService authService = new AuthService();
                    authService.logout();
                    root = FXMLLoader.load(getClass().getResource(Routes.LOGIN));
                } catch (IOException | BackingStoreException e) {
                    throw new RuntimeException(e);
                }

                Scene scene = new Scene(root);
                Stage stage = (Stage) paybtn.getScene().getWindow();
                stage.setScene(scene);
                stage.show();

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Vous n'êtes pas abonné à cette offre !");
            alert.showAndWait();
        }
    }

    @FXML
    void viewPay(ActionEvent event) {
        try {
            TestController.createStage("/com/tn/musego/add-customer.fxml", "Ajouter une carte de paiement");
            //paybtn.getScene().getWindow().setWidth(paybtn.getScene().getWidth() + 0.001);
            if (AddCustomerController.sign) {
                scrollPane.setContent(offresHbox);
                Parent root = null;
                try {
                    AuthService authService = new AuthService();
                    authService.logout();
                    root = FXMLLoader.load(getClass().getResource(Routes.LOGIN));
                } catch (IOException | BackingStoreException e) {
                    throw new RuntimeException(e);
                }

                Scene scene = new Scene(root);
                Stage stage = (Stage) paybtn.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        curentUserId = new UserService().getCurrentUserID();
        offresHbox = displayOffresHbox();
        scrollPane.setContent(offresHbox);
    }

}
