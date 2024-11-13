package com.tn.musego.controllers;

import com.tn.musego.entities.Atelier;
import com.tn.musego.entities.Attendance;
import com.tn.musego.entities.Formation;
import com.tn.musego.entities.User;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.AtelierService;
import com.tn.musego.services.impl.AttendanceService;
import com.tn.musego.services.impl.FormationService;
import com.tn.musego.services.impl.UserService;
import com.tn.musego.utils.EmailHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class FrontFormationController implements Initializable {

    VBox formationsVBox;
    FormationService formationService = new FormationService();
    Long userId;
    @FXML
    private ScrollPane formationScrollPane;
    @FXML
    private ComboBox<Atelier> comboFormation;
    @FXML
    private Text labelFormation;
    //    @FXML
//    private TableView<Formation> tableFormation;
    @FXML
    private Text textInfo;
    @FXML
    private ImageView imgFormation;
    private ObservableList<Formation> formations = FXCollections.observableArrayList();
    @FXML
    private ListView<Formation> listView;

    @FXML
    private void handleComboBoxAction(ActionEvent event) {
        Atelier selectedValue = comboFormation.getSelectionModel().getSelectedItem();
        String type = "";
//                tableFormation.getItems().removeAll(formations);
//        formations.addAll(new FormationService().getByAtelier(Atelier.DESSIN.label));
//                tableFormation.setItems(formations);
        imgFormation.setImage(new Image(selectedValue.getImage()));
        formationsVBox = afficherEvenementsVBox(selectedValue.getId());
        formationScrollPane.setContent(formationsVBox);
    }

    public VBox afficherEvenementsVBox(Long atelier) {


        List<Formation> formationList = formationService.getByAtelier(atelier);
        //evenementsList.sort(Comparator.comparing(Evenements::getDatefin).reversed());


        VBox vBox = new VBox();
        vBox.setSpacing(20); // espacement vertical entre les éléments

        //affichage des formations
        for (Formation f : formationList) {
            HBox hbox = new HBox();

            VBox detailsVBox = new VBox();

            Label nomLabel = new Label(f.getNom());
            nomLabel.setStyle("-fx-font-size: 25px; -fx-text-fill: black; -fx-font-weight: bold;");

            LocalDate currentDate = LocalDate.now();
            Date datedeb = f.getDateDebut();
            Timestamp timestampDebut = new Timestamp(datedeb.getTime());

            Date datefin = f.getDateFin();
            Timestamp timestampFin = new Timestamp(datefin.getTime());

            Label statusLabel = new Label();
            statusLabel.setText(currentDate.isBefore(timestampDebut.toLocalDateTime().toLocalDate()) ? "Formation Disponible" : "Inscription Terminée");
            Circle statusIndicator = new Circle(8);
            statusIndicator.setFill(currentDate.isBefore(timestampDebut.toLocalDateTime().toLocalDate()) ? Color.GREEN : Color.RED);
            VBox buttonsVBox = new VBox();
            buttonsVBox.setSpacing(8); // espace 8 pixels


            HBox statusHBox = new HBox();
            statusHBox.setSpacing(5);
            statusHBox.setAlignment(Pos.CENTER_LEFT);
            statusHBox.getChildren().addAll(statusIndicator, statusLabel);

            Label datedebutLabel = new Label("Date début: " + f.getDateDebut().toString());
            datedebutLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black;");

            Label datefinLabel = new Label("Date fin: " + f.getDateFin().toString());
            datefinLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black;");

            detailsVBox.getChildren().addAll(nomLabel, statusHBox, datedebutLabel, datefinLabel);
            AttendanceService p = new AttendanceService();


            long formationId = f.getId();
            boolean isUserRegistered = p.userRegistered(userId, formationId);


            if (currentDate.isBefore(timestampDebut.toLocalDateTime().toLocalDate())) {
                Button participBtn = new Button();
                participBtn.setStyle("-fx-background-color: #000000; -fx-text-fill: white; -fx-font-weight: bold;");
                buttonsVBox.getChildren().add(participBtn);
                participBtn.setText(isUserRegistered ? "Annuler" : "Participer");
                participBtn.setOnAction(event -> {
                    if (!isUserRegistered) {
                        ajouter_attendance(formationId);
                    } else {
                        supprimer_attendance(formationId);
                    }
                });

            }
            AnchorPane spacing = new AnchorPane();
            HBox.setHgrow(spacing, Priority.ALWAYS);
            hbox.getChildren().addAll(detailsVBox, spacing, buttonsVBox);
            hbox.setSpacing(20);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setPadding(new Insets(10));
            vBox.getChildren().add(hbox);

        }


        return vBox;
    }

    public void ajouter_attendance(long formationId) {
        AttendanceService attendanceService = new AttendanceService();

        ////////verification tee user  si existe ou non /////////
        if (attendanceService.userRegistered(userId, formationId)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Vous avez déjà participé à cette formation !");
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
                attendanceService.createEntity(new Attendance(userId, formationId));
                try {
                    Formation formation = new FormationService().getEntityByID(formationId);
                    User currentUser = new UserService().getCurrentLoggedinUser();
                    EmailHelper emailHelper = new EmailHelper();
                    emailHelper.confirmationFormation(currentUser.getEmail(), formation.getNom(), formation.getDateDebut(), formation.getDateFin());
                } catch (MyCustomException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    public void supprimer_attendance(long formationId) {
        AttendanceService attendanceService = new AttendanceService();

        if (attendanceService.userRegistered(userId, formationId)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setTitle(null);
            alert.setContentText("Etes vous certain de Vouloir annuler votre participation de la formation.");

            ButtonType confirmerButton = new ButtonType("Confirmer");
            ButtonType annulerButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(confirmerButton, annulerButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == confirmerButton) {
                attendanceService.supprimerAttendance(userId, formationId);
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

        comboFormation.getItems().addAll(new AtelierService().getAll());
        UserService userService = new UserService();
        userId = userService.getCurrentUserID();


        comboFormation.setConverter(new StringConverter<>() {
            @Override
            public String toString(Atelier object) {
                if (object != null) {
                    return object.getNom();
                } else return "";
            }

            @Override
            public Atelier fromString(String string) {
                return comboFormation.getSelectionModel().getSelectedItem();
            }
        });

        /*FormationService crud = new FormationService();
        ObservableList<Formation> listFormations = FXCollections.observableList(crud.getAll());
        comboFormation.setValue(listFormations.get(0));

        comboFormation.setConverter(new StringConverter<Formation>() {
            @Override
            public String toString(Formation object) {
                return object.getAtelier();
            }

            @Override
            public Formation fromString(String string) {
                return comboFormation.getSelectionModel().getSelectedItem();
            }
        });
        comboFormation.setItems(listFormations);*/
        //comboFormation.getItems().addAll(new FormationService().getAll());


    }
}



