package com.tn.musego;

import com.tn.musego.services.impl.UserService;
import com.tn.musego.utils.Routes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


public class HelloApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException, BackingStoreException {
        FXMLLoader fxmlLoader;
        if (new UserService().getCurrentUserID() != 0) {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(Routes.FRONT_CONTAINER));
        } else {

            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(Routes.LOGIN));
        }
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(HelloApplication.class.getResource("/styles/styles.css")
                .toExternalForm());
        stage.setTitle("Musego");
        stage.getIcons().add(new Image(getClass().getResource("/images/icon.png").openStream()));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


}