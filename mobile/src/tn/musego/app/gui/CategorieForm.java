/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.musego.app.gui;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.google.zxing.WriterException;
import tn.musego.app.entities.Categorie;
import tn.musego.app.entities.Oeuvre;
import tn.musego.app.services.CategorieService;
import tn.musego.app.services.OeuvreService;

import java.util.ArrayList;

/**
 * @author Salma Turki
 */
public class CategorieForm extends Form {

    CategorieService categorieService = CategorieService.getInstance();
    EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(100, 100, 0xffff0000), true);
    Container container = new Container(new BorderLayout());

    /* public CategorieForm(Form previous) {
        setTitle("List categories");
        setLayout(BoxLayout.y());

        ArrayList<Categorie> categories = categorieService.getInstance().getAllCategories();
        for (Categorie t : categories) {
            addElement(t);
            System.out.println("fffffffffffffffffffffffffffffffffffffffffffffff");
        }

        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

    }
*/
    public CategorieForm(Form previous) {

        setTitle("Liste des catégories");
        setLayout(new BoxLayout(BoxLayout.Y_AXIS)); // Set the form layout to a vertical BoxLayout with center alignment

        // rest of the code

        ArrayList<Categorie> categories = CategorieService.getInstance().getAllCategories();
        for (Categorie c : categories) {
            // Create a container to hold the button and image label
            Container container = new Container(new BorderLayout());

            // Create the button and add it to the container
            Button btn = new Button(c.getNom());
            btn.setUIID("CategoryButton"); // Set a custom UIID for the button
            btn.addActionListener(e -> {
                // Récupérer toutes les oeuvres de cette catégorie
                ArrayList<Oeuvre> oeuvres = OeuvreService.getInstance().getAllOeuvresByCategorie(c.getId());
// Récupérer la description de la catégorie
                String categorieDescription = c.getDescription();

                // Afficher les oeuvres dans la forme OeuvreForm
                try {

                    new OeuvreForm(this, oeuvres, c.getId(), categorieDescription).show();

                } catch (WriterException ex) {
                    throw new RuntimeException(ex);
                }
            });
            container.addComponent(BorderLayout.SOUTH, btn); // Place the button at the bottom of the container
            // Create the image label and add it to the container
            Label imageLabel = new Label();
            imageLabel.setUIID("CategoryImage"); // Set a custom UIID for the label
            String imageName = c.getImage();
            if (imageName != null && !imageName.isEmpty()) {
                String imageUrl = c.getImage();
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

        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }


    public void addElement(Categorie categorie) {
        CheckBox cb = new CheckBox(categorie.getNom());
        cb.setEnabled(false);
        if (!categorie.getDescription().isEmpty()) {
            cb.setSelected(true);
        }
        add(cb);

        // Create a label for the image
        Label imageLabel = new Label();
        imageLabel.setUIID("CategoryImage"); // Set a custom UIID for the label

        // Check if the category has an image
        String imageName = categorie.getImage();
        if (imageName != null && !imageName.isEmpty()) {
            // Load the image from the URL and set it as the icon for the label
            String imageUrl = categorie.getImage();
            EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(Display.getInstance().getDisplayWidth() / 2, Display.getInstance().getDisplayHeight() / 2, 0xffff0000), true);
            EncodedImage image = URLImage.createToStorage(placeholder, "image_" + System.currentTimeMillis(), imageUrl, URLImage.RESIZE_SCALE);
            imageLabel.setIcon(image);
        }

        add(imageLabel);
    }

}
/*
public CategorieForm(Form previous) {
    setTitle("Liste des catégories");
    setLayout(BoxLayout.y());

    ArrayList<Categorie> categories = CategorieService.getInstance().getAllCategories();
    for (Categorie c : categories) {
        Container container = new Container(new BorderLayout());

        Label imageLabel = new Label();
        String imageName = c.getImage();
        if (imageName != null && !imageName.isEmpty()) {
            String imageUrl = c.getImage() ;
            EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(getWidth(), getWidth()), true);
            EncodedImage image = URLImage.createToStorage(placeholder, "image_" + System.currentTimeMillis(), imageUrl, URLImage.RESIZE_SCALE_TO_FILL);
            imageLabel.setIcon(image.scaled(getWidth() - 50, getWidth() - 50));
            container.addComponent(BorderLayout.CENTER, imageLabel);
        }

        Button btn = new Button(c.getNom());
        btn.addActionListener(e -> {
            ArrayList<Oeuvre> oeuvres = OeuvreService.getInstance().getAllOeuvresByCategorie(c.getId());
            new OeuvreForm(this, oeuvres).show();
        });
        btn.getAllStyles().setPadding(0, 0, 0, 0);
        btn.getAllStyles().setAlignment(Component.CENTER);
        container.addComponent(BorderLayout.SOUTH, btn);

        add(container);
    }

    getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
}


public void addElement(Categorie categorie) {
    CheckBox cb = new CheckBox(categorie.getNom());
    cb.setEnabled(false);
    if (!categorie.getDescription().isEmpty()) {
        cb.setSelected(true);
    }
    add(cb);

    Label imageLabel = new Label();
    
    add(imageLabel);
}
*/