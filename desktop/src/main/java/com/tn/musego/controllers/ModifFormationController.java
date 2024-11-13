package com.tn.musego.controllers;

import com.tn.musego.entities.Atelier;
import com.tn.musego.entities.Formation;
import com.tn.musego.entities.User;
import com.tn.musego.entities.enums.Niveau;
import com.tn.musego.entities.enums.RoleEnum;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.AtelierService;
import com.tn.musego.services.impl.FormationService;
import com.tn.musego.services.impl.UserService;
import com.tn.musego.utils.AlertHelper;
import com.tn.musego.utils.DateHelper;
import com.tn.musego.utils.FunctionHelper;
import com.tn.musego.utils.ValidatorHelper;
import com.tn.musego.utils.validator.ChoiceValidator;
import com.tn.musego.utils.validator.DateAfterValidator;
import com.tn.musego.utils.validator.TextFieldValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class ModifFormationController implements Initializable {

    public static Formation formation;

    ValidatorHelper validatorHelper = new ValidatorHelper();

    ObservableList<User> listFormateur = FXCollections.observableArrayList();

    ObservableList<Atelier> ateliers = new AtelierService().getAll();
    @FXML
    private ComboBox<Atelier> comboAtelier;
    @FXML
    private ComboBox<Niveau> comboNiveau;
    @FXML
    private DatePicker dtdebut;
    @FXML
    private DatePicker dtfin;
    @FXML
    private Label lblerr;
    @FXML
    private ComboBox<User> choiceFormateur;
    @FXML
    private TextField tfnom;
    @FXML
    private Button btncls;

    public void initFormation() throws MyCustomException {
        tfnom.setText(formation.getNom());
        comboAtelier.setConverter(new StringConverter<>() {
            @Override
            public String toString(Atelier object) {
                if (object != null) {
                    return object.getNom();
                } else return "";
            }

            @Override
            public Atelier fromString(String string) {
                return comboAtelier.getSelectionModel().getSelectedItem();
            }
        });
        comboNiveau.setValue(formation.getNiveau());
        dtdebut.setValue(Instant.ofEpochMilli(formation.getDateDebut().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        dtfin.setValue(Instant.ofEpochMilli(formation.getDateFin().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
    }


    @FXML
    void addBut(ActionEvent event) {
        try {

            if (validatorHelper.validateFields()) {
                Formation formation = new Formation(
                        tfnom.getText(),
                        DateHelper.dateFromString(dtdebut.getValue().toString()),
                        DateHelper.dateFromString(dtfin.getValue().toString()),
                        comboNiveau.getSelectionModel().getSelectedItem(),
                        choiceFormateur.getSelectionModel().getSelectedItem().getId(),
                        comboAtelier.getSelectionModel().getSelectedItem().getId()
                );
                formation.setId(ModifFormationController.formation.getId());
                FormationService formationService = new FormationService();
                formationService.updateEntity(formation);
                AlertHelper.showSuccess("Formation modifiée avec succès!");
                Stage stage = (Stage) tfnom.getScene().getWindow();
                stage.close();
            }

        } catch (Exception e) {
            AlertHelper.showError("Une erreur coté backend");
            e.printStackTrace();
        }


    }

    @FXML
    void annulerBut(ActionEvent event) {
        Stage stage = (Stage) btncls.getScene().getWindow();
        // do what you have to do
        stage.close();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initFormation();
        } catch (MyCustomException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Niveau> obList1 = FXCollections.observableArrayList(Niveau.values());
        comboNiveau.getItems().addAll(obList1);

        ObservableList<Atelier> obList2 = FXCollections.observableArrayList(new AtelierService().getAll());
        comboAtelier.getItems().addAll(obList2);


        UserService userService = new UserService();
        listFormateur = userService.getUsersByRole(RoleEnum.ROLE_FORMATEUR);
        choiceFormateur.setItems(listFormateur);
        choiceFormateur.setConverter(new StringConverter<>() {
            @Override
            public String toString(User object) {
                if (object != null) {
                    return object.getUsername();
                } else return "";
            }

            @Override
            public User fromString(String string) {
                return choiceFormateur.getSelectionModel().getSelectedItem();
            }
        });
        choiceFormateur.setValue(listFormateur.stream().filter(user -> user.getId() == formation.getIdFormateur()).findFirst().get());
        comboAtelier.setValue(ateliers.stream().filter(atelier -> atelier.getId() == formation.getIdAtelier()).findFirst().get());
        FunctionHelper.disablePastDates(dtdebut);
        FunctionHelper.disablePastDates(dtfin);
        validatorHelper.addValidator(new TextFieldValidator(tfnom));
        validatorHelper.addValidator(new ChoiceValidator(comboNiveau));
        validatorHelper.addValidator(new ChoiceValidator(choiceFormateur));
        validatorHelper.addValidator(new ChoiceValidator(comboAtelier));
        validatorHelper.addValidator(new DateAfterValidator(dtdebut, dtfin, "Date de fin doit être supérieur a la date de debut"));

    }
}
