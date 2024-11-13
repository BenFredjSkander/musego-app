package com.tn.musego.controllers;

import com.tn.musego.entities.User;
import com.tn.musego.services.impl.AuthService;
import com.tn.musego.services.impl.UserService;
import com.tn.musego.utils.Routes;
import com.tn.musego.utils.StringCnst;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author Skander Ben Fredj
 * @created 02-Mar-23
 * @project musego
 */

public class BackContainerController implements Initializable {

    @FXML
    public TabPane tabPane;
    @FXML
    public AnchorPane root;
    public int initTabIndex = 0;
    public Tab appointmentTab;
    public Tab usersTab;

    public Tab sponsorsTab;

    public Tab oeuvreTab;

    public Tab artisteTab;

    public Tab ateliersTab;
    public Tab formationTab;

    public Tab offreTab;

    public Tab abonnementTab;

    public Tab avisTab;

    User user = new User();
    @FXML
    private Text profilename;
    @FXML
    private MenuButton profilemenu;

    @FXML
    void openProfileMenu(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        double tabPaneWidth = 1280;
        double tabPaneHeight = 720;

        tabPane.setPrefSize(tabPaneWidth, tabPaneHeight);
        tabPane.setMinSize(tabPaneWidth, tabPaneHeight);
        tabPane.setMaxSize(tabPaneWidth, tabPaneHeight);

        tabPane.setTabMinWidth(50);
        tabPane.setTabMaxWidth(50);
        tabPane.setTabMinHeight(200);
        tabPane.setTabMaxHeight(200);

        try {
            Preferences userPreferences = Preferences.userRoot();
            Long userid = userPreferences.getLong(StringCnst.user_id_key, 0);
            UserService userService = new UserService();

            user = userService.getEntityByID(userid);
            profilemenu.setText(user.getUsername());
            MenuItem frontOffice = new MenuItem("Front office");
            frontOffice.setOnAction(event -> {
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource(Routes.FRONT_CONTAINER));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Scene scene = new Scene(root);
                Stage stage = (Stage) profilemenu.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            });
            frontOffice.setStyle("-fx-background-color: #ffffff");
            frontOffice.setStyle("-fx-border-color: #ffffff");
            MenuItem logoutItem = new MenuItem("Se déconnecter");
            logoutItem.setStyle("-fx-background-color: #ffffff");
            logoutItem.setStyle("-fx-border-color: #ffffff");
            logoutItem.setOnAction(event -> {
                Parent root = null;

                try {
                    AuthService authService = new AuthService();
                    authService.logout();
                    root = FXMLLoader.load(getClass().getResource(Routes.LOGIN));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (BackingStoreException e) {
                    throw new RuntimeException(e);
                }

                Scene scene = new Scene(root);
                Stage stage = (Stage) profilemenu.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            });
            profilemenu.getItems().addAll(frontOffice, logoutItem);

            FontIcon usersIcon = new FontIcon(Feather.USERS);

            usersTab = new Tab();
            usersTab.setGraphic(createTabHeader("Utilisateurs", usersIcon));

            usersTab.setOnSelectionChanged(event -> {
                if (usersTab.isSelected()) {
                    try {
                        Parent usersContent = FXMLLoader.load(getClass().getResource(Routes.BACK_USER_LIST));
                        usersTab.setContent(usersContent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });


            FontIcon eventsIcon = new FontIcon(Feather.CALENDAR);
//            eventsIcon.getStyleClass().add("-");

            appointmentTab = new Tab();
//            appointmentTab.setDisable(true);
            appointmentTab.setOnSelectionChanged(event -> {
                try {
                    if (appointmentTab.isSelected()) {
                        Parent eventContent = FXMLLoader.load(getClass().getResource(Routes.BACK_EVENTS));
                        appointmentTab.setContent(eventContent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
//            Parent appointmentContent = FXMLLoader.load(getClass().getResource(Routes.backEvents));

            appointmentTab.setGraphic(createTabHeader("Evenements", eventsIcon));

            FontIcon sponsorsIcon = new FontIcon(Feather.DOLLAR_SIGN);
//            eventsIcon.getStyleClass().add("-");

            sponsorsTab = new Tab();
//            appointmentTab.setDisable(true);
            sponsorsTab.setOnSelectionChanged(event -> {
                try {
                    if (sponsorsTab.isSelected()) {
                        Parent appointmentContent = FXMLLoader.load(getClass().getResource(Routes.BACK_SPONSOR));
                        sponsorsTab.setContent(appointmentContent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
//            Parent appointmentContent = FXMLLoader.load(getClass().getResource(Routes.backEvents));

            sponsorsTab.setGraphic(createTabHeader("Sponsors", sponsorsIcon));


            FontIcon oeuvreIcon = new FontIcon(Feather.IMAGE);

            oeuvreTab = new Tab();
            oeuvreTab.setOnSelectionChanged(event -> {
                try {
                    if (oeuvreTab.isSelected()) {
                        Parent oeuvresContent = FXMLLoader.load(getClass().getResource(Routes.BACK_OEUVRES));
                        oeuvreTab.setContent(oeuvresContent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            oeuvreTab.setGraphic(createTabHeader("Oeuvres", oeuvreIcon));


            FontIcon artisteIcon = new FontIcon(CarbonIcons.PAINT_BRUSH);

            artisteTab = new Tab();
            artisteTab.setOnSelectionChanged(event -> {
                try {
                    if (artisteTab.isSelected()) {
                        Parent artisteContent = FXMLLoader.load(getClass().getResource(Routes.BACK_ARTISTE));
                        artisteTab.setContent(artisteContent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            artisteTab.setGraphic(createTabHeader("Artistes", artisteIcon));


            FontIcon atelierIcon = new FontIcon(CarbonIcons.EXPAND_ALL);

            ateliersTab = new Tab();
            ateliersTab.setOnSelectionChanged(event -> {
                try {
                    if (ateliersTab.isSelected()) {
                        Parent ateliersContent = FXMLLoader.load(getClass().getResource(Routes.BACK_ATELIERS));
                        ateliersTab.setContent(ateliersContent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            ateliersTab.setGraphic(createTabHeader("Ateliers", atelierIcon));


            FontIcon formationIcon = new FontIcon(CarbonIcons.EXPAND_ALL);

            formationTab = new Tab();
            formationTab.setOnSelectionChanged(event -> {
                try {
                    if (formationTab.isSelected()) {
                        Parent ateliersContent = FXMLLoader.load(getClass().getResource(Routes.BACK_FORMATIONS));
                        formationTab.setContent(ateliersContent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            formationTab.setGraphic(createTabHeader("Formations", formationIcon));

            FontIcon offresIcon = new FontIcon(Feather.VOLUME_1);

            offreTab = new Tab();
            offreTab.setOnSelectionChanged(event -> {
                try {
                    if (offreTab.isSelected()) {
                        Parent ateliersContent = FXMLLoader.load(getClass().getResource(Routes.BACK_OFFRES));
                        offreTab.setContent(ateliersContent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });

            offreTab.setGraphic(createTabHeader("Offres", offresIcon));

            FontIcon abonnementsIcon = new FontIcon(Feather.CREDIT_CARD);

            abonnementTab = new Tab();
            abonnementTab.setOnSelectionChanged(event -> {
                try {
                    if (abonnementTab.isSelected()) {
                        Parent ateliersContent = FXMLLoader.load(getClass().getResource(Routes.BACK_ABONNEMENTS));
                        abonnementTab.setContent(ateliersContent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            abonnementTab.setGraphic(createTabHeader("Abonnements", abonnementsIcon));

            FontIcon avisIcon = new FontIcon(Feather.MESSAGE_CIRCLE);


            avisTab = new Tab();
            avisTab.setOnSelectionChanged(event -> {
                try {
                    if (avisTab.isSelected()) {
                        Parent ateliersContent = FXMLLoader.load(getClass().getResource(Routes.BACK_AVIS));
                        avisTab.setContent(ateliersContent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            avisTab.setGraphic(createTabHeader("Avis", avisIcon));

            tabPane.getTabs().addAll(usersTab, appointmentTab, sponsorsTab, oeuvreTab, artisteTab, ateliersTab, formationTab, offreTab, abonnementTab);
            tabPane.getSelectionModel().select(initTabIndex);

//            tabPane.getTabs().forEach(tab -> tab.setDisable(true));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private StackPane createTabHeader(String text, Node graphics) {
        Label label = new Label(" " + text, graphics);
        label.setContentDisplay(ContentDisplay.LEFT);

        label.getStyleClass().add("tab-pane-label");
        return new StackPane(new Group(label));
    }

    @FXML
    void goToFront(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(Routes.FRONT_CONTAINER));

        Scene scene = new Scene(root);
        Stage stage = (Stage) profilemenu.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
