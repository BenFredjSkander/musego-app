package com.tn.musego.controllers;

import com.tn.musego.entities.Artiste;
import com.tn.musego.entities.Categorie;
import com.tn.musego.entities.Oeuvre;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.ArtisteService;
import com.tn.musego.services.impl.CategorieService;
import com.tn.musego.services.impl.OeuvreService;
import com.tn.musego.utils.AlertHelper;
import com.tn.musego.utils.DateHelper;
import com.tn.musego.utils.ImageUploadHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ModifierOeuvreController implements Initializable {
    ImageUploadHelper imageUploadHelper = new ImageUploadHelper();
    Oeuvre oeuvre;
    @FXML
    private Button btnBrowse;

    @FXML
    private Button btnModif;

    @FXML
    private ComboBox<Artiste> cbidA;

    @FXML
    private ComboBox<Categorie> cbidC;

    @FXML
    private DatePicker dpdatecOm;

    @FXML
    private TextField txtDescriptionOm;

    @FXML
    private TextField txtIdOm;

    @FXML
    private TextField txtImageOm;

    @FXML
    private TextField txtLieucOm;

    @FXML
    private TextField txtTitreOm;

    private Image image;
    private File file;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        ArtisteService artisteService = new ArtisteService();
        ObservableList<Artiste> observableIdArtistes = FXCollections.observableArrayList();
        observableIdArtistes=artisteService.afficherArtistes();
        cbidA.setItems(observableIdArtistes);
        //  List<Integer> idsArt = new ArtisteService().afficherArtistes().stream().map(Artiste::getId).toList();

        CategorieService categorieService = new CategorieService();
        ObservableList<Categorie> observableIdCategories = FXCollections.observableArrayList();
        observableIdCategories=categorieService.afficherCategories();
        cbidC.setItems(observableIdCategories);

        cbidC.setConverter(new StringConverter<>() {
            @Override
            public String toString(Categorie object) {
                if (object != null) {
                    return object.getNom();
                } else return "";
            }

            @Override
            public Categorie fromString(String string) {
                return cbidC.getSelectionModel().getSelectedItem();
            }
        });
        cbidA.setConverter(new StringConverter<>() {
            @Override
            public String toString(Artiste object) {
                if (object != null) {
                    return object.getNom();
                } else return "";
            }

            @Override
            public Artiste fromString(String string) {
                return cbidA.getSelectionModel().getSelectedItem();
            }
        });



/***********CONTROLE DE SAISIE***********/
        CntrlSaisieString(txtTitreOm);
        //CntrlSaisieString(txtCategorieOm);
        CntrlSaisieString(txtDescriptionOm);
        CntrlSaisieString(txtLieucOm);
        CntrlSaisieString(txtImageOm);
        btnModif.disableProperty().bind(txtTitreOm.textProperty().isEmpty().or(txtLieucOm.textProperty().isEmpty().or(txtDescriptionOm.textProperty().isEmpty().or(cbidA.getSelectionModel().selectedItemProperty().isNull().or(cbidC.getSelectionModel().selectedItemProperty().isNull().or(dpdatecOm.valueProperty().isNull()))))));

    }

    public void CntrlSaisieString(TextField txt) {
        txt.addEventFilter(KeyEvent.KEY_TYPED, eve -> {
            String text = ((TextField) eve.getSource()).getText();
            String character = eve.getCharacter();
            String regex = "[\\p{L}\\p{Punct} ]+";

            // If the character entered is not a digit, consume the event
            if (!character.matches(regex)) {
                eve.consume();
                System.out.println("only string");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ajouter Oeuvre");
                alert.setHeaderText("Information");
                alert.setContentText("Vous ne pouvez ajouter que des lettres et des caracteres speciaux !");
                alert.showAndWait();
                alert.close();

            }
        });
    }

    public boolean CntrlSaisieDate() {
        if (!dpdatecOm.getValue().isBefore(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("La date doit être anterieure à la date actuelle");
            alert.show();
            return false;
        } else {
            return true;
        }
    }

    public void initOeuvre(Oeuvre oeuvre) {
        this.oeuvre = oeuvre;
        txtIdOm.setText(String.valueOf(oeuvre.getId()));
        txtTitreOm.setText(oeuvre.getTitre());
        dpdatecOm.setValue(DateHelper.dateToDatepicker(oeuvre.getDateCreation()));

        txtDescriptionOm.setText(oeuvre.getDescription());
        txtLieucOm.setText(oeuvre.getLieuDeConservartion());
        txtImageOm.setText(oeuvre.getImage());
    }

    @FXML
    void updateImage(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez un fichier PNG");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"), new FileChooser.ExtensionFilter("Images PNG", ".png", ".jpg"));

        // Afficher la boîte de dialogue et obtenir le fichier sélectionné//
        File fichierSelectionne = fileChooser.showOpenDialog(stage);

        if (fichierSelectionne != null) {
            btnBrowse.setText("Fichier sélectionné : " + fichierSelectionne.getName());
            file = fichierSelectionne;
        }


    }

    public void refreshData(TableView table) {
        table.refresh();

    }

    @FXML
    void ModifOeuvre(ActionEvent event) throws SQLException, IOException, MyCustomException {
        Date dateCreat = Date.valueOf(dpdatecOm.getValue());
        String id = txtIdOm.getText();
        String titre = txtTitreOm.getText();
        //  String categorie = txtCategorieOm.getText();
        String description = txtDescriptionOm.getText();
        String lieuC = txtLieucOm.getText();
        int idArt = cbidA.getSelectionModel().getSelectedItem().getId();
        int idCat = cbidC.getSelectionModel().getSelectedItem().getId();
        String image = txtImageOm.getText();
        //  System.out.println("id:" + id);

        // o.setId(Integer.parseInt(id));
        oeuvre.setTitre(titre);
        //   o.setCategorie(categorie);
        oeuvre.setDescription(description);
        oeuvre.setLieuDeConservartion(lieuC);
        oeuvre.setDateCreation(dateCreat);
        oeuvre.setIdArtiste(idArt);
        oeuvre.setCategorie(idCat);
        oeuvre.setImage(image);
        System.out.println(oeuvre.getIdArtiste() +" "+ oeuvre.getCategorie() );

        if (CntrlSaisieDate()) {
            try {
                String url = imageUploadHelper.upload(file, "images");
                oeuvre.setImage(url);
                OeuvreService oeuvreService = new OeuvreService();
                oeuvreService.modifierOeuvre(oeuvre);
                AlertHelper.showSuccess("Oeuvre modifiée avec succès");
            } catch (Exception e) {
                AlertHelper.showError("Erreur lors de la modification");

            }
        }

    }

    @FXML
    void SelectArt(ActionEvent event) {

    }

}
