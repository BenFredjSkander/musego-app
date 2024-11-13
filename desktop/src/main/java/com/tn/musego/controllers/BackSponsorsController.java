package com.tn.musego.controllers;

import animatefx.animation.BounceIn;
import com.tn.musego.HelloApplication;
import com.tn.musego.entities.Evenements;
import com.tn.musego.entities.Sponsor;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.EvenementService;
import com.tn.musego.services.impl.SponsorService;
import com.tn.musego.utils.ImageUploadHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BackSponsorsController implements Initializable {
    ImageUploadHelper imageUploadHelper = new ImageUploadHelper();
    @FXML
    private Stage stage;
    @FXML
    private TableView<Sponsor> sponsorTable;
    @FXML
    private TableColumn<Sponsor, String> nom;
    @FXML

    private TableColumn<Sponsor, String> event_sponsorise;
    @FXML
    private TableColumn<Sponsor, Image> photo;
    @FXML
    private TableColumn<Sponsor, Integer> tab_capacite_finan;
    @FXML
    private TableColumn<Sponsor, Integer> id;

    @FXML
    private Button input_poster;


    @FXML
    private Button ajoutersponsor;

    private ObservableList<Sponsor> sponsorsList = FXCollections.observableArrayList();
    @FXML
    private Button button_confirm_modif;
    /////////////////MODIFIER SPONSOR////////////
    @FXML
    private Button button_select_modifier;
    @FXML
    private Button button_supprimer;
    private ObservableList<Sponsor> dataList = FXCollections.observableArrayList();
    @FXML
    private TextField id_sponsor;
    @FXML
    private TextField capacitefinan;
    @FXML
    private TextField nom_sponsor;
    @FXML
    private ComboBox<Evenements> eventselected;
    @FXML
    private File file;
    @FXML
    private TextField searchbar;

    private Evenements selectedEventForEdit;
    private ObservableList<Evenements> observableIdEvenements;

    public String getImagePathFromString(String imagePath) {
        return new File(imagePath).toURI().toString();
    }

//    private void affichersponsors() {
//
//        SponsorService sponsorService = new SponsorService();
//        sponsorsList=  FXCollections.observableList(sponsorService.afficherSponsors());
//        sponsorTable.setItems(sponsorsList);
//    }

    @FXML
    void ajouterSponsor(ActionEvent event) {

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("back-ajouter-sponsor.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("hello musego!");
        stage.setScene(scene);
        stage.showAndWait();
        fillTable();

    }

    public boolean controleSaisie() {
        String nomSponsor = nom.getText().trim();
        String capaciteFinanSponsor = capacitefinan.getText().trim();

        // Vérifier si tous les champs sont remplis
        if (nomSponsor.isEmpty() || capaciteFinanSponsor.isEmpty() || eventselected.getSelectionModel().isEmpty()) {
            return false;
        }

        // Vérifier si le nom contient uniquement des lettres et des espaces
        if (!nomSponsor.matches("^[a-zA-Z ]+$")) {
            return false;
        }

        // Vérifier si la capacité financière est supérieure à 1000
        double capaciteFinan = 0;
        try {
            capaciteFinan = Double.parseDouble(capaciteFinanSponsor);
        } catch (NumberFormatException e) {
            return false;
        }
        if (capaciteFinan < 1000) {
            return false;
        }

        return true;
    }

    @FXML
    void select_Sponsor(ActionEvent event) {

        if (sponsorTable.getSelectionModel().getSelectedItem() == null) {
            showMessage(Alert.AlertType.ERROR, "Veuillez sélectionnez un sponsor");
            return;
        }
        // Get the selected event from the table
        Sponsor selectedSponsor = sponsorTable.getSelectionModel().getSelectedItem();

        if (selectedSponsor != null) {

            ///afficher les donnes de l 'evenement selectionne///
            nom_sponsor.setText(String.valueOf(selectedSponsor.getNom()));

            capacitefinan.setText(String.valueOf(selectedSponsor.getCapaciteFin()));

/*
           eventselected.getSelectionModel(selectedSponsor.getId_evenement())*/


            id_sponsor.setText(String.valueOf(selectedSponsor.getId()));

            input_poster.setText(String.valueOf(selectedSponsor.getPhoto()));
            //eventselected.setValue(selectedEventForEdit);
        }


    }

    @FXML
    void add_poster(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg"));

        // Afficher la boîte de dialogue et obtenir le fichier sélectionné//
        File fichierSelectionne = fileChooser.showOpenDialog(stage);

        if (fichierSelectionne != null) {
            input_poster.setText("Fichier sélectionné : " + fichierSelectionne.getName());
            file = fichierSelectionne;
        }


    }

    @FXML
    void modifierSponsor(ActionEvent event) throws IOException, MyCustomException {

        if (!controleSaisie()) {
            return;
        }

/////////ken maselectionnech hata event bech nafficihilou erreur ///////////
        if (id_sponsor.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("info sponsor ");
            alert.setHeaderText(null);
            alert.setContentText("vous n'avez pas selectionné un sponsor !");
            alert.show();
            return;
        }
        Sponsor e1 = new Sponsor();

        //////on va attribuer   les aattributs  eli ml  formulaire ll variable e //
        e1.setId(Long.parseLong(id_sponsor.getText()));
        e1.setNom(nom_sponsor.getText());
        e1.setCapaciteFin(Integer.parseInt(capacitefinan.getText()));

        String url = imageUploadHelper.upload(file, "images");
        e1.setPhoto(url);
        e1.setIdEvenement((int) eventselected.getSelectionModel().getSelectedItem().getId());


        ///modifier l evenement//
        SponsorService pcm = new SponsorService();
        pcm.modifierSponsor(e1);


        //////refresh tee table///
        fillTable();


////////////affichage msg tee modfier////
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("info evenement ");
        alert.setHeaderText(null);
        alert.setContentText("sponsor modifié avec succés!");
        alert.show();

    }

    public void recherche() {
        FilteredList<Sponsor> filteredData = new FilteredList<>(dataList, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(sponsor -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null)
                    return true;
                String searchKeyword = newValue.toLowerCase();
                if (sponsor.getNom().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else
                    return false;

            });
        });
        SortedList<Sponsor> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(sponsorTable.comparatorProperty());
        sponsorTable.setItems(sortedData);


    }


