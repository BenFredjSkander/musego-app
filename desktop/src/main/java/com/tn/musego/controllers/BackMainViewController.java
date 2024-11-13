package com.tn.musego.controllers;

/**
 * @author Skander Ben Fredj
 * @created 27-Feb-23
 * @project musego
 */

import com.tn.musego.entities.User;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.UserService;
import com.tn.musego.utils.Routes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class BackMainViewController implements Initializable {

    User user = new User();

    String view;

    @FXML
    private StackPane content;

    @FXML
    private Text profilename;


    @FXML
    private MenuButton profilemenu;

    @FXML
    void goToArts(ActionEvent event) {

    }

    @FXML
    void goToEvents(ActionEvent event) {
        changeContent(Routes.BACK_EVENTS);
    }

    @FXML
    void goToFeedback(ActionEvent event) {

    }

    @FXML
    void goToHome(ActionEvent event) {

    }

    @FXML
    void goToSubscriptions(ActionEvent event) {

    }

    @FXML
    void goToUsers(ActionEvent event) {
        changeContent(Routes.BACK_USER_LIST);
    }

    @FXML
    void goToWorkshops(ActionEvent event) {

    }

    @FXML
    void openProfileMenu(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Preferences userPreferences = Preferences.userRoot();
        Long userid = userPreferences.getLong("user_id", 0);
        UserService userService = new UserService();
        try {
            user = userService.getEntityByID(userid);
            profilemenu.setText(user.getUsername());
//            if (view == null) {
//
//            }
        } catch (MyCustomException e) {
            throw new RuntimeException(e);
        }

    }

    public void changeContent(String view) {

        try {
            Parent fxml = FXMLLoader.load(getClass().getResource(view));
            content.getChildren().clear();
            content.getChildren().setAll(fxml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setContent(String view, Object obj) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
        Parent root = loader.load();

//        Object controller2 = loader.getController();
//        controller2.
    }

}
