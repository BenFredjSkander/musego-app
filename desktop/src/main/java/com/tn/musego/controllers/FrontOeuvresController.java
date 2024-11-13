package com.tn.musego.controllers;

import animatefx.animation.BounceIn;
import com.tn.musego.HelloApplication;
import com.tn.musego.entities.Artiste;
import com.tn.musego.entities.Oeuvre;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.ArtisteService;
import com.tn.musego.services.impl.OeuvreService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FrontOeuvresController implements Initializable {
    @FXML
    VBox oeuvresVBox;
    @FXML
    VBox artistesVBox;
    ArtisteService arts = new ArtisteService();
    Artiste a = new Artiste();
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ScrollPane scrollPaneArtiste;
    @FXML
    private TextField searchbar;

    @FXML
    void viewOeuvreFront(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchbar.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String textSearch = searchbar.getText();
                System.out.println("haw chneya ktebt +++++++++++++++++++++++" + textSearch);
                oeuvresVBox = afficheroeuvresVBox(textSearch.equals("") ? "all" : textSearch);
                scrollPane.setContent(oeuvresVBox);
            }
        });

        oeuvresVBox = afficheroeuvresVBox("all");
        scrollPane.setContent(oeuvresVBox);


    }

    public VBox afficheroeuvresVBox(String test) {
        OeuvreService pcd = new OeuvreService();
        List<Oeuvre> OeuvresList = pcd.afficherListOeuvres(test);

        VBox vBox = new VBox();
        vBox.setSpacing(20); // espacement vertical entre les éléments

        //affichage pour les Oeuvres/////
        for (Oeuvre o : OeuvresList) {
            HBox hbox = new HBox();
            VBox details = new VBox();
            ImageView imageView = new ImageView(new Image(o.getImage()));
            imageView.setFitWidth(150);
            imageView.setPreserveRatio(true);
//            imageView.setFitHeight(150);
            new BounceIn(imageView).play();
            imageView.setOnMouseClicked(event ->
            {
                int id = o.getId();
                System.out.println(("id:" + id));
                imageView.setUserData(o);
                imageView.getUserData();
                System.out.println("data: " + imageView.getUserData());

                try {
                    artistesVBox = afficherArtistesVBox(o.getIdArtiste());
                } catch (MyCustomException e) {
                    throw new RuntimeException(e);
                }
                scrollPaneArtiste.setContent(artistesVBox);

            });
            Label titreLabel = new Label(o.getTitre());
            titreLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-weight: bold;");

            Label dateLabel = new Label("Date de création: " + o.getDateCreation().toString());
            dateLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

            Label categorieLabel = new Label("Categorie: " + o.getCategorie());
            categorieLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black; ");

            Label descriptionLabel = new Label("Description: " + o.getDescription());
            dateLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

            Label lieuLabel = new Label("Lieu: " + o.getLieuDeConservartion());
            lieuLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black; ");
            details.getChildren().addAll(titreLabel, dateLabel, categorieLabel, descriptionLabel, lieuLabel, createStatusBox());
            hbox.getChildren().addAll(imageView, details);
            hbox.setSpacing(20);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setPadding(new Insets(10));

            vBox.getChildren().add(hbox);

        }

        return vBox;
    }

    private HBox createStatusBox() {
        HBox statusHBox = new HBox();
        statusHBox.setSpacing(5);
        statusHBox.setAlignment(Pos.CENTER_LEFT);

        // add status elements to statusHBox

        return statusHBox;
    }

    public VBox afficherArtistesVBox(Integer s) throws MyCustomException {
        ArtisteService arts = new ArtisteService();
        System.out.println("ssss:" + s);

        a = arts.afficherUnArtiste(s);
        System.out.println("aaaaaaaaaa: " + a.getId());
        VBox vBox = new VBox();
        vBox.setSpacing(20); // espacement vertical entre les éléments

        HBox hbox = new HBox();

        ImageView imageView2 = new ImageView(new Image(a.getImage()));
        imageView2.setFitWidth(290);
        imageView2.setPreserveRatio(true);
        imageView2.setFitHeight(290);
        new BounceIn(imageView2).play();
        Label nomLabelA = new Label(a.getNom() + " " + a.getPrenom());
        nomLabelA.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-weight: bold;");

        Label dateLabelA = new Label("Date de naissance: " + a.getDateDeNaissance().toString());
        dateLabelA.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

        Label adresseLabelA = new Label("Adresse: " + a.getAdresse());
        adresseLabelA.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

        Label specialiteLabelA = new Label("Specialite: " + a.getSpecialite());
        specialiteLabelA.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

        Label nationaliteLabelA = new Label("Nationalité: " + a.getNationalite());
        nationaliteLabelA.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

        vBox.getChildren().addAll(imageView2, nomLabelA, dateLabelA, adresseLabelA, specialiteLabelA, nationaliteLabelA, createStatusBox());
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER_LEFT);
        vBox.setPadding(new Insets(10));

        return vBox;
    }

    /*******************Musique********************/
    public void jouerMusique(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mp3.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Jouons de la musique!");
        stage.setScene(scene);
        stage.show();


    }


}