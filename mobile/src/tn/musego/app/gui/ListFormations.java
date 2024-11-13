package tn.musego.app.gui;

import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.io.ConnectionRequest;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import tn.musego.app.entities.Formation;
import tn.musego.app.services.ServiceFormation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListFormations extends Form {


    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


    public ListFormations(Form previous, String TitleAtelier, int id,String img) {
        setTitle("Atelier " + TitleAtelier);
        setLayout(BoxLayout.y());

        ArrayList<Formation> formations = ServiceFormation.getInstance().getAllFormations(id);
        Style s = new Style();
        s.setBgColor(0xa9a9a9);
        ImageViewer imgv;
        EncodedImage placeholder = EncodedImage.createFromImage(FontImage.createMaterial(FontImage.MATERIAL_SYNC, s).scaled(this.getWidth() / 3, this.getWidth() / 5), false);

        if (img != null) {
            ConnectionRequest.setDefaultUserAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0");
            URLImage background = URLImage.createToStorage(placeholder, "image_" + System.currentTimeMillis(), img, URLImage.RESIZE_SCALE_TO_FILL);

            imgv = new ImageViewer(background);
            add(imgv);
        }
        SpanLabel spanLabel=new SpanLabel();
        if (!formations.isEmpty()) {
            spanLabel.setText("Merci d'avoir choisi l'un de nos ateliers. Veuillez maintenant séléctionner une des formations disponibles.");
            add(spanLabel);
            add(new Label("___________________________________________________"));
            for (Formation o : formations) {
                add(addElement(o));
                add(new Label("___________________________________________________"));
            }
        } else {
            spanLabel.setText("Aucune formations n'est disponible, revenez ultérieurement.");

            add(spanLabel);

            add(new Label("___________________________________________________"));
        }


        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

    }

    public Container addElement(Formation formation) {

        Container cn1 = new Container(BoxLayout.x());
        Container cn2 = new Container(BoxLayout.y());


        Label lbNom = new Label("Nom: "+formation.getNom());
        Label lbDebut = new Label("Début: "+formatter.format(formation.getDateDebut()));
        Label lbFin = new Label("Fin: "+formatter.format(formation.getDateFin()));

        cn2.addAll(lbNom,lbDebut,lbFin);

        Button button = new Button("Voir détails");
        button.addActionListener(e -> {
            new FormationDetails(this, formation).show();
        });

        cn1.addAll(cn2,button);


        return cn1;
    }
}