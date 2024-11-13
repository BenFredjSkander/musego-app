package com.tn.musego.controllers;

import com.tn.musego.entities.Artiste;
import com.tn.musego.entities.Offre;
import com.tn.musego.entities.enums.TypeOffreEnum;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.impl.ArtisteService;
import com.tn.musego.services.impl.OffreService;
import com.tn.musego.utils.AlertHelper;
import com.tn.musego.utils.DateHelper;
import com.tn.musego.utils.ImageUploadHelper;
import com.tn.musego.utils.ValidatorHelper;
import com.tn.musego.utils.validator.ChoiceValidator;
import com.tn.musego.utils.validator.DateAfterValidator;
import com.tn.musego.utils.validator.TextAreaValidator;
import com.tn.musego.utils.validator.TextFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class EditOffreController {


    ValidatorHelper validatorHelper = new ValidatorHelper();
    ImageUploadHelper imageUploadHelper = new ImageUploadHelper();
    @FXML
    private File file;

    @FXML
    Label lblerr;

    @FXML
    private Stage stage;

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
    private Button btnedit;

    public Long id;

    @FXML
    public void initialize() {
        combotype.getItems().addAll(TypeOffreEnum.values());

        testInteger(tfprix);
        testInteger(tfpromotion);


        btnedit.disableProperty().bind(combotype.valueProperty().isNull().or(tfprix.textProperty().isEmpty().or(tfpromotion.textProperty().isEmpty().or(dtdebut.valueProperty().isNull().or(dtfin.valueProperty().isNull()).or(tadesc.textProperty().isEmpty())))));


        validatorHelper.addValidator(new ChoiceValidator(combotype));
        validatorHelper.addValidator(new TextFieldValidator(tfprix));
        validatorHelper.addValidator(new TextFieldValidator(tfpromotion));
        validatorHelper.addValidator(new DateAfterValidator(dtdebut, dtfin));
        validatorHelper.addValidator(new TextAreaValidator(tadesc));
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


    public void initFields(Offre offre) {
        combotype.setValue(offre.getType());
        dtdebut.setValue(Instant.ofEpochMilli(offre.getDateDebut().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        dtfin.setValue(Instant.ofEpochMilli(offre.getDateFin().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        tfprix.setText(offre.getPrix().toString());
        tfpromotion.setText(offre.getPromotion().toString());
        tadesc.setText(offre.getDescription());
        lblerr.setText(null);

        id=offre.getId();
    }

    @FXML
    public void editBut(ActionEvent event) throws IOException, MyCustomException {

        Offre offre = new Offre();


        Double prix = Double.parseDouble(tfprix.getText());
        Double promotion = Double.parseDouble(tfpromotion.getText());
        java.sql.Date dateDebut = java.sql.Date.valueOf(dtdebut.getValue());
        java.sql.Date dateFin = java.sql.Date.valueOf(dtfin.getValue());
        String description = tadesc.getText();
        TypeOffreEnum type = combotype.getSelectionModel().getSelectedItem();

        offre.setId(id);
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
                offreService.updateEntity(offre);
                AlertHelper.showSuccess("Offre modifié avec succès");

                Stage stage = (Stage) btnedit.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                AlertHelper.showError("Erreur lors de la modification");
                System.out.println(e.getMessage());
            }

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mofification Offre");
            alert.setHeaderText("Erreur");
            alert.setContentText("Vérifiez les champs: prix < 10000, promtion < 100, une image doit être choisi.");
            alert.showAndWait();
        }
    }

    @FXML
    void fileBut(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez un fichier PNG");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Images PNG", ".png", ".jpg"));

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
        tfprix.clear();
        tfpromotion.clear();
        tadesc.clear();
        lblerr.setText(null);
    }
}
