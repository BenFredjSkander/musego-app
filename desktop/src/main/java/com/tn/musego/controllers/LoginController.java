package com.tn.musego.controllers;

import com.jfoenix.controls.JFXButton;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.AuthService;
import com.tn.musego.utils.AlertHelper;
import com.tn.musego.utils.Routes;
import com.tn.musego.utils.ValidatorHelper;
import com.tn.musego.utils.validator.EmailFieldValidator;
import com.tn.musego.utils.validator.TextFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    ValidatorHelper validatorHelper = new ValidatorHelper();

    @FXML
    private TextField email_input;

    @FXML
    private Button login_btn;

    @FXML
    private PasswordField pass_input;

    @FXML
    private Button signup_btn;


    @FXML
    private JFXButton passresetbtn;


    @FXML
    void goToSignup(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(Routes.SIGNUP));

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void login(ActionEvent event) {
        if (validatorHelper.validateFields()) {
            AuthService authService = new AuthService();
            try {
                authService.login(email_input.getText(), pass_input.getText());
                Parent root = FXMLLoader.load(getClass().getResource(Routes.FRONT_CONTAINER));

                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (MyCustomException e) {
                AlertHelper.showWarning(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                AlertHelper.showWarning(e.getMessage());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        validatorHelper.addValidator(new EmailFieldValidator(email_input));
        validatorHelper.addValidator(new TextFieldValidator(pass_input));

    }

    @FXML
    void goTopassReset(ActionEvent actionEvent) throws IOException {
        if (!email_input.getText().isEmpty() && EmailValidator.getInstance().isValid(email_input.getText())) {
            AuthService authService = new AuthService();
            try {
                authService.requestResetPassword(email_input.getText());
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Routes.PASS_RESET_VERIF));
                Parent root = loader.load();

                FrontPassresetVerifController controller2 = loader.getController();
                controller2.setEmail(email_input.getText());

                Stage stage = (Stage) email_input.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (MyCustomException e) {
                AlertHelper.showWarning(e.getMessage());
            }
        } else {
            AlertHelper.showWarning("Format d'email incorrect");
        }
    }
}
