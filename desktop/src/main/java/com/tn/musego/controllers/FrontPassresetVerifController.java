package com.tn.musego.controllers;

/**
 * @author Skander Ben Fredj
 * @created 26-Feb-23
 * @project musego
 */

import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.PassTokenService;
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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FrontPassresetVerifController implements Initializable {
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
    void verifCode(ActionEvent event) {
        if (validatorHelper.validateFields()) {
            PassTokenService passTokenService = new PassTokenService();
            try {
                passTokenService.tokenExist(codeInput.getText());
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Routes.PASS_RESET_NEW));
                Parent root = loader.load();

                FrontPassNewController controller2 = loader.getController();
                controller2.setData(email.getText(), codeInput.getText());

                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (MyCustomException e) {
                AlertHelper.showWarning(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validatorHelper.addValidator(new TextFieldValidator(codeInput));
    }
}
