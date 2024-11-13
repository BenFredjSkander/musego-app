package tn.musego.app.gui;

import com.codename1.components.FloatingActionButton;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BoxLayout;
import tn.musego.app.entities.Avis;
import tn.musego.app.services.AvisService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ListeAvisForm extends Form {
    private final Set<Long> addedAvisIds = new HashSet<>();

    public ListeAvisForm(Form previous) {
        this.setTitle("Mes avis");

        ArrayList<Avis> avis = AvisService.getInstance().getAllAvis();


        this.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        for (Avis a : avis) {
            addElement(a);
        }
//        Button ajouter = new Button("ajouter");
//        ajouter.addActionListener(e -> {
//            new AjouterAvisForm(this).show();
//
//        });
//        add(ajouter);

        FloatingActionButton fab = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
        fab.addActionListener(e -> new AjouterAvisForm(this).show());
        fab.bindFabToContainer(this);


        this.show();
        if (previous != null) {
            this.getToolbar().addMaterialCommandToLeftBar(null, FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
        }
    }


    public void addElement(Avis avis) {
        // Vérifier si l'avis a déjà été ajouté
        if (addedAvisIds.contains(avis.getId())) {
            return;
        }

        // Ajouter un séparateur avant l'avis, sauf pour le premier
        if (addedAvisIds.size() > 0) {
            addSeparator();
        }

        // Ajouter le type de l'avis en gras
        Label type = new Label(avis.getType());
        type.getStyle().setFgColor(0x000000);
        type.getStyle().setBgColor(0xffffff);
        type.getStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
        add(type);

        // Ajouter les autres détails de l'avis
        SpanLabel description = new SpanLabel("Description : " + avis.getDescription());
        add(description);
        Label avisSur = new Label("Avis sur : " + avis.getAvisSur());
        add(avisSur);

        // Créer un conteneur pour les boutons "Voir détails" et "Supprimer"
        Container buttonContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));

        // Ajouter le bouton "Modifier" pour l'avis
        Button modifier = new Button("Modifier");
        modifier.addActionListener(e -> new EditAvisForm(avis, this).show());
        buttonContainer.add(modifier);

        // Ajouter le bouton "Supprimer" pour l'avis
        Button supprimer = new Button("Supprimer");
        supprimer.addActionListener(e -> {
            AvisService.getInstance().deleteAvis(avis.getId());
            //  this.remove(avis);
//            show();
            new ListeAvisForm(new HomeForm()).show();
        });
        buttonContainer.add(supprimer);

        add(buttonContainer);

        // Ajouter l'identifiant de l'avis à la liste des avis déjà ajoutés
        addedAvisIds.add(avis.getId());
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
