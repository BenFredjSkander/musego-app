package com.tn.musego.controllers;


import animatefx.animation.BounceIn;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.tn.musego.HelloApplication;
import com.tn.musego.entities.Evenements;
import com.tn.musego.entities.Participation;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.EvenementService;
import com.tn.musego.utils.ImageUploadHelper;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static java.sql.Date.valueOf;

public class BackEventController implements Initializable {

    ImageUploadHelper imageUploadHelper = new ImageUploadHelper();
    @FXML
    private File file;

    @FXML
    private Button annulerButton;

    @FXML
    private Stage stage;
    @FXML
    private DatePicker date_debut;

    @FXML
    private DatePicker date_fin;

    @FXML
    private TextArea description_event;

    @FXML
    private Button input_poster;

    @FXML
    private TextField lieu_event;

    @FXML
    private TextField nbParticipantsTextField;

    @FXML
    private TextField nom_event;

    @FXML
    private HBox Evenements_id;

    @FXML
    private GridPane root;

    @FXML
    private TextField type_event;


    @FXML
    private Button btn_sponsor;

    @FXML
    private Button rechercheBtn;

    /*
        @FXML
        void consulter_sponsors(ActionEvent event) {

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("back-sponsor.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setTitle("hello musego!");
            stage.setScene(scene);
            stage.show();

        }*/
    ///////////////////////////////////////////////////////////modifier event /////////////////////////////////////////
    @FXML
    private Button button_confirm_modif;


    /////////////////////////////////partie mtee events////////////////////
    @FXML
    private Button button_modifier;
    @FXML
    private TextField id_event;
    @FXML
    private Button button_supprimer;
    ///////partie ajout /////////
    @FXML
    private Button ajouterButton;
    @FXML
    private TableView<Evenements> table_events;


    ////supprimer event/////
    @FXML
    private TableColumn<Evenements, String> tab_id;
    @FXML
    private TableColumn<Evenements, String> tab_date_debut;


    /////////////////////////////////////////  ajouter events ///////////////////////////////////////
    @FXML
    private TableColumn<Evenements, String> tab_date_fin;
    @FXML
    private TableColumn<Evenements, String> tab_nb_participants;
    @FXML
    private TableColumn<Evenements, String> tab_nom;
    @FXML
    private TableColumn<Evenements, String> tab_type;
    @FXML
    private TableColumn<Evenements, String> tab_lieu;


    ///////////////fonction affichage events ///////////
    private ObservableList<Evenements> dataList = FXCollections.observableArrayList();
    private ObservableList<Participation> participList = FXCollections.observableArrayList();

    @FXML
    private TextField searchbar;
    //    @FXML
//    private TableView<Participation> table_particp;
//    @FXML
//    private TableColumn<?, ?> particip_id_event;
//    @FXML
//    private TableColumn<?, ?> particip_id_user;
    //////////////////////////////supprimer Participation////////////////////////
    @FXML
    private Button button_supprimer_partcip;
    @FXML
    private Button button_rapport;

//    @FXML
//    void consulter_sponsors(ActionEvent event) {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("back-sponsor.fxml"));
//        Parent root = null;
//        try {
//            root = fxmlLoader.load();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        // récupération de la scène actuelle
//        Button btn = (Button) event.getSource();
//        Scene scene = btn.getScene();
//
//        // création d'une nouvelle scène avec la racine chargée
//        Scene newScene = new Scene(root);
//
//        // remplacement de la scène actuelle avec la nouvelle scène
//        Stage stage = (Stage) scene.getWindow();
//        stage.setScene(newScene);
//        stage.setTitle("hello musego!");
//        stage.show();
//    }

