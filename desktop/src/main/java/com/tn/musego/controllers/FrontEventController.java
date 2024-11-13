package com.tn.musego.controllers;

import animatefx.animation.BounceIn;
import com.tn.musego.entities.Evenements;
import com.tn.musego.services.impl.EvenementService;
import com.tn.musego.services.impl.ParticipationService;
import com.tn.musego.services.impl.UserService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;


public class FrontEventController implements Initializable {
    VBox evenementsVBox;
    EvenementService pcd = new EvenementService();
    @FXML
    private ListView<Evenements> listView;
    @FXML
    private HBox navbar_abonnement;
    @FXML
    private HBox navbar_ateliers;
    @FXML
    private VBox vbox_Event;
    @FXML
    private HBox navbar_avis;
    @FXML
    private HBox navbar_compte;
    @FXML
    private HBox navbar_event;
    @FXML
    private HBox navbar_home;
    @FXML
    private HBox Evenements_id;
    @FXML
    private List<Integer> participations;
    @FXML
    private TextArea voir_details;
    @FXML
    private TextField searchbar;
    @FXML
    private Button chercher_button;
    @FXML
    private ListView<String> liste_particip;
    /////////////partie cherhcer////////
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button voir_particip;
    //////////////////supprimer my particip///////////
    @FXML
    private Button supprimer_particip;

    public VBox afficherEvenementsVBox(String what) {


        List<Evenements> evenementsList = pcd.afficherEvenements(what);
        evenementsList.sort(Comparator.comparing(Evenements::getDateFin).reversed());


        VBox vBox = new VBox();
        vBox.setSpacing(20); // espacement vertical entre les éléments


        int userId = new UserService().getCurrentUserID().intValue();


        //affichage pour les evenements/////
        for (Evenements evenement : evenementsList) {
            HBox hbox = new HBox();
            ImageView imageView = new ImageView(new Image(evenement.getPoster()));
            imageView.setFitWidth(150);
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(150);
            new BounceIn(imageView).play();

            VBox detailsVBox = new VBox();

            Label nomLabel = new Label(evenement.getNom());
            nomLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-weight: bold;");

            LocalDate currentDate = LocalDate.now();
            Date datedeb = evenement.getDateDebut();
            Timestamp timestampDebut = new Timestamp(datedeb.getTime());

            Date datefin = evenement.getDateFin();
            Timestamp timestampFin = new Timestamp(datefin.getTime());

            Label statusLabel = new Label();
            Circle statusIndicator = new Circle(8);
            VBox buttonsVBox = new VBox();
            buttonsVBox.setSpacing(10); // Ajouter un espacement de 10 pixels entre les boutons et le reste des éléments

            Button detailsButton = new Button("Voir détails");
            detailsButton.setStyle("-fx-background-color: #3f51b5; -fx-text-fill: white; -fx-font-weight: bold;");
            detailsButton.setPadding(new Insets(0, 5, 0, 0)); // Ajout d'un padding pour séparer les boutons

            buttonsVBox.getChildren().add(detailsButton);


            HBox statusHBox = new HBox();
            statusHBox.setSpacing(5);
            statusHBox.setAlignment(Pos.CENTER_LEFT);
            statusHBox.getChildren().addAll(statusIndicator, statusLabel);

            Label datedebutLabel = new Label("Date début: " + evenement.getDateDebut().toString());
            datedebutLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

            Label datefinLabel = new Label("Date fin: " + evenement.getDateFin().toString());
            datefinLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

            detailsVBox.getChildren().addAll(nomLabel, statusHBox, datedebutLabel, datefinLabel);
            ParticipationService p = new ParticipationService();


            int evenementId = (int) evenement.getId();
            boolean isUserRegistered = p.isUserAlreadyRegistered(userId, evenementId);


            if (userId != 0)
            ///////encore evenement n'a pas commnecé///////
            {
                if (currentDate.isBefore(timestampDebut.toLocalDateTime().toLocalDate())) {

                    statusLabel.setText("Non commencé");
                    statusIndicator.setFill(Color.YELLOW);


                    ///////////partie tee button participer/////////


                    if (!isUserRegistered) {


                        Button participerButton = new Button("Participer");
                        participerButton.setStyle("-fx-background-color: #ff9800; -fx-text-fill: white; -fx-font-weight: bold;");
                        buttonsVBox.getChildren().add(participerButton);

                        participerButton.setOnAction(event -> {
                            ajouter_participation(userId, evenementId);
                        });

                    }


                    if (isUserRegistered) {
                        Button supprimerButton = new Button("Annuler");  //////// ken user mawjoud bech naffichilou button supprimer//////
                        supprimerButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
                        buttonsVBox.getChildren().add(supprimerButton);

                        supprimerButton.setOnAction(event -> {
                            supprimer_participation(userId, evenementId);
                        });


                        buttonsVBox.setAlignment(Pos.CENTER_RIGHT);
                        buttonsVBox.setPadding(new Insets(10));
                    }


                } else if (currentDate.isAfter(timestampFin.toLocalDateTime().toLocalDate())) {
                    statusLabel.setText("Terminé");
                    statusIndicator.setFill(Color.GRAY);
                } else {
                    statusLabel.setText("En cours");
                    statusIndicator.setFill(Color.GREEN);
                }


                ///////////////button voir details///////
                detailsButton.setOnAction(event -> {
                    voir_details.setText(evenement.getDescription());

                });


            }

            hbox.getChildren().addAll(imageView, detailsVBox, buttonsVBox);
            hbox.setSpacing(20);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setPadding(new Insets(10));

            vBox.getChildren().add(hbox);

        }


        return vBox;
    }

    public void ajouter_participation(int userId, int evenementId) {
        ParticipationService pcp = new ParticipationService();

        ////////verification tee user  si existe ou non /////////
        if (pcp.isUserAlreadyRegistered(userId, evenementId)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Vous avez déjà participé à cet événement !");
            alert.showAndWait();


        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirmer votre participation.");

            ButtonType confirmerButton = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);

            ButtonType annulerButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(confirmerButton, annulerButton);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == confirmerButton) {
                pcp.ajouterParticipation(userId, evenementId);//                 // Appeler la fonction ajouterParticipation() ici


            }
        }
    }

    public void supprimer_participation(int userId, int evenementId) {
        ParticipationService pcp = new ParticipationService();

        if (pcp.isUserAlreadyRegistered(userId, evenementId)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirmer l'annulation' de votre participation.");

            ButtonType confirmerButton = new ButtonType("Confirmer");
            ButtonType annulerButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(confirmerButton, annulerButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == confirmerButton) {
                pcp.supprimerParticipation(userId, evenementId);
                refraiche();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Vous n'êtes pas inscrit à cet événement !");
            alert.showAndWait();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        searchbar.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String textSearch = searchbar.getText();
                System.out.println("haw chneya ktebt +++++++++++++++++++++++" + textSearch);
                evenementsVBox = afficherEvenementsVBox(textSearch.equals("") ? "all" : textSearch);
                scrollPane.setContent(evenementsVBox);
            }
        });


        // refraiche();//
        evenementsVBox = afficherEvenementsVBox("all");
        scrollPane.setContent(evenementsVBox);


    }


    @FXML
    void refraiche() {
        afficherEvenementsVBox("all");

    }


}