///////////supprimer sponsor//////////

    @FXML
    void supprimer_sponsor(ActionEvent event) {

        if (sponsorTable.getSelectionModel().getSelectedItem() == null) {
            showMessage(Alert.AlertType.ERROR, "Veuillez sélectionnez un sponsor");
            return;
        }


        // Get the selected event from the table
        Sponsor selectedSponsor = sponsorTable.getSelectionModel().getSelectedItem();

        // Call the method to delete the event
        SponsorService pcd = new SponsorService();
        pcd.supprimerSponsor((int) selectedSponsor.getId());
        System.out.println("//////////////////////////////////////" + selectedSponsor.getId());
        // Remove the event from the data list
        dataList.remove(selectedSponsor);


        //////affichage tee msg//////
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("info participation ");
        alert.setHeaderText(null);
        alert.setContentText("sponsor supprimé  avec succés!");
        alert.show();


        // Remove the sponsor from the data list
        fillTable();


    }


    void fillTable() {
        System.out.println("from filltable");
        sponsorTable.getItems().clear();
        SponsorService sponsorService = new SponsorService();
        sponsorsList = FXCollections.observableList(sponsorService.afficherSponsors());
        sponsorTable.setItems(sponsorsList);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        new BounceIn(sponsorTable).play();

        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));

        tab_capacite_finan.setCellValueFactory(new PropertyValueFactory<>("capaciteFin"));
        event_sponsorise.setCellValueFactory(new PropertyValueFactory<>("idEvenement"));
        photo.setCellValueFactory(cellData -> {
            String photoPath = cellData.getValue().getPhoto();
            if (photoPath != null && !photoPath.isEmpty()) {
                Image image = new Image(photoPath);
                return new SimpleObjectProperty<>(image);
            } else {
                return new SimpleObjectProperty<>(null);
            }
        });

        photo.setCellFactory(column -> {
            return new TableCell<Sponsor, Image>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(Image item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        imageView.setPreserveRatio(true);
                        imageView.setFitHeight(60);
                        imageView.setImage(item);
                        setGraphic(imageView);
                    }
                }
            };
        });


        observableIdEvenements = FXCollections.observableArrayList(new EvenementService().listeEvenements());

        eventselected.setConverter(new StringConverter<>() {
            @Override
            public String toString(Evenements object) {
                if (object != null) {
                    return object.getNom();
                } else return "";
            }

            @Override
            public Evenements fromString(String string) {
                return eventselected.getSelectionModel().getSelectedItem();
            }
        });
        eventselected.setItems(observableIdEvenements);

        fillTable();//maj//
    }

    void showMessage(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("info sponsor");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

}
