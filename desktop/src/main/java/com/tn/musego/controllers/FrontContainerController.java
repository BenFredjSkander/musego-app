package com.tn.musego.controllers;

import com.tn.musego.entities.Abonnement;
import com.tn.musego.entities.Offre;
import com.tn.musego.entities.User;
import com.tn.musego.entities.enums.RoleEnum;
import com.tn.musego.services.impl.AbonnementService;
import com.tn.musego.services.impl.AuthService;
import com.tn.musego.services.impl.OffreService;
import com.tn.musego.services.impl.UserService;
import com.tn.musego.utils.FunctionHelper;
import com.tn.musego.utils.Routes;
import com.tn.musego.utils.StringCnst;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author Skander Ben Fredj
 * @created 06-Mar-23
 * @project musego
 */

public class FrontContainerController implements Initializable {
    @FXML
    public TabPane tabPane;
    @FXML
    public AnchorPane root;

    public Tab homeTab;


    public Tab oeuvreTab;

    public Tab eventsTab;


    public Tab ateliersTab;


    public Tab abonnementTab;

    public Tab avisTab;

    User user = new User();

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
            AbonnementService abonnementService = new AbonnementService();
            boolean activeSubscription = abonnementService.haveActiveSubscription(userid);
            Abonnement abonnement=abonnementService.getEntityByUserID(userid);
            profilemenu.setText(user.getUsername());
            MenuItem backOfficeMenuItem = new MenuItem("Back office");
            backOfficeMenuItem.setOnAction(event -> {
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource(Routes.BACK_CONTAINER));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Scene scene = new Scene(root);
                Stage stage = (Stage) profilemenu.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            });
            backOfficeMenuItem.setStyle(null);

            MenuItem monAbonnement = new MenuItem("Mon abonnement");
            monAbonnement.setOnAction(event -> {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Routes.FRONT_ABONNEMENT));
                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                AbonnementFrontController abonnementFrontController = fxmlLoader.getController();
                abonnementFrontController.initFields(abonnement,user);
                stage.initModality(Modality.APPLICATION_MODAL);
                Scene sc = new Scene(root);
                stage.setTitle("Mon abonnement");
                stage.setResizable(false);
                try {
                    stage.getIcons().add(new Image(FunctionHelper.class.getResource("/images/icon.png").openStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.setScene(sc);
                stage.show();
            });

            MenuItem logoutItem = new MenuItem("Se déconnecter");
            logoutItem.setStyle(null);
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
            if (user.getRoles().contains(RoleEnum.ROLE_ADMIN)) {
                profilemenu.getItems().add(backOfficeMenuItem);
            }
            if(activeSubscription) profilemenu.getItems().addAll(monAbonnement,logoutItem);
            else profilemenu.getItems().add(logoutItem);
//            if (view == null) {
//
//            }

            FontIcon homeIcon = new FontIcon(Feather.HOME);

            homeTab = new Tab();
            homeTab.setOnSelectionChanged(event -> {
                try {
                    if (homeTab.isSelected()) {
                        Parent oeuvresContent = FXMLLoader.load(getClass().getResource(Routes.FRONT_HOME));
                        homeTab.setContent(oeuvresContent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            homeTab.setGraphic(createTabHeader("Accueil", homeIcon));


            FontIcon oeuvreIcon = new FontIcon(Feather.IMAGE);

            oeuvreTab = new Tab();
            oeuvreTab.setOnSelectionChanged(event -> {
                try {
                    if (oeuvreTab.isSelected()) {
                        Parent oeuvresContent = FXMLLoader.load(getClass().getResource(Routes.FRONT_OEUVRE));
                        oeuvreTab.setContent(oeuvresContent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            oeuvreTab.setGraphic(createTabHeader("Oeuvres", oeuvreIcon));


            FontIcon eventsIcon = new FontIcon(Feather.CALENDAR);

            eventsTab = new Tab();
            eventsTab.setOnSelectionChanged(event -> {
                try {
                    if (eventsTab.isSelected()) {
                        Parent eventContent = FXMLLoader.load(getClass().getResource(Routes.FRONT_EVENTS));
                        eventsTab.setContent(eventContent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });

            eventsTab.setGraphic(createTabHeader("Evenements", eventsIcon));


            FontIcon ateliersIcon = new FontIcon(CarbonIcons.EXPAND_ALL);

            ateliersTab = new Tab();
            ateliersTab.setOnSelectionChanged(event -> {
                try {
                    if (ateliersTab.isSelected()) {
                        Parent eventContent = FXMLLoader.load(getClass().getResource(Routes.FRONT_FORMATION));
                        ateliersTab.setContent(eventContent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });

            ateliersTab.setGraphic(createTabHeader("Ateliers", ateliersIcon));


            FontIcon avisIcon = new FontIcon(Feather.MESSAGE_CIRCLE);

            avisTab = new Tab();
            avisTab.setOnSelectionChanged(event -> {
                try {
                    if (avisTab.isSelected()) {
                        Parent eventContent = FXMLLoader.load(getClass().getResource(Routes.FRONT_AVIS));
                        avisTab.setContent(eventContent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });

            avisTab.setGraphic(createTabHeader("Avis", avisIcon));


            FontIcon abonnementIcon = new FontIcon(Feather.DOLLAR_SIGN);

            abonnementTab = new Tab();
            abonnementTab.setOnSelectionChanged(event -> {
                try {
                    if (abonnementTab.isSelected()) {
                        Parent eventContent = FXMLLoader.load(getClass().getResource(Routes.FRONT_OFFRE));
                        abonnementTab.setContent(eventContent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });

            abonnementTab.setGraphic(createTabHeader("Abonnements", abonnementIcon));

            System.out.println(activeSubscription);
            if (activeSubscription) {
                Offre offre=new OffreService().getEntityByID(abonnement.getIdOffre());
                tabPane.getTabs().addAll(homeTab, abonnementTab);
                if(offre.getType().getLabel().equals("Gold") || offre.getType().getLabel().equals("Silver") || offre.getType().getLabel().equals("Bronze")) tabPane.getTabs().addAll(oeuvreTab);
                if(offre.getType().getLabel().equals("Gold") || offre.getType().getLabel().equals("Silver")) tabPane.getTabs().add(eventsTab);
                if(offre.getType().getLabel().equals("Gold")) tabPane.getTabs().add(ateliersTab);

            } else {
                tabPane.getTabs().addAll(homeTab, abonnementTab);

            }
            tabPane.getSelectionModel().select(0);

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
    void goToBackoffice(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(Routes.BACK_CONTAINER));

        Scene scene = new Scene(root);
        Stage stage = (Stage) profilemenu.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
