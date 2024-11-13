package com.tn.musego.controllers;

import com.tn.musego.entities.Artiste;
import com.tn.musego.entities.Offre;
import com.tn.musego.entities.enums.TypeOffreEnum;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.ArtisteService;
import com.tn.musego.services.impl.OffreService;
import com.tn.musego.utils.DateHelper;
import com.tn.musego.utils.ImageUploadHelper;
import com.tn.musego.utils.validator.*;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import com.tn.musego.utils.FunctionHelper;
import com.tn.musego.utils.ValidatorHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class AddOffreController implements Initializable {
    ValidatorHelper validatorHelper = new ValidatorHelper();
    ImageUploadHelper imageUploadHelper = new ImageUploadHelper();

    @FXML
    private Stage stage;
    @FXML
    private File file;
    @FXML
    Label lblerr;
    @FXML
    private ComboBox<TypeOffreEnum> combotype;
    @FXML
    private DatePicker dtdebut;
    @FXML
    private DatePicker dtfin;
    @FXML
    private TextField tfprix;
    @FXML
    private TextField tfpromotion;
    @FXML
    private TextArea tadesc;

    @FXML
    private Button btnfile;

    @FXML
    private Button btnadd;

    @FXML
    public void initialize() {
        combotype.setValue(TypeOffreEnum.BRONZE);
        combotype.getItems().removeAll(combotype.getItems());
        combotype.getItems().addAll(TypeOffreEnum.values());

        testInteger(tfprix);
        testInteger(tfpromotion);


        btnadd.disableProperty().bind(combotype.valueProperty().isNull().or(tfprix.textProperty().isEmpty().or(tfpromotion.textProperty().isEmpty().or(dtdebut.valueProperty().isNull().or(dtfin.valueProperty().isNull()).or(tadesc.textProperty().isEmpty())))));

    }

    public void testInteger(TextField txt) {
        txt.addEventFilter(KeyEvent.KEY_TYPED, str -> {
            String chr = str.getCharacter();
            String regex = "^[0-9]$";

            if (!chr.matches(regex)) {
                str.consume();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Champs invalid");
                alert.setHeaderText("Information");
                alert.setContentText("Vous ne pouvez ajouter que des chiffres !");
                alert.showAndWait();
                alert.close();
            }
        });
    }

    @FXML
    public void addBut(ActionEvent event) throws IOException, MyCustomException {
        Offre offre = new Offre();


        Double prix = Double.parseDouble(tfprix.getText());
        Double promotion = Double.parseDouble(tfpromotion.getText());
        Date dateDebut = Date.valueOf(dtdebut.getValue());
        Date dateFin = Date.valueOf(dtfin.getValue());
        String description = tadesc.getText();
        TypeOffreEnum type = combotype.getSelectionModel().getSelectedItem();

        offre.setType(type);
        offre.setDateDebut(dateDebut);
        offre.setDateFin(dateFin);
        offre.setPrix(prix);
        offre.setPromotion(promotion);
        offre.setDescription(description);

        if (validatorHelper.validateFields() && prix <= 10000 && promotion <100 && file!=null) {
            String url = imageUploadHelper.upload(file, "images");
            offre.setImage(url);
            try {
                OffreService offreService = new OffreService();
                offreService.createEntity(offre);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ajouter Offre");
                alert.setHeaderText("Information");
                alert.setContentText("Offre est bien ajouté");
                alert.showAndWait();

                Stage stage = (Stage) btnadd.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ajouter Offre");
                alert.setHeaderText("Information");
                alert.setContentText("Offre n'est pas ajouté");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ajouter Offre");
            alert.setHeaderText("Erreur");
            alert.setContentText("Vérifiez les champs: prix < 10000, promtion < 100, une image doit être choisi.");
            alert.showAndWait();
        }
    }

    @FXML
    void fileBut(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez un fichier PNG");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images Files", "*.png", "*.jpg"));

        // Afficher la boîte de dialogue et obtenir le fichier sélectionné//
        File fichierSelectionne = fileChooser.showOpenDialog(stage);

        if (fichierSelectionne != null) {
            btnfile.setText("Fichier sélectionné : " + fichierSelectionne.getName());
            file = fichierSelectionne;
        }


    }

    @FXML
    public void annulerBut() {
        combotype.setValue(TypeOffreEnum.BRONZE);
        dtfin.setValue(null);
        dtdebut.setValue(null);
        tfprix.setText(null);
        tfpromotion.setText(null);
        tadesc.setText(null);
        lblerr.setText(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FunctionHelper.disablePastDates(dtdebut);
        combotype.getItems().removeAll(combotype.getItems());
        combotype.getItems().addAll(TypeOffreEnum.values());

        testInteger(tfprix);
        testInteger(tfpromotion);


        btnadd.disableProperty().bind(combotype.valueProperty().isNull().or(tfprix.textProperty().isEmpty().or(tfpromotion.textProperty().isEmpty().or(dtdebut.valueProperty().isNull().or(dtfin.valueProperty().isNull()).or(tadesc.textProperty().isEmpty())))));

        validatorHelper.addValidator(new ChoiceValidator(combotype));
        /*validatorHelper.addValidator(new NumberFieldValidator(tfprix));
        validatorHelper.addValidator(new NumberFieldValidator(tfpromotion));*/
        validatorHelper.addValidator(new DateAfterValidator(dtdebut, dtfin));
        validatorHelper.addValidator(new TextAreaValidator(tadesc));
    }
}
