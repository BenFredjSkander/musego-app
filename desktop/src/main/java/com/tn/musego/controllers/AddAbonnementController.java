package com.tn.musego.controllers;

import com.tn.musego.entities.Abonnement;
import com.tn.musego.entities.Offre;
import com.tn.musego.entities.User;
import com.tn.musego.entities.enums.TypeAbEnum;
import com.tn.musego.services.impl.AbonnementService;
import com.tn.musego.services.impl.OffreService;
import com.tn.musego.services.impl.UserService;
import com.tn.musego.utils.ValidatorHelper;
import com.tn.musego.utils.validator.ChoiceValidator;
import com.tn.musego.utils.validator.DateAfterValidator;
import com.tn.musego.utils.validator.TextFieldValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class AddAbonnementController {



    ObservableList<User> users = new UserService().getAll();
    ObservableList<Offre> offres = new OffreService().getAll();
    ValidatorHelper validatorHelper = new ValidatorHelper();
    @FXML
    Label lblerr;
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

    @FXML
    private Button btnadd;

    @FXML
    public void initialize() {

        combotype.getItems().removeAll(combotype.getItems());
        combotype.getItems().addAll(TypeAbEnum.values());
        combotype.setValue(TypeAbEnum.MENSUEL);
        comboffre.setValue(offres.get(0));
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
        comboffre.setItems(FXCollections.observableList(offres));
        combuser.getItems().removeAll(combuser.getItems());
        combuser.getItems().addAll(users);
        combuser.setValue(users.get(0));


        testInteger(tfprix);

        btnadd.disableProperty().bind(combotype.valueProperty().isNull().or(tfprix.textProperty().isEmpty().or(combuser.valueProperty().isNull().or(comboffre.valueProperty().isNull().or(dtdebut.valueProperty().isNull().or(dtfin.valueProperty().isNull()))))));

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

    @FXML
    public void addBut(ActionEvent event) {
        Abonnement abonnement = new Abonnement();


        Double prix = Double.parseDouble(tfprix.getText());
        java.sql.Date dateDebut = java.sql.Date.valueOf(dtdebut.getValue());
        java.sql.Date dateFin = java.sql.Date.valueOf(dtfin.getValue());
        TypeAbEnum type = combotype.getSelectionModel().getSelectedItem();
        Long idOffre = comboffre.getValue().getId();
        Long idUser = combuser.getValue().getId();

        abonnement.setType(type);
        abonnement.setDateDebut(dateDebut);
        abonnement.setDateFin(dateFin);
        abonnement.setPrix(prix);
        abonnement.setIdOffre(idOffre);
        abonnement.setIdUser(idUser);

        if (validatorHelper.validateFields() && prix <= 10000) {
            try {
                AbonnementService abonnementService = new AbonnementService();
                abonnementService.createEntity(abonnement);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ajouter Abonnement");
                alert.setHeaderText("Information");
                alert.setContentText("Abonnement est bien ajouté");
                alert.showAndWait();

                Stage stage = (Stage) btnadd.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ajouter Abonnement");
                alert.setHeaderText("Information");
                alert.setContentText("Abonnement n'est pas ajouté");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ajouter abonnement");
            alert.setHeaderText("Erreur");
            alert.setContentText("Le champ prix doit être inférieur à 10000");
            alert.showAndWait();
        }
    }

    @FXML
    public void annulerBut() {
        combotype.setValue(TypeAbEnum.MENSUEL);
        dtfin.setValue(null);
        dtdebut.setValue(null);
        tfprix.setText(null);
        comboffre.setValue(offres.get(0));
        combuser.setValue(users.get(0));
        lblerr.setText(null);
    }
}
