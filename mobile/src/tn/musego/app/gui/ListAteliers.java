package tn.musego.app.gui;

import com.codename1.components.ImageViewer;
import com.codename1.io.ConnectionRequest;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import tn.musego.app.entities.Atelier;
import tn.musego.app.services.ServiceAtelier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListAteliers extends Form {

    public ListAteliers(Form previous) {
        setTitle("List ateliers");
        setLayout(BoxLayout.y());

        ArrayList<Atelier> ateliers = ServiceAtelier.getInstance().getAllAteliers();
        for (Atelier o : ateliers) {
            add(addElement(o));
            add(new Label("___________________________________________________"));
        }
        getToolbar().addMaterialCommandToLeftBar(null, FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public Container addElement(Atelier atelier) {

        Container cn1 = new Container(BoxLayout.x());
        Container cn2 = new Container(BoxLayout.y());

        Label lbNom = new Label(atelier.getNom());
//        Label lbDateCreation = new Label(atelier.getCreatedAt().toString());
        Style s = new Style();
        s.setBgColor(0xa9a9a9);
        ImageViewer imgv;
        EncodedImage placeholder = EncodedImage.createFromImage(FontImage.createMaterial(FontImage.MATERIAL_SYNC, s).scaled(this.getWidth() / 3, this.getWidth() / 5), false);

        Button btnplus = new Button("Voir formations");

        btnplus.addActionListener(e -> {
            new ListFormations(this, atelier.getNom(), atelier.getId(),atelier.getImage()).show();
        });

        String url = atelier.getImage();

        if (url != null) {
            ConnectionRequest.setDefaultUserAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0");
            URLImage background = URLImage.createToStorage(placeholder, "image_" + System.currentTimeMillis(), url, URLImage.RESIZE_SCALE_TO_FILL);

            imgv = new ImageViewer(background);
            cn1.addAll(imgv, lbNom);
            cn2.addAll(cn1,btnplus);
        } else {
            cn1.add(lbNom);
            cn2.addAll(cn1,btnplus);
        }


        return cn2;
    }


}