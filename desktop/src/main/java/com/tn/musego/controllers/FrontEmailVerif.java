package com.tn.musego.controllers;

import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.AuthService;
import com.tn.musego.services.impl.UserService;
import com.tn.musego.utils.AlertHelper;
import com.tn.musego.utils.Routes;
import com.tn.musego.utils.ValidatorHelper;
import com.tn.musego.utils.validator.TextFieldValidator;
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
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Skander Ben Fredj
 * @created 26-Feb-23
 * @project musego
 */

public class FrontEmailVerif implements Initializable {

    ValidatorHelper validatorHelper = new ValidatorHelper();

    @FXML
    private TextField codeInput;

    @FXML
    private Text email;

    @FXML
    private Button verifBtn;

    public void setEmail(String email) {
        this.email.setText(email);
    }

    @FXML
    void verifCode(ActionEvent event) throws IOException {
        if (validatorHelper.validateFields()) {
            AuthService authService = new AuthService();
            UserService userService = new UserService();
            try {
                authService.checkEmailVerifCode(codeInput.getText());
                userService.enableUser(email.getText());
                authService.destroyEmailVerifToken(codeInput.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Votre compte est activé");
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
            } catch (MyCustomException e) {
                AlertHelper.showWarning(e.getMessage());
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validatorHelper.addValidator(new TextFieldValidator(codeInput));
    }
}
