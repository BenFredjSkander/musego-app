package com.tn.musego.controllers;

import animatefx.animation.BounceIn;
import com.jfoenix.controls.JFXButton;
import com.tn.musego.entities.User;
import com.tn.musego.entities.enums.RoleEnum;
import com.tn.musego.services.impl.UserService;
import com.tn.musego.utils.FunctionHelper;
import com.tn.musego.utils.Routes;
import com.tn.musego.utils.ValidatorHelper;
import com.tn.musego.utils.validator.TextFieldValidator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class BackUserListController implements Initializable {

    ValidatorHelper validatorHelper = new ValidatorHelper();

    @FXML
    private TableColumn<User, String> col_email;

    @FXML
    private TableColumn<User, String> col_enabled;


    @FXML
    private TableColumn<User, String> col_role;

    @FXML
    private TableColumn<User, String> col_username;

    @FXML
    private TableColumn<User, String> col_creation_date;


    @FXML
    private TableView<User> tableview;


    @FXML
    private JFXButton edit_btn;

    @FXML
    private Button recherche_btn;

    @FXML
    private TextField search_input;

    @FXML
    private JFXButton lock_btn;

    private ObservableList<User> dataList = FXCollections.observableArrayList();


    @FXML
    void editUserBtn(ActionEvent event) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Routes.BACK_USER_UPDATE));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        BackUpdateUserController controller2 = fxmlLoader.getController();
        controller2.initUser(tableview.getSelectionModel().getSelectedItem());
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene sc = new Scene(root);
        sc.getStylesheets().add(FunctionHelper.class.getResource("/styles/styles.css")
                .toExternalForm());
        stage.setTitle("Mise a jour utilisateur");
        stage.setResizable(false);
        stage.getIcons().add(new Image(FunctionHelper.class.getResource("/images/icon.png").openStream()));
        stage.setScene(sc);
        stage.showAndWait();
        tableview.getItems().clear();
        fillTable();
    }

    @FXML
    void lockUserBtn(ActionEvent event) {
        User selectedUser = tableview.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(selectedUser.isLocked() ? "Débloquer l'utilisateur" : "Bloquer l'utilisateur");
        alert.setHeaderText(null);
        alert.setContentText(selectedUser.isLocked() ? "Voulez vous débloquer l'utilisateur " + tableview.getSelectionModel().getSelectedItem().getUsername() + " ?" : "Voulez vous bloquer l'utilisateur " + tableview.getSelectionModel().getSelectedItem().getUsername() + " ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            UserService userService = new UserService();
            userService.lockUser(selectedUser.getId());
            tableview.getItems().clear();
            fillTable();
        } else {
            alert.close();
        }
        tableview.getItems().clear();
        fillTable();

    }

    @FXML
    void findUser(ActionEvent event) {
        if (validatorHelper.validateFields()) {
            ObservableList<User> dummy = dataList;
            ObservableList<User> foundUsers =
                    dummy.stream().filter(
                                    user -> user.getEmail().contains(search_input.getText())
                                            || user.getUsername().contains(search_input.getText()
                                    ))
                            .collect(Collectors.toCollection(FXCollections::observableArrayList));
            tableview.getItems().clear();
            tableview.setItems(foundUsers);
        }

    }

    void fillTable() {
        UserService userService = new UserService();
        dataList = userService.getAll();
        tableview.setItems(dataList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new BounceIn(tableview).play();

        validatorHelper.addValidator(new TextFieldValidator(search_input, "Requête invalide"));
//        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_enabled.setCellValueFactory(new PropertyValueFactory<>("verified"));
        col_role.setCellValueFactory(param -> {
            SimpleStringProperty property = new SimpleStringProperty();
            if (param.getValue().getRoles().contains(RoleEnum.ROLE_ADMIN)){
                property.setValue("ADMIN");
            } else if (param.getValue().getRoles().contains(RoleEnum.ROLE_FORMATEUR)) {
                property.setValue("Formateur");
            }else property.setValue("Utilisateur");
            return property;
        });
        col_creation_date.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        tableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                lock_btn.setDisable(false);
                edit_btn.setDisable(false);
                if (newSelection.isLocked()) {
                    lock_btn.setStyle("-fx-background-color: #a4ff6d");
                    lock_btn.setText("Débloquer");
                } else {
                    lock_btn.setStyle("-fx-background-color: #ff0000");
                    lock_btn.setText("Bloquer");
                }
            } else {
                lock_btn.setDisable(true);
                edit_btn.setDisable(true);
            }
        });

        fillTable();
    }
}