    @FXML
    void select_events() {
        if (table_events.getSelectionModel().getSelectedItem() == null) {
            showMessage(Alert.AlertType.ERROR, "Veuillez sélectionner un évenement");
            return;
        }
        // Get the selected event from the table
        Evenements selectedEvent = table_events.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {

            ///afficher les donnes de l 'evenement selectionne///
            id_event.setText(String.valueOf(selectedEvent.getId()));
            nom_event.setText(selectedEvent.getNom());
            date_debut.setValue(Instant.ofEpochMilli(selectedEvent.getDateDebut().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
            date_fin.setValue(Instant.ofEpochMilli(selectedEvent.getDateFin().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());

            type_event.setText(selectedEvent.getType());
            lieu_event.setText(selectedEvent.getLieu());
            description_event.setText(selectedEvent.getDescription());
            nbParticipantsTextField.setText(String.valueOf(selectedEvent.getNbParticipants()));


        }


    }

    @FXML
    void modifier_event(ActionEvent event) throws IOException, MyCustomException {
        if (!controleSaisie()) {
            return;
        }

/////////ken maselectionnech hata event bech nafficihilou erreur ///////////
        if (id_event.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("info evenement ");
            alert.setHeaderText(null);
            alert.setContentText("vous n'avez pas selectionné un evenement !");
            alert.show();
            return;
        }
        Evenements e1 = new Evenements();

        //////on va attribuer   les aattributs  eli ml  formulaire ll variable e //
        e1.setId(Long.parseLong(id_event.getText()));
        e1.setNom(nom_event.getText());


        e1.setDateDebut(valueOf(date_debut.getValue()));//
        e1.setDateFin(valueOf(date_fin.getValue()));//


        e1.setType(type_event.getText());
        e1.setLieu(lieu_event.getText());
        e1.setDescription(description_event.getText());

        System.out.println(file.getName());
//        e1.setPoster("http://localhost/images/events/" + file.getName());
        String url = imageUploadHelper.upload(file, "images");
        e1.setPoster(url);

        ///modifier l evenement//
        EvenementService pcm = new EvenementService();
        pcm.modifierEvenement(e1);


////////////affichage msg tee modfier////
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("info evenement ");
        alert.setHeaderText(null);
        alert.setContentText("evenement modifié avec succés!");
        alert.showAndWait();
        fillTable();

    }

    @FXML
    void supprimer_events(ActionEvent event) {
        if (table_events.getSelectionModel().getSelectedItem() == null) {
            showMessage(Alert.AlertType.ERROR, "Veuillez sélectionner un évenement");
            return;
        }
        // Get the selected event from the table
        Evenements selectedEvent = table_events.getSelectionModel().getSelectedItem();

        // Call the method to delete the event
        EvenementService pcd = new EvenementService();
        pcd.supprimerEvenement((int) selectedEvent.getId());

        // Remove the event from the data list
        dataList.remove(selectedEvent);


        //////affichage tee msg//////
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("info evenement ");
        alert.setHeaderText(null);
        alert.setContentText("evenement supprimé avec succés!");
        alert.showAndWait();
        fillTable();

    }

/////////////////////////////////

    @FXML
    void add_poster(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez un fichier PNG");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images PNG", "*.png", "*.jpg"));

        // Afficher la boîte de dialogue et obtenir le fichier sélectionné//
        File fichierSelectionne = fileChooser.showOpenDialog(stage);

        if (fichierSelectionne != null) {
            input_poster.setText("Fichier sélectionné : " + fichierSelectionne.getName());
            file = fichierSelectionne;
        }


    }

    /////////////////////////////////controle de saisie/////////////
    public boolean controleSaisie() {
        String nom = nom_event.getText();
        String type = type_event.getText();
        String lieu = lieu_event.getText();

        if (nom.isEmpty() || type.isEmpty() || lieu.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Les champs nom, type et lieu sont obligatoires");
            alert.show();
            return false;
        }
        if (!nom.matches("[\\p{L}\\p{Punct} ]+") || !type.matches("[\\p{L}\\p{Punct} ]+") || !lieu.matches("[\\p{L}\\p{Punct} ]+")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Les champs nom, type et lieu doivent être saisis avec des lettres, des espaces et des symboles de ponctuation seulement");
            alert.show();
            return false;
        }


        LocalDate debut = date_debut.getValue();
        LocalDate fin = date_fin.getValue();
        if (debut == null || fin == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Les champs date début et date fin sont obligatoires");
            alert.show();
            return false;
        }

        if (debut.isBefore(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("La date de début doit être postérieure à la date actuelle");
            alert.show();
            return false;
        }

        if (debut.isAfter(fin)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("La date de début doit être antérieure à la date de fin");
            alert.show();
            return false;
        }

        return true;
    }


    ///////////////////////////////////////////////////////////////////////////////participations///////////////////////


    ///////////////fonction affichage participations  ///////////

    @FXML
    void ajouter_event(ActionEvent event) {

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("back-ajouter-evenement.fxml"));
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

    @FXML
    void annuler(ActionEvent event) {
        nom_event.setText(null);
        date_debut.setValue(null);
        date_fin.setValue(null);
        description_event.setText(null);
        lieu_event.setText(null);
        type_event.setText(null);


    }

    @FXML
    void recherche() {
        FilteredList<Evenements> filteredData = new FilteredList<>(dataList, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Evenements -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null)
                    return true;
                String searchKeyword = newValue.toLowerCase();
                if (Evenements.getNom().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Evenements.getDescription().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Evenements.getLieu().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Evenements.getType().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else
                    return false;

            });
        });
        SortedList<Evenements> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_events.comparatorProperty());
        table_events.setItems(sortedData);


    }

    //////////////////////////////affichage par defaut///////////////////////
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        new BounceIn(table_events).play();

        tab_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tab_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));


        tab_date_debut.setCellValueFactory(cellData -> {          ////tconverti date llstring//////
            SimpleStringProperty property = new SimpleStringProperty();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            if (cellData.getValue().getDateDebut() != null) {
                LocalDate date = LocalDate.parse(cellData.getValue().getDateDebut().toString());
                property.setValue(date.format(formatter));
            } else {
                property.setValue("");
            }
            return property;
        });


        tab_date_fin.setCellValueFactory(cellData -> { ////tconverti date llstring//////

            SimpleStringProperty property = new SimpleStringProperty();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            if (cellData.getValue().getDateDebut() != null) {
                LocalDate date = LocalDate.parse(cellData.getValue().getDateFin().toString());
                property.setValue(date.format(formatter));
            } else {
                property.setValue("");
            }
            return property;
        });


        tab_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        tab_nb_participants.setCellValueFactory(new PropertyValueFactory<>("nbParticipants"));
        tab_lieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));

        fillTable();
