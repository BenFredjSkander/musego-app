package tn.musego.app.gui;

import com.codename1.components.ImageViewer;
import com.codename1.io.ConnectionRequest;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import tn.musego.app.entities.Offre;
import tn.musego.app.services.ServiceOffre;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ListOffres extends Form {
    public ListOffres(Form previous) {
        setTitle("Nos offres");
        setLayout(BoxLayout.y());

        ArrayList<Offre> offres = ServiceOffre.getInstance().getAllOffres();
        if (!offres.isEmpty()) {
            for (Offre o : offres) {
                add(addElement(o));
                add(new Label("___________________________________________________"));
            }
        } else add(new Label("Pas d'offres disponibles pour le moment"));

        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

    }

    public Container addElement(Offre offre) {

        Container cn1 = new Container(BoxLayout.y());
        Container cn2 = new Container(BoxLayout.y());

        Container cn3 = new Container(BoxLayout.x());
        Container cn4 = new Container(BoxLayout.x());
        Label lbTitle = new Label("Offre " + offre.getType());
        Label lbPrix = new Label("Prix: " + offre.getPrix() + "€");
        Label lbPromotion = new Label("Promotion: " + offre.getPromotion() + "%");

        Button btnsub = new Button("S'abonner");
        Button btnplus = new Button("Description");

        btnplus.addActionListener(e -> {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Dialog.show("Détails Offre " + offre.getType(), offre.getDescription() + "\nDate début: " + formatter.format(offre.getDateDebut()) + "\nDate fin: " + formatter.format(offre.getDateFin()), "Fermer", null);
        });
        btnsub.addActionListener(e -> {
            new PaymentForm(this, offre).show();
        });

        cn3.addAll(btnplus, btnsub);
        EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(this.getWidth() / 3, this.getWidth() / 3, 0xffff0000), true);

        ImageViewer imgv;
        String url = offre.getImage();

        if (url != null) {
            ConnectionRequest.setDefaultUserAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0");
            URLImage background = URLImage.createToStorage(placeholder, url, url, URLImage.RESIZE_SCALE_TO_FILL);

            imgv = new ImageViewer(background);
            cn2.addAll(lbTitle, lbPrix, lbPromotion);
            cn4.addAll(imgv, cn2);
            cn1.addAll(cn4, cn3);
        } else {
            cn2.addAll(lbTitle, lbPrix, lbPromotion);
            cn1.addAll(cn2, cn3);
        }

        return cn1;
    }
}
