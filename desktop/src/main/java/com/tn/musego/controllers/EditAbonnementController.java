package com.tn.musego.controllers;

import com.tn.musego.entities.Abonnement;
import com.tn.musego.entities.Offre;
import com.tn.musego.entities.User;
import com.tn.musego.entities.enums.TypeAbEnum;
import com.tn.musego.entities.enums.TypeOffreEnum;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.AbonnementService;
import com.tn.musego.services.impl.OffreService;
import com.tn.musego.services.impl.UserService;
import com.tn.musego.utils.AlertHelper;
import com.tn.musego.utils.DBConnection;
import com.tn.musego.utils.DateHelper;
import com.tn.musego.utils.ValidatorHelper;
import com.tn.musego.utils.validator.ChoiceValidator;
import com.tn.musego.utils.validator.DateAfterValidator;
import com.tn.musego.utils.validator.TextAreaValidator;
import com.tn.musego.utils.validator.TextFieldValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.Instant;
import java.time.ZoneId;

public class EditAbonnementController {


    ValidatorHelper validatorHelper = new ValidatorHelper();
    @FXML
    Label lblerr;
    ObservableList<User> users = new UserService().getAll();
    ObservableList<Offre> offres = new OffreService().getAll();

    @FXML
    private Button btnedit;

    @FXML
    private ComboBox<TypeAbEnum> combotype;
    @FXML
    private DatePicker dtdebut;
    @FXML
    private DatePicker dtfin;
    @FXML
    private TextField tfprix;
    @FXML
    private ComboBox<User> combuser;
    @FXML
    private ComboBox<Offre> comboffre;


    public Long id;

    @FXML
    public void initialize() {
        combotype.getItems().removeAll(combotype.getItems());
        combotype.getItems().addAll(TypeAbEnum.values());
        comboffre.setConverter(new StringConverter<>() {
            @Override
            public String toString(Offre object) {
                if (object.getType() != null) {
                    return object.getType().toString()+": "+object.getPrix()+"€ "+object.getPromotion()+'%';
                } else return "";
            }

            @Override
            public Offre fromString(String string) {
                return comboffre.getSelectionModel().getSelectedItem();
            }
        });
        comboffre.setItems(FXCollections.observableList(offres));

        combuser.setConverter(new StringConverter<>() {
            @Override
            public String toString(User object) {
                if (object != null) {
                    return object.getUsername()+" : "+object.getRoles();
                } else return "";
            }

            @Override
            public User fromString(String string) {
                return combuser.getSelectionModel().getSelectedItem();
            }
        });
        combuser.getItems().addAll(users);
        testInteger(tfprix);
        btnedit.disableProperty().bind(combotype.valueProperty().isNull().or(tfprix.textProperty().isEmpty().or(combuser.valueProperty().isNull().or(comboffre.valueProperty().isNull().or(dtdebut.valueProperty().isNull().or(dtfin.valueProperty().isNull()))))));


        validatorHelper.addValidator(new ChoiceValidator(combotype));
        validatorHelper.addValidator(new TextFieldValidator(tfprix));
        validatorHelper.addValidator(new DateAfterValidator(dtdebut, dtfin));
    }

    public void testInteger(TextField txt) {
        txt.addEventFilter(KeyEvent.KEY_TYPED, str -> {
            String chr = str.getCharacter();
            String regex = "^[0-9]$";

            if (!chr.matches(regex)) {
                str.consume();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Champs invalid");
                alert.setHeaderText("Information");
                alert.setContentText("Vous ne pouvez ajouter que des chiffres !");
                alert.showAndWait();
                alert.close();
            }
        });
    }

    public void initFields(Abonnement abonnement) {
        combotype.setValue(abonnement.getType());
        dtdebut.setValue(Instant.ofEpochMilli(abonnement.getDateDebut().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        dtfin.setValue(Instant.ofEpochMilli(abonnement.getDateFin().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        tfprix.setText(abonnement.getPrix().toString());
        long idOffre = abonnement.getIdOffre();
        comboffre.setValue(offres.stream()
                .filter(offre -> offre.getId() == idOffre)
                .findFirst()
                .get()
        );
        long idUser = abonnement.getIdUser();
        combuser.setValue(users.stream()
                .filter(user -> user.getId() == idUser)
                .findFirst()
                .get()
        );
        lblerr.setText(null);

        id=abonnement.getId();

    }


    @FXML
    public void editBut(ActionEvent event) {
        Abonnement abonnement = new Abonnement();


        Double prix = Double.parseDouble(tfprix.getText());
        java.sql.Date dateDebut = java.sql.Date.valueOf(dtdebut.getValue());
        java.sql.Date dateFin = java.sql.Date.valueOf(dtfin.getValue());
        TypeAbEnum type = combotype.getSelectionModel().getSelectedItem();
        Long idOffre = comboffre.getSelectionModel().getSelectedItem().getId();
        Long idUser = combuser.getSelectionModel().getSelectedItem().getId();

        abonnement.setId(id);
        abonnement.setType(type);
        abonnement.setDateDebut(dateDebut);
        abonnement.setDateFin(dateFin);
        abonnement.setPrix(prix);
        abonnement.setIdOffre(idOffre);
        abonnement.setIdUser(idUser);

        if (validatorHelper.validateFields() && prix <= 10000) {
            try {
                AbonnementService abonnementService = new AbonnementService();
                abonnementService.updateEntity(abonnement);
                AlertHelper.showSuccess("Abonnement modifié avec succès");

                Stage stage = (Stage) btnedit.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                AlertHelper.showError("Erreur lors de la modification");
                System.out.println(e.getMessage());
            }

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Modifier abonnement");
            alert.setHeaderText("Erreur");
            alert.setContentText("Le champ prix doit être inférieur à 10000");
            alert.showAndWait();
        }
    }

    @FXML
    public void annulerBut() throws MyCustomException {
        combotype.setValue(TypeAbEnum.MENSUEL);
        dtfin.setValue(null);
        dtdebut.setValue(null);
        tfprix.setText(null);
        comboffre.setValue(offres.get(0));
        combuser.setValue(users.get(0));
        lblerr.setText(null);
    }
}
