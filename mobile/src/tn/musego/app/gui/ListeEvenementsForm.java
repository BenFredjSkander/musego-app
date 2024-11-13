package tn.musego.app.gui;

import com.codename1.components.ScaleImageLabel;
import com.codename1.l10n.DateFormat;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.*;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import tn.musego.app.entities.Evenement;
import tn.musego.app.services.ServiceEvenement;

import java.util.ArrayList;


public class ListeEvenementsForm extends Form {

    public ListeEvenementsForm(Form previous) {
        this.setTitle("Nos Evenements");

        ArrayList<Evenement> evenements = ServiceEvenement.getInstance().getAllEvenements();

        this.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        for (int i = 0; i < evenements.size(); i++) {
            addElement(evenements.get(i));
            if(i != evenements.size()) {
                addSeparator();
            }
        }
        Button voirStats = new Button("Voir Statistiques");
        voirStats.addActionListener(e -> {
            EvenementStatsForm statsForm = new EvenementStatsForm(previous);
            statsForm.show();
        });
        add(voirStats);
        Button genererQR = new Button("Générer QR");
        genererQR.addActionListener(e -> genererCodeQR());
        add(genererQR);


        this.show();
        if (previous != null) {
            this.getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
        }
    }

    private void genererCodeQR() {
        String url = "https://musego.tn";
        int size = 250; // Taille du code QR (en pixels)

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, size, size);

            // Créer une image du code QR
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Image qrImage = createImageFromBitMatrix(bitMatrix, width, height);

            // Afficher l'image du code QR dans une boîte de dialogue
            Dialog qrDialog = new Dialog("Code QR");
            ScaleImageLabel qrImageLabel = new ScaleImageLabel(qrImage);
            qrDialog.setLayout(new BorderLayout());
            qrDialog.add(BorderLayout.CENTER, qrImageLabel);
            qrDialog.setDisposeWhenPointerOutOfBounds(true);
            qrDialog.show();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private Image createImageFromBitMatrix(BitMatrix bitMatrix, int width, int height) {
        Image image = Image.createImage(width, height);
        Graphics graphics = image.getGraphics();
        graphics.setColor(0xFFFFFF); // Couleur blanche
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(0x000000); // Couleur noire

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bitMatrix.get(x, y)) {
                    graphics.fillRect(x, y, 1, 1);
                }
            }
        }

        return image;
    }


    public void addElement(Evenement evenement) {

        // Ajouter le titre de l'événement en gras
        Label nom = new Label(evenement.getNom());
        nom.getStyle().setFgColor(0x000000);
        nom.getStyle().setBgColor(0xffffff);
        nom.getStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
        add(nom);

        // Ajouter les autres détails de l'événement
        Label lieu = new Label("Lieu : " + evenement.getLieu());
        add(lieu);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Label dateDebut = new Label("Date début : " + dateFormat.format(evenement.getDateDebut()));
        add(dateDebut);
        Label dateFin = new Label("Date fin : " + dateFormat.format(evenement.getDateFin()));
        add(dateFin);


        // Créer un conteneur pour les boutons "Voir détails" et "Scanner"
        Container buttonContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));

        // Ajouter le bouton "Voir détails" pour l'événement
        Button voirDetails = new Button("Voir détails");
        voirDetails.addActionListener(e -> new EvenementDetailsForm(this, evenement.getId()).show());
        buttonContainer.add(voirDetails);


        add(buttonContainer);

    }


    private void addSeparator() {
        // Ajouter un séparateur horizontal
        Container separator = new Container();
        separator.getStyle().setBgColor(0xaaaaaa);
        separator.getStyle().setBgTransparency(255);
        separator.setPreferredSize(new Dimension(getWidth(), 1));
        addComponent(separator);
    }


}
