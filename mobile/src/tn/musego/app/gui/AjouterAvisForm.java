package tn.musego.app.gui;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import tn.musego.app.entities.Avis;
import tn.musego.app.services.AvisService;

import java.util.Map;

public class AjouterAvisForm extends Form {

    public AjouterAvisForm(Form previous) {
        super();
        getToolbar().addMaterialCommandToLeftBar(null, FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
        setLayout(BoxLayout.y());
        // Create text fields and a combo box for the avis information
        TextField typeField = new TextField();
        typeField.setHint("Type");
        TextArea descriptionField = new TextArea();
        descriptionField.setHint("Description");

        ComboBox<String> combo = new ComboBox<> (
                "Evenement", "Atelier", "Oeuvre");
        combo.setHint("Avis sur");


        // Create a button to add the avis
        Button addBtn = new Button("Ajouter");
        addBtn.addActionListener(e -> {

            String type = typeField.getText().trim();
            String description = descriptionField.getText().trim();

            // Check that the entered values are valid
            if (type.isEmpty() || description.isEmpty() ) {
                Dialog.show("Error", "Un ou plusieurs champs sont requis", "OK", null);
                return;
            }

            // Create a new avis with the entered information
            Avis avis = new Avis();
            avis.setType(type);
            avis.setDescription(description);
            avis.setAvisSur(combo.getSelectedItem());

            // Add the avis to the server
            AvisService.getInstance().addAvis(avis);
            new ListeAvisForm(new HomeForm()).show();


        });

        // Add the text fields and combo box to the form
        add(combo);
        add(typeField);
        add(descriptionField);
        add(addBtn);
    }
}

