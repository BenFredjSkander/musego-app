package tn.musego.app.gui;

import com.codename1.ui.*;
import tn.musego.app.entities.Avis;
import tn.musego.app.services.AvisService;

public class EditAvisForm extends Form {

    public EditAvisForm(Avis avis, Form previous) {
        super("Edit Avis");
        getToolbar().addMaterialCommandToLeftBar(null, FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

        // Create text fields and a combo box for the avis information
        TextField typeField = new TextField(avis.getType(), "Type");
        TextField descriptionField = new TextField(avis.getDescription(), "Description");
        TextField avisSurField = new TextField(avis.getAvisSur(), "Avis Sur");

        // Create a button to save the changes
        Button saveBtn = new Button("Save");
        saveBtn.addActionListener(e -> {
            // Validate the entered values

            String type = typeField.getText().trim();
            String description = descriptionField.getText().trim();
            String avisSur = avisSurField.getText().trim();

            // Check that the entered values are valid
            if (type.isEmpty() || description.isEmpty() || avisSur.isEmpty()) {
                Dialog.show("Error", "Please enter valid values for all fields", "OK", null);
                return;
            }

            // Update the avis with the entered information
            avis.setType(type);
            avis.setDescription(description);
            avis.setAvisSur(avisSur);

            // Update the avis on the server
            AvisService.getInstance().editAvis(avis);

            // Close the form
            new ListeAvisForm(null).show();
            //this.close();


        });

        // Add the text fields and button to the form
        add(typeField);
        add(descriptionField);
        add(avisSurField);
        add(saveBtn);
    }
}
