package com.tn.musego.controllers;

import com.tn.musego.entities.Artiste;
import com.tn.musego.entities.Categorie;
import com.tn.musego.entities.Oeuvre;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.ArtisteService;
import com.tn.musego.services.impl.CategorieService;
import com.tn.musego.services.impl.OeuvreService;
import com.tn.musego.utils.AlertHelper;
import com.tn.musego.utils.ImageUploadHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

public class BackAjouterOeuvreController implements Initializable {

    Oeuvre o = new Oeuvre();
    @FXML
    private TextField txtTitreO;
    @FXML
    private TextField txtCategorieO;
    @FXML
    private TextArea txtDescriptionO;
    @FXML
    private TextField txtLieucO;
    @FXML
    private TextField txtImageO;
    @FXML
    private TextArea txtImageO1;
    @FXML
    private DatePicker dpdatecO;
    @FXML
    private ComboBox<Artiste> cbidA;
    @FXML
    private ComboBox<Categorie> cbidC;
    @FXML
    private Button btnAjoutO;
    @FXML
    private Label alertLabel;
    @FXML
    private Button btnBrowse;
    private Image image;
    @FXML
    private ImageView imageView;
    private File file;
    @FXML
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
                    return object.getNom() + " "+ object.getPrenom();
                } else return "";
            }

            @Override
            public Artiste fromString(String string) {
                return cbidA.getSelectionModel().getSelectedItem();
            }
        });

        dpdatecO.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0);
            }
        });

        /***********CONTROLE DE SAISIE***********/
        CntrlSaisieString(txtTitreO);
        //CntrlSaisieString(txtCategorieO);
        CntrlSaisieString(txtDescriptionO);
        CntrlSaisieString(txtLieucO);
        btnAjoutO.disableProperty().bind(txtTitreO.textProperty().isEmpty().or(txtLieucO.textProperty().isEmpty().or(txtDescriptionO.textProperty().isEmpty().or(cbidA.getSelectionModel().selectedItemProperty().isNull().or(cbidC.getSelectionModel().selectedItemProperty().isNull().or(dpdatecO.valueProperty().isNull()))))));

    }


    @FXML
    void addImage(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez un fichier PNG");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));

        // Afficher la boîte de dialogue et obtenir le fichier sélectionné//
        File fichierSelectionne = fileChooser.showOpenDialog(stage);

        if (fichierSelectionne != null) {
            btnBrowse.setText("Fichier sélectionné : " + fichierSelectionne.getName());
            file = fichierSelectionne;
        }


    }


    public void CntrlSaisieString(TextInputControl txt) {
        txt.addEventFilter(KeyEvent.KEY_TYPED, eve -> {
//            String text = ((TextField) eve.getSource()).getText();
            String character = eve.getCharacter();
            String regex = "[\\p{L}\\p{Punct} ]+";

            // If the character entered is not a digit, consume the event
            if (!character.matches(regex)) {
                eve.consume();
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
        if (!dpdatecO.getValue().isBefore(LocalDate.now())) {
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

    public void AjoutOeuvre(ActionEvent event) throws SQLException, IOException, MyCustomException {
        Date dateCreat = Date.valueOf(dpdatecO.getValue());
        ImageUploadHelper imageUploadHelper = new ImageUploadHelper();
        String titre = txtTitreO.getText();
      //  String categorie = txtCategorieO.getText();
        String description = txtDescriptionO.getText();
        String lieuc = txtLieucO.getText();

        int idArt = cbidA.getSelectionModel().getSelectedItem().getId();
        int idCat = cbidC.getSelectionModel().getSelectedItem().getId();
        o.setTitre(titre);
     //   o.setCategorie(categorie);
        o.setDescription(description);
        o.setDateCreation(dateCreat);
        o.setLieuDeConservartion(lieuc);
        o.setIdArtiste(idArt);
        o.setCategorie(idCat);
//        o.setImage("http://localhost/images/oeuvres/" + file.getName());

        if (CntrlSaisieDate()) {
            String url = imageUploadHelper.upload(file, "images");
            o.setImage(url);
            try {
                OeuvreService oeuvreService = new OeuvreService();
                oeuvreService.ajouterOeuvre(o);
                AlertHelper.showSuccess("Oeuvre ajoutée avec succées");
            } catch (Exception e) {
                AlertHelper.showError("Une erreur c'");
            }
        }
    }

    @FXML
    void SelectArt(ActionEvent event) {

    }


}
