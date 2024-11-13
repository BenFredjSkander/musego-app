package com.tn.musego.controllers;

import animatefx.animation.BounceIn;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.tn.musego.entities.Abonnement;
import com.tn.musego.entities.Offre;
import com.tn.musego.entities.User;
import com.tn.musego.services.impl.OffreService;
import com.tn.musego.services.impl.UserService;
import com.tn.musego.utils.DateHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class AbonnementFrontController implements Initializable {

    @FXML
    private Button btnfact;

    private Long currentUserId;

    @FXML
    private TextField tfdtdeb;

    @FXML
    private TextField tfdtfin;

    @FXML
    private TextField tfprix;

    @FXML
    private TextField tftype;

    @FXML
    private TextField tfoffreprom;

    @FXML
    private TextField tfoffretype;

    @FXML
    private ImageView img;

    private Abonnement abonnement;

    private Offre offre;

    private User user;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initFields(Abonnement abonnement,User user) {
        this.abonnement=abonnement;
        this.offre=new OffreService().getEntityByID(abonnement.getIdOffre());
        this.user=user;
        currentUserId = new UserService().getCurrentUserID();
        tftype.setText(abonnement.getType().toString());
        tfprix.setText(abonnement.getPrix().toString());
        tfdtdeb.setText(abonnement.getDateDebut().toString().substring(0, 10));
        tfdtfin.setText(abonnement.getDateFin().toString().substring(0, 10));
        tfoffretype.setText(offre.getType().getLabel());
        tfoffreprom.setText(offre.getPromotion().toString());
        if(offre.getImage()!=null){

            ImageView imageView = new ImageView(new Image(offre.getImage()));
            imageView.setFitWidth(150);
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(150);
            new BounceIn(imageView).play();
            img.setImage(imageView.getImage());
        }

    }

    @FXML
    void factBtn(ActionEvent actionEvent) {

        try {

            String fileName = "Facture_" + abonnement.getType() + ".pdf";
            // Chemin du dossier de stockage

            // Chemin complet du fichier PDF

            // Création du fichier PDF
            FileOutputStream fos = new FileOutputStream(fileName);
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            Paragraph titre = new Paragraph("MuseGO")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(24)
                    .setBold();
            document.add(titre);
            Paragraph stitre = new Paragraph("Facturation de l'abonnement "+abonnement.getType())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold();
            document.add(stitre);

            Paragraph paragraph = new Paragraph("Cette facture est destiné au client "+user.getUsername())
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(paragraph);

            Paragraph dated = new Paragraph("Date de début  : " + abonnement.getDateDebut())
                    .setMarginTop(20);
            document.add(dated);
            Paragraph datef = new Paragraph("Date de fin  : " + abonnement.getDateFin());
            document.add(datef);
            Paragraph prix = new Paragraph("Prix (€) : " + abonnement.getPrix());
            document.add(prix);
            Paragraph off = new Paragraph("Sous l'offre : " + offre.getType());
            document.add(off);
            if(offre.getDescription()!=null) {
                Paragraph description = new Paragraph("Description de l'offre : " + offre.getDescription());
                document.add(description);
            }
            Paragraph promotion = new Paragraph("Promotion de l'offre : " + offre.getPromotion())
                    .setMarginBottom(20);
            document.add(promotion);

            Paragraph date = new Paragraph("Crée le : " + DateHelper.currentTime())
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginBottom(20);
            document.add(date);
            // Fermeture du document PDF
            document.close();
            fos.close();

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF doc(*.pdf)", "*.pdf"));

            fileChooser.setTitle("Enregistrer la facture PDF");
            fileChooser.setInitialFileName(fileName);
            File selectedFile = fileChooser.showSaveDialog(null);
            if (selectedFile != null) {
                Files.move(Path.of(fileName), selectedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
