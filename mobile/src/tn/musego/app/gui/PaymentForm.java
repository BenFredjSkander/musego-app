package tn.musego.app.gui;

import com.codename1.components.ImageViewer;
import com.codename1.components.MultiButton;
import com.codename1.components.SpanLabel;
import com.codename1.io.ConnectionRequest;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.list.GenericListCellRenderer;
import com.codename1.ui.plaf.Style;
import tn.musego.app.entities.Abonnement;
import tn.musego.app.entities.Offre;
import tn.musego.app.services.ServiceAbonnement;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class PaymentForm extends Form {
    public PaymentForm(Form previous, Offre offre) {
        setTitle("Paiement");
        setLayout(BoxLayout.y());

        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_LEFT, ev -> {
            previous.showBack();
        });


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Label lbType = new Label("Type d'offre: " + offre.getType());
        Label lbPrix = new Label("Prix: " + (offre.getPrix()+5) + "€");
        Label lbPromotion = new Label("Promotion: " + offre.getPromotion() + "%");
        Label lbDateDebut = new Label("Date Début: " + formatter.format(offre.getDateDebut()));
        Label lbDateFin = new Label("Date Fin: " + formatter.format(offre.getDateFin()));
        Label lbDescription = new Label("Description: " + offre.getDescription());

        SpanLabel choixpay = new SpanLabel("Veuillez choisir une carte de paiement");

        ComboBox<Map<String, Object>> combopay = new ComboBox<>(
                createListPay("Visa", "tok_visa"),
                createListPay("Mastercard", "tok_mastercard"),
                createListPay("Amex", "tok_amex"));

        combopay.setRenderer(new GenericListCellRenderer<>(new MultiButton(), new MultiButton()));
        combopay.setSelectedIndex(0);

        SpanLabel choix = new SpanLabel("Veuillez choisir un type d'abonnement pour continuer");

        ComboBox<Map<String, Object>> combo = new ComboBox<>(
                createListEntry("Hebdomadaire", ((5+(offre.getPrix()) * 7) * (offre.getPromotion() / 100))),
                createListEntry("Mensuel", ((5+(offre.getPrix()) * 30) * (offre.getPromotion() / 100))),
                createListEntry("Annuel", ((5+(offre.getPrix()) * 365) * (offre.getPromotion() / 100))));

        combo.setRenderer(new GenericListCellRenderer<>(new MultiButton(), new MultiButton()));
        combo.setSelectedIndex(0);
        Button btnsub = new Button();
        Abonnement subexist = ServiceAbonnement.getInstance().getAbonnementByUserOffre(offre.getId());
        if (subexist == null) btnsub.setText("S'abonner");
        else {
            btnsub.setText("Vous êtes déjà abonné à cette offre!.");
            btnsub.setEnabled(false);
        }
        btnsub.addActionListener(e -> {
            if (combo.getSelectedItem() != null) {
                if (Dialog.show("Confirmation", "Voulez vous vous abonner à cette offre " + offre.getType() + '?', "Oui", "Non")) {
                    ServiceAbonnement.getInstance().stripePayment(combopay.getSelectedItem().get("Line2").toString(),combo.getSelectedItem().get("Line1").toString(), (Double) combo.getSelectedItem().get("Line2"));
                    ServiceAbonnement.getInstance().addAbonnement(offre.getId(), combo.getSelectedItem().get("Line1").toString(), (Double) combo.getSelectedItem().get("Line2"));
                    Dialog.show("Confirmation", "Félicitations!, vous êtes abonné à l'offre " + offre.getType(), "Oui", null);
                    previous.showBack();
                }
            } else Dialog.show("Echéc", "Veuillez sélectionner un type d'abonnement pour continuer ", "Ok", null);
        });
        //combobox start
        //combobox end
        //image start
        //EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(this.getWidth(), this.getWidth() / 5, 0xffff0000), true);

        Style s = new Style();
        s.setBgColor(0xa9a9a9);
        EncodedImage placeholder = EncodedImage.createFromImage(
                FontImage.createMaterial(FontImage.MATERIAL_SYNC, s).scaledHeight(Display.getInstance().getDisplayHeight()/3), false);

        ImageViewer imgv;
        String url = offre.getImage();

        if (url != null) {
            ConnectionRequest.setDefaultUserAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0");
            URLImage background = URLImage.createToStorage(placeholder, url, url, URLImage.RESIZE_SCALE_TO_FILL);

            //imgs = URLImage.createToStorage(placeholder, url, url, URLImage.RESIZE_SCALE);
            //premier url est le nom de l'image et le deuxieme est l'image elle même
            imgv = new ImageViewer(background);
            //image end

            if (subexist == null)
                addAll(lbType, imgv, lbDescription, lbPrix, lbPromotion, lbDateDebut, lbDateFin, choix, combo, choixpay, combopay, btnsub);
            else addAll(lbType, imgv, lbDescription, lbPrix, lbPromotion, lbDateDebut, lbDateFin, btnsub);
        } else {
            if (subexist == null)
                addAll(lbType, lbDescription, lbPrix, lbPromotion, lbDateDebut, lbDateFin, choix, combo, choixpay,combopay, btnsub);
            else addAll(lbType, lbDescription, lbPrix, lbPromotion, lbDateDebut, lbDateFin, btnsub);
        }


    }

    private Map<String, Object> createListPay(String card, String name) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("Line1", card);
        entry.put("Line2", name);
        return entry;
    }

    private Map<String, Object> createListEntry(String type, double prix) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("Line1", type);
        entry.put("Line2", prix);
        return entry;
    }
}