//        table_events.refresh();
//        recherche();

    }

    ///////affichage tee events ////
    void fillTable() {


        System.out.println("from filltable");
        table_events.getItems().clear();
        EvenementService pcd = new EvenementService();
        dataList = FXCollections.observableList(pcd.afficherEvenements("all"));
        table_events.setItems(dataList);
    }

//    private void fillparticipants() {
//
//
//        particip_id_event.setCellValueFactory(new PropertyValueFactory<>("id_evenement"));
//        particip_id_user.setCellValueFactory(new PropertyValueFactory<>("id_user"));
//
//
//        ParticipationService pcd = new ParticipationService();
//        pcd.afficherParticipations().forEach(p -> {
//            participList.add(p);
//            System.out.println(p);
//        });
//        table_particp.setItems(participList);
//    }


///////////partie rapport///////////////////////////////

    //@FXML
//    void supprimer_particip(ActionEvent event) {
//
//
//        // Get the selected event from the table
//        Participation selectedparticipation = table_particp.getSelectionModel().getSelectedItem();
//
//        // Call the method to delete the event
//        ParticipationService pcd = new ParticipationService();
//        pcd.supprimerParticipation(selectedparticipation.getId_user(), selectedparticipation.getId_evenement());
//
//        // Remove the event from the data list
//        dataList.remove(selectedparticipation);
//
//
//        //////affichage tee msg//////
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("info participation ");
//        alert.setHeaderText(null);
//        alert.setContentText("participation supprimée  avec succés!");
//        alert.show();
//        table_particp.getItems().clear();
//        fillparticipants();
//
//        // Refresh the table
//        table_particp.refresh();
//
//    }

    public void voir_rapport(ActionEvent actionEvent) {
        if (table_events.getSelectionModel().getSelectedItem() == null) {
            showMessage(Alert.AlertType.ERROR, "Veuillez sélectionner un évenement");
            return;
        }

        Evenements evenement = table_events.getSelectionModel().getSelectedItem();

        try {
            // Création du nom de fichier basé sur le nom de l'événement
            String fileName = "rapport_" + evenement.getNom() + ".pdf";
            // Chemin du dossier de stockage des rapports

            // Chemin complet du fichier PDF

            // Création du fichier PDF
            FileOutputStream fos = new FileOutputStream(fileName);
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Ajout du titre de l'événement
            Paragraph titre = new Paragraph("Nom de l'événement : " + evenement.getNom())
                    .setFontSize(18)
                    .setBold();
            document.add(titre);


            // Ajout de l'image du poster de l'événement
//            Image img = new Image(ImageDataFactory.create(evenement.getPoster()));
//            System.out.println("   ena poster howa "+evenement.getPoster());
//            document.add(img);


            // Ajout des informations de l'événement
            Paragraph dates = new Paragraph("Dates : " + evenement.getDateDebut() + " - " + evenement.getDateFin())
                    .setMarginTop(20);
            document.add(dates);
            Paragraph type = new Paragraph("Type : " + evenement.getType());
            document.add(type);
            Paragraph lieu = new Paragraph("Lieu : " + evenement.getLieu());
            document.add(lieu);
            Paragraph description = new Paragraph("Description : " + evenement.getDescription())
                    .setMarginBottom(20);
            document.add(description);

            // Fermeture du document PDF
            document.close();
            fos.close();
            // Affichage du dialog de sauvegarde
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF doc(*.pdf)", "*.pdf"));

            fileChooser.setTitle("Enregistrer le rapport PDF");
            fileChooser.setInitialFileName(fileName);
            File selectedFile = fileChooser.showSaveDialog(null);
            if (selectedFile != null) {
                Files.move(Path.of(fileName), selectedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


/////////partie stats////////

    public void voir_stats(ActionEvent actionEvent) {

        GraphiqueEvents fenetreStatistiques = new GraphiqueEvents();
        fenetreStatistiques.afficherGraphique();

    }


    public void voir_stats_date(ActionEvent actionEvent) {

        GraphiqueEvents fenetreStatistiques = new GraphiqueEvents();
        fenetreStatistiques.afficherGraphiqueParDate();

    }

/////////////////partie recherche////////////


    public void refresh(ActionEvent actionEvent) {

        // Effacer les éléments existants de la table
        table_events.getItems().clear();
        // Remplir la table avec les données mises à jour
        fillTable();
        // Forcer l'actualisation de l'affichage de la table
        table_events.refresh();


    }


    void showMessage(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("info evenement ");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}

















