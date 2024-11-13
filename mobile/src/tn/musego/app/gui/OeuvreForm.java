/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.musego.app.gui;


import com.codename1.components.SpanLabel;
import com.codename1.l10n.DateFormat;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import tn.musego.app.entities.Artiste;
import tn.musego.app.entities.Oeuvre;
import tn.musego.app.services.OeuvreService;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Salma Turki
 */
public class OeuvreForm extends Form {

    OeuvreService oeuvreService = OeuvreService.getInstance();
    private ArrayList<Oeuvre> oeuvresByCategorie;
    EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(100, 100, 0xffff0000), true);

    Container container = new Container(new BorderLayout());

//    public OeuvreForm(Form previous) {
//        setTitle("Liste des oeuvres");
//        setLayout(BoxLayout.y());
//
//        ArrayList<Oeuvre> oeuvres = oeuvreService.getInstance().getAllOeuvres();
//        for (Oeuvre t : oeuvres) {
//            addElement(t);
//            System.out.println("fffffffffffffffffffffffffffffffffffffffffffffff");
//        }
//
//        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
////
//
//    }

    public OeuvreForm(Form previous, ArrayList<Oeuvre> oeuvres, int categorieId, String categorieDescription) throws WriterException {
        setTitle("Liste des oeuvres");


        // Create a container to hold the description and set its layout to center alignment
        Container descriptionContainer = new Container(new FlowLayout(Component.CENTER));
        Label descriptionTitle = new Label("Description:");
        descriptionTitle.setUIID("DescriptionTitle"); // Set a custom UIID for the label
        descriptionContainer.add(descriptionTitle);
        SpanLabel descriptionLabel = new SpanLabel(categorieDescription);
        descriptionLabel.setUIID("DescriptionLabel"); // Set a custom UIID for the label
        descriptionLabel.setScrollableY(true); // Enable vertical scrolling for the label
// descriptionLabel.setPreferredH(150); // Set a fixed height for the label
        descriptionContainer.add(descriptionLabel);
        add(descriptionContainer);


        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        //  Button trierBtn = new Button("Trier par nom");
// Create a container to hold the button and set its layout to center alignment
        Container buttonContainer = new Container(new FlowLayout(Component.CENTER));
        Button trierBtn = new Button("Trier par nom");
        buttonContainer.add(trierBtn);
        add(buttonContainer);


        ArrayList<Oeuvre> oeuvresByCategorie = OeuvreService.getInstance().getAllOeuvresByCategorie(categorieId);
        trierBtn.addActionListener(e -> {
            try {
                trierParNom(categorieId, categorieDescription);
            } catch (WriterException ex) {
                throw new RuntimeException(ex);
            }
        });

        for (Oeuvre o : oeuvresByCategorie) {
            // Create a container to hold the label and image
            Container container = new Container(new BorderLayout());
            Artiste artiste = o.getArtiste();
            // Create the label and add it to the container
            Label label = new Label(o.getTitre());
            label.setUIID("CategoryLabel"); // Set a custom UIID for the label
            label.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
            label.setAlignment(CENTER);
            container.addComponent(BorderLayout.NORTH, label); // Place the label at the top of the container

            // Create the image label and add it to the container
            Label imageLabel = new Label();
            imageLabel.setUIID("CategoryImage"); // Set a custom UIID for the label
            String imageName = o.getImage();
            if (imageName != null && !imageName.isEmpty()) {
                String imageUrl = o.getImage();
                EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage((int) (Display.getInstance().getDisplayWidth() / 1.5), Display.getInstance().getDisplayHeight() / 2, 0xffff0000), true);
                EncodedImage image = URLImage.createToStorage(placeholder, "image_" + System.currentTimeMillis(), imageUrl, URLImage.RESIZE_SCALE);
                imageLabel.setIcon(image);

                // Center the image label within the container
                Container imageContainer = new Container(new FlowLayout(Component.CENTER));
                imageContainer.add(imageLabel);
                container.addComponent(BorderLayout.CENTER, imageContainer); // Place the image container at the center of the container
            }

            // Create a container to hold the description and date of birth
            Container infoContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));

            // Add the location label to the container
            Label lieuLabel = new Label("Lieu: " + o.getLieuConservation());
            lieuLabel.setUIID("LieuLabel"); // Set a custom UIID for the label
            lieuLabel.getStyle().setMarginLeft(15);

            infoContainer.addComponent(lieuLabel);


            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            // Add the location label to the container
            Label dateLabel = new Label("Date de creation: " + dateFormat.format(o.getDateCreation()));
            dateLabel.setUIID("dateLabel"); // Set a custom UIID for the label
            dateLabel.getStyle().setMarginLeft(15);

            infoContainer.addComponent(dateLabel);
            // Add the artist label to the container if it exists
            if (artiste != null) {
                Label artisteLabel = new Label("Artiste: " + artiste.getNom() + ' ' + artiste.getPrenom());
                artisteLabel.setUIID("ArtisteLabel");
                artisteLabel.getStyle().setMarginLeft(15);

                infoContainer.addComponent(artisteLabel);
            }

            // Add the info container to the main container
            container.addComponent(BorderLayout.SOUTH, infoContainer);

            // Create a container to hold the button container and QR code container
            Container oeuvreContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            oeuvreContainer.addComponent(container); // Add the button container to the oeuvre container

            // Generate the QR code image for the current oeuvre
            QRCodeWriter qrWriter = new QRCodeWriter();
            BitMatrix qrMatrix = qrWriter.encode(o.getDescription(), BarcodeFormat.QR_CODE, Display.getInstance().getDisplayWidth() / 3, Display.getInstance().getDisplayWidth() / 3);
            Image qrImage = toImage(qrMatrix);
            System.out.println("QR code image generated for " + o.getDescription());
            System.out.println("eeeeeeeeeeeeeeeeeeeeeeee" + qrImage);
            // Create a label to display the QR code image
            Label qrLabel = new Label();
            qrLabel.setIcon(qrImage);
            qrLabel.addPointerPressedListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    // Create a dialog to display the description
                    Dialog dialog = new Dialog();
                    dialog.setLayout(new BorderLayout());
                    dialog.setTitle(o.getTitre());

                    TextArea descriptionTextArea = new TextArea(o.getDescription());
                    descriptionTextArea.setEditable(false);
                    descriptionTextArea.setUIID("DialogBody");
                    dialog.addComponent(BorderLayout.CENTER, descriptionTextArea);

                    Button closeButton = new Button("Fermer");
                    closeButton.addActionListener(e -> dialog.dispose());
                    dialog.addComponent(BorderLayout.SOUTH, closeButton);

                    dialog.show();
                }
            });


            Container qrContainer = new Container(new FlowLayout(Component.CENTER));
            qrContainer.add(qrLabel);
            oeuvreContainer.addComponent(qrContainer); // Add the QR code container to the oeuvre container

            // Add the oeuvre container to the form
            add(oeuvreContainer);

        }


        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());


    }

    // Method to convert a BitMatrix to an Image
    private Image toImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
            }
        }

        Image image = Image.createImage(pixels, width, height);
        return image;

    }

    private void trierParNom(int id, String categorieDescription) throws WriterException {


        oeuvresByCategorie = OeuvreService.getInstance().getAllOeuvresByCategorie(id);
        Collections.sort(oeuvresByCategorie, (o1, o2) -> o1.getTitre().compareToIgnoreCase(o2.getTitre()));
        // Remove all the previous components from the form
        removeAll();
        // Create a container to hold the description and set its layout to center alignment
        Container descriptionContainer = new Container(new FlowLayout(Component.CENTER));
        Label descriptionTitle = new Label("Description:");
        descriptionTitle.setUIID("DescriptionTitle"); // Set a custom UIID for the label
        descriptionContainer.add(descriptionTitle);
        SpanLabel descriptionLabel = new SpanLabel(categorieDescription);
        descriptionLabel.setUIID("DescriptionLabel"); // Set a custom UIID for the label
        descriptionLabel.setScrollableY(true); // Enable vertical scrolling for the label
// descriptionLabel.setPreferredH(150); // Set a fixed height for the label
        descriptionContainer.add(descriptionLabel);
        add(descriptionContainer);

        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        //  Button trierBtn = new Button("Trier par nom");
// Create a container to hold the button and set its layout to center alignment
        Container buttonContainer = new Container(new FlowLayout(Component.CENTER));
        // Re-add the sorted oeuvres to the form
        for (Oeuvre o : oeuvresByCategorie) {
            // Create a container to hold the label and image
            Container container = new Container(new BorderLayout());
            Artiste artiste = o.getArtiste();
            // Create the label and add it to the container
            Label label = new Label(o.getTitre());
            label.setUIID("CategoryLabel"); // Set a custom UIID for the label
            label.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
            label.setAlignment(CENTER);
            container.addComponent(BorderLayout.NORTH, label); // Place the label at the top of the container

            // Create the image label and add it to the container
            Label imageLabel = new Label();
            imageLabel.setUIID("CategoryImage"); // Set a custom UIID for the label
            String imageName = o.getImage();
            if (imageName != null && !imageName.isEmpty()) {
                String imageUrl = o.getImage();
                EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage((int) (Display.getInstance().getDisplayWidth() / 1.5), Display.getInstance().getDisplayHeight() / 2, 0xffff0000), true);
                EncodedImage image = URLImage.createToStorage(placeholder, "image_" + System.currentTimeMillis(), imageUrl, URLImage.RESIZE_SCALE);
                imageLabel.setIcon(image);

                // Center the image label within the container
                Container imageContainer = new Container(new FlowLayout(Component.CENTER));
                imageContainer.add(imageLabel);
                container.addComponent(BorderLayout.CENTER, imageContainer); // Place the image container at the center of the container
            }

            // Create a container to hold the description and date of birth
            Container infoContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));

            // Add the location label to the container
            Label lieuLabel = new Label("Lieu: " + o.getLieuConservation());
            lieuLabel.setUIID("LieuLabel"); // Set a custom UIID for the label
            lieuLabel.getStyle().setMarginLeft(15);

            infoContainer.addComponent(lieuLabel);

            // Add the artist label to the container if it exists
            if (artiste != null) {
                Label artisteLabel = new Label("Artiste: " + artiste.getNom() + ' ' + artiste.getPrenom());
                artisteLabel.setUIID("ArtisteLabel");
                artisteLabel.getStyle().setMarginLeft(15);

                infoContainer.addComponent(artisteLabel);
            }

            // Add the info container to the main container
            container.addComponent(BorderLayout.SOUTH, infoContainer);

            // Create a container to hold the button container and QR code container
            Container oeuvreContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            oeuvreContainer.addComponent(container); // Add the button container to the oeuvre container

            // Generate the QR code image for the current oeuvre
            QRCodeWriter qrWriter = new QRCodeWriter();
            BitMatrix qrMatrix = qrWriter.encode(o.getDescription(), BarcodeFormat.QR_CODE, Display.getInstance().getDisplayWidth() / 3, Display.getInstance().getDisplayWidth() / 3);
            Image qrImage = toImage(qrMatrix);
            System.out.println("QR code image generated for " + o.getDescription());
            System.out.println("eeeeeeeeeeeeeeeeeeeeeeee" + qrImage);
            // Create a label to display the QR code image
            Label qrLabel = new Label();
            qrLabel.setIcon(qrImage);
            qrLabel.addPointerPressedListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    // Create a dialog to display the description
                    Dialog dialog = new Dialog();
                    dialog.setLayout(new BorderLayout());
                    dialog.setTitle(o.getTitre());

                    TextArea descriptionTextArea = new TextArea(o.getDescription());
                    descriptionTextArea.setEditable(false);
                    descriptionTextArea.setUIID("DialogBody");
                    dialog.addComponent(BorderLayout.CENTER, descriptionTextArea);

                    Button closeButton = new Button("Fermer");
                    closeButton.addActionListener(e -> dialog.dispose());
                    dialog.addComponent(BorderLayout.SOUTH, closeButton);

                    dialog.show();
                }
            });


            Container qrContainer = new Container(new FlowLayout(Component.CENTER));
            qrContainer.add(qrLabel);
            oeuvreContainer.addComponent(qrContainer); // Add the QR code container to the oeuvre container

            // Add the oeuvre container to the form
            add(oeuvreContainer);

        }

        // Revalidate the form to update the layout
        revalidate();
    }


    private void updateUI(ArrayList<Oeuvre> oeuvres) {
        removeAll();
        for (Oeuvre o : oeuvres) {
            // Create a container to hold the button and image label
            Container container = new Container(new BorderLayout());

            // Create the button and add it to the container
            Button btn = new Button(o.getTitre());
            btn.setUIID("CategoryButton"); // Set a custom UIID for the button
            container.addComponent(BorderLayout.SOUTH, btn); // Place the button at the bottom of the container

            // Create the image label and add it to the container
            Label imageLabel = new Label();
            imageLabel.setUIID("CategoryImage"); // Set a custom UIID for the label
            String imageName = o.getImage();
            if (imageName != null && !imageName.isEmpty()) {
                String imageUrl = o.getImage();
                EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage((int) (Display.getInstance().getDisplayWidth() / 1.5), Display.getInstance().getDisplayHeight() / 2, 0xffff0000), true);
                EncodedImage image = URLImage.createToStorage(placeholder, "image_" + System.currentTimeMillis(), imageUrl, URLImage.RESIZE_SCALE);
                imageLabel.setIcon(image);

                // Center the image label within the container
                Container imageContainer = new Container(new FlowLayout(Component.CENTER));
                imageContainer.add(imageLabel);
                container.addComponent(BorderLayout.NORTH, imageContainer); // Place the image container at the top of the container
            }

            // Create a container to hold the button container and set its layout to center alignment
            Container centerContainer = new Container(new FlowLayout(Component.CENTER));
            centerContainer.add(container);

            // Add the center container to the form
            add(centerContainer);
        }
        revalidate();
    }


    public void addElement(Oeuvre oeuvre) {
        System.out.println("my imaaaaagggggeeee" + oeuvre.getImage());
        CheckBox cb = new CheckBox(oeuvre.getTitre());
        cb.setEnabled(false);
        if (!oeuvre.getDescription().isEmpty()) {
            cb.setSelected(true);
        }
        add(cb);

        // Create a label for the image
        Label imageLabel = new Label();
        imageLabel.setUIID("Container"); // Optional: Set a custom UIID for the label

        // Check if the category has an image
        String imageName = oeuvre.getImage();
        if (imageName != null && !imageName.isEmpty()) {
            // Load the image from storage and set it as the icon for the label
            EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(Display.getInstance().getDisplayWidth() / 3, Display.getInstance().getDisplayWidth() / 3, 0xffff0000), true);
            EncodedImage image = URLImage.createToStorage(placeholder, imageName, imageName, URLImage.RESIZE_SCALE);
            imageLabel.setIcon(image);
        }


        add(imageLabel);
    }

}