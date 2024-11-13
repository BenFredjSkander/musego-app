package com.tn.musego.controllers;

import com.tn.musego.entities.User;
import com.tn.musego.entities.enums.RoleEnum;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.UserService;
import com.tn.musego.utils.AlertHelper;
import com.tn.musego.utils.DateHelper;
import com.tn.musego.utils.FunctionHelper;
import com.tn.musego.utils.ValidatorHelper;
import com.tn.musego.utils.validator.DatepickerValidator;
import com.tn.musego.utils.validator.EmailFieldValidator;
import com.tn.musego.utils.validator.NumberFieldValidator;
import com.tn.musego.utils.validator.TextFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

public class BackUpdateUserController implements Initializable {
    ValidatorHelper userValidator = new ValidatorHelper();
    ValidatorHelper adminValidator = new ValidatorHelper();
    ValidatorHelper formateurValidator = new ValidatorHelper();
    private User user;


    @FXML
    private DatePicker birthday_input;

    @FXML
    private TextField email_input;

    @FXML
    private TextField phone_input;

    @FXML
    private CheckBox role1check;

    @FXML
    private CheckBox role2check;

    @FXML
    private CheckBox role3check;


    @FXML
    private TextField speciality_input;

    @FXML
    private Button update_btn;

    @FXML
    private TextField username_input;

    @FXML
    private DatePicker hirepicker;


    @FXML
    void updateUser(ActionEvent event) {
        if (role2check.isSelected() && role3check.isSelected()) {
            AlertHelper.showWarning("Veuillez sélectionner un seul role");
        } else {

            try {
                if (role2check.isSelected()) {
                    if (formateurValidator.validateFields()) {
                        user.setPhoneNumber(phone_input.getText());
                        user.setSpeciality(speciality_input.getText());
                        user.setBirthDate(DateHelper.dateFromString(birthday_input.getValue().toString()));
                        user.setHiringDate(DateHelper.dateFromString(hirepicker.getValue().toString()));
                        user.setUsername(username_input.getText());
                        user.setEmail(email_input.getText());
                        user.setRoles(new HashSet<>(List.of(RoleEnum.ROLE_FORMATEUR)));
                        System.out.println("formateur" + user);
                        UserService userService = new UserService();
                        userService.updateEntity(user);
                        AlertHelper.showSuccess("Utilisateur modifié avec succès!");
                        Stage stage = (Stage) birthday_input.getScene().getWindow();
                        stage.close();
                    }
                } else if (role3check.isSelected()) {
                    if (adminValidator.validateFields()) {
                        user.setPhoneNumber(phone_input.getText());
                        user.setUsername(username_input.getText());
                        user.setEmail(email_input.getText());
                        user.setRoles(new HashSet<>(List.of(RoleEnum.ROLE_ADMIN)));
                        System.out.println("admin" + user);
                        UserService userService = new UserService();
                        userService.updateEntity(user);
                        AlertHelper.showSuccess("Utilisateur modifié avec succès!");
                        Stage stage = (Stage) birthday_input.getScene().getWindow();
                        stage.close();
                    }
                } else {
                    if (userValidator.validateFields()) {
                        user.setUsername(username_input.getText());
                        user.setEmail(email_input.getText());
                        user.setRoles(new HashSet<>());
                        System.out.println("user" + user);
                        UserService userService = new UserService();
                        userService.updateEntity(user);
                        AlertHelper.showSuccess("Utilisateur modifié avec succès!");
                        Stage stage = (Stage) birthday_input.getScene().getWindow();
                        stage.close();
                    }
                }

            } catch (MyCustomException e) {
                AlertHelper.showError(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void initUser(User user) {
        role1check.setSelected(user.getRoles().contains(RoleEnum.ROLE_USER));
        role2check.setSelected(user.getRoles().contains(RoleEnum.ROLE_FORMATEUR));
        role3check.setSelected(user.getRoles().contains(RoleEnum.ROLE_ADMIN));
        this.user = user;
        username_input.setText(user.getUsername());
        email_input.setText(user.getEmail());
        if (user.getRoles().contains(RoleEnum.ROLE_FORMATEUR)) {
            speciality_input.setText(user.getSpeciality());
            phone_input.setText(user.getPhoneNumber());
            birthday_input.setValue(DateHelper.dateToDatepicker(user.getBirthDate()));
            hirepicker.setValue(DateHelper.dateToDatepicker(user.getHiringDate()));
        } else if (user.getRoles().contains(RoleEnum.ROLE_ADMIN)) {
            phone_input.setText(user.getPhoneNumber());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        role1check.setDisable(true);
        FunctionHelper.disableFutureDates(birthday_input);
        userValidator.addValidator(new TextFieldValidator(username_input));
        userValidator.addValidator(new EmailFieldValidator(email_input));

        adminValidator.addValidator(new TextFieldValidator(username_input));
        adminValidator.addValidator(new EmailFieldValidator(email_input));
        adminValidator.addValidator(new NumberFieldValidator(phone_input, 8));

        formateurValidator.addValidator(new TextFieldValidator(username_input));
        formateurValidator.addValidator(new EmailFieldValidator(email_input));
        formateurValidator.addValidator(new DatepickerValidator(birthday_input));
        formateurValidator.addValidator(new NumberFieldValidator(phone_input, 8));
        formateurValidator.addValidator(new TextFieldValidator(speciality_input));
        formateurValidator.addValidator(new DatepickerValidator(hirepicker));
    }
}
