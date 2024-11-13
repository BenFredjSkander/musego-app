package com.tn.musego.controllers;

import com.tn.musego.services.impl.PassTokenService;
import com.tn.musego.services.impl.UserService;
import com.tn.musego.utils.AlertHelper;
import com.tn.musego.utils.Routes;
import com.tn.musego.utils.ValidatorHelper;
import com.tn.musego.utils.validator.PasswordFieldMatchValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Skander Ben Fredj
 * @created 26-Feb-23
 * @project musego
 */


public class FrontPassNewController implements Initializable {

    ValidatorHelper validatorHelper = new ValidatorHelper();

    @FXML
    private Text email;

    @FXML
    private PasswordField pass1input;

    @FXML
    private PasswordField pass2input;

    @FXML
    private Button resetBtn;

    private String receivedToken;

    public void setData(String email, String token) {
        this.email.setText(email);
        this.receivedToken = token;
    }

    @FXML
    void resetPassword(ActionEvent event) {
        if (validatorHelper.validateFields()) {
            try {
                UserService userService = new UserService();
                PassTokenService passTokenService = new PassTokenService();

                userService.changePassword(email.getText(), pass1input.getText());
                passTokenService.destroyToken(receivedToken);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Mot de passe modifié avec succès");
                alert.setHeaderText(null);
                Optional<ButtonType> result = alert.showAndWait();
                ButtonType button = result.orElse(ButtonType.CANCEL);

                if (button == ButtonType.OK) {
                    Parent root = FXMLLoader.load(getClass().getResource(Routes.LOGIN));

                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                }
            } catch (Exception e) {
                AlertHelper.showError("Unknown error");

            }
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validatorHelper.addValidator(new PasswordFieldMatchValidator(pass1input, pass2input));
    }
}