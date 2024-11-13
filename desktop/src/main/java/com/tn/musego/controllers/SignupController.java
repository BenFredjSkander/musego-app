package com.tn.musego.controllers;

import com.tn.musego.entities.User;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.AuthService;
import com.tn.musego.utils.AlertHelper;
import com.tn.musego.utils.Routes;
import com.tn.musego.utils.ValidatorHelper;
import com.tn.musego.utils.validator.EmailFieldValidator;
import com.tn.musego.utils.validator.PasswordFieldMatchValidator;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    ValidatorHelper validatorHelper = new ValidatorHelper();
    @FXML
    private Button create_btn;

    @FXML
    private TextField email_input;

    @FXML
    private Button login_btn;


    @FXML
    private PasswordField pass2_input;

    @FXML
    private PasswordField pass_input;

    @FXML
    private TextField username_input;

    @FXML
    void createAccount(ActionEvent event) {

        if (validatorHelper.validateFields()) {
            System.out.println("field valid");
        } else System.out.println("invalid");

        if (validatorHelper.validateFields()) {
            AuthService authService = new AuthService();
            try {
                User user = new User();
                user.setEmail(email_input.getText());
                user.setUsername(username_input.getText());
                user.setPassword(pass_input.getText());
                authService.signup(user);

                FXMLLoader loader = new FXMLLoader(getClass().getResource(Routes.EMAIL_VERIF_SIGNUP));
                Parent root = loader.load();

                FrontEmailVerif controller2 = loader.getController();
                controller2.setEmail(email_input.getText());

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (MyCustomException e) {
                AlertHelper.showWarning(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @FXML
    void goToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(Routes.LOGIN));

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        validatorHelper.addValidator(new TextFieldValidator(username_input));
        validatorHelper.addValidator(new EmailFieldValidator(email_input));
        validatorHelper.addValidator(new PasswordFieldMatchValidator(pass_input, pass2_input));
    }
}
