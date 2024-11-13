package com.tn.musego.controllers;

import com.tn.musego.entities.Atelier;
import com.tn.musego.entities.Formation;
import com.tn.musego.entities.User;
import com.tn.musego.entities.enums.Niveau;
import com.tn.musego.entities.enums.RoleEnum;
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
import java.util.ResourceBundle;

public class AjoutFormationController implements Initializable {

    ObservableList<User> listFormateur = FXCollections.observableArrayList();

    ValidatorHelper validatorHelper = new ValidatorHelper();
    @FXML
    private Button btnadd;
    @FXML
    private Button btncls;
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
                FormationService formationService = new FormationService();
                formationService.createEntity(formation);

                AlertHelper.showSuccess("Formation ajoutée avec succès!");
                Stage stage = (Stage) btnadd.getScene().getWindow();
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
        stage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Niveau> obList1 = FXCollections.observableArrayList(Niveau.values());
        comboNiveau.getItems().addAll(obList1);

        ObservableList<Atelier> obList2 = FXCollections.observableArrayList(new AtelierService().getAll());
        comboAtelier.getItems().addAll(obList2);

        UserService userService = new UserService();
        listFormateur = userService.getUsersByRole(RoleEnum.ROLE_FORMATEUR);
        System.out.println(listFormateur.size());
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
        FunctionHelper.disablePastDates(dtdebut);
        FunctionHelper.disablePastDates(dtfin);
        validatorHelper.addValidator(new TextFieldValidator(tfnom));
        validatorHelper.addValidator(new ChoiceValidator(comboNiveau));
        validatorHelper.addValidator(new ChoiceValidator(choiceFormateur));
        validatorHelper.addValidator(new ChoiceValidator(comboAtelier));
        validatorHelper.addValidator(new DateAfterValidator(dtdebut, dtfin, "Date de fin doit être supérieur a la date de debut"));
    }
}
