package tn.musego.app.gui;

import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Util;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import tn.musego.app.entities.Abonnement;
import tn.musego.app.entities.Offre;
import tn.musego.app.services.ServiceAbonnement;
import tn.musego.app.services.ServiceOffre;
import tn.musego.app.utils.FileDownloadHelper;
import tn.musego.app.utils.Statics;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class MonAbonnement extends Form {
    public MonAbonnement(Form previous) {
        setTitle("Mon abonnement");
        setLayout(BoxLayout.y());
        setSafeArea(true);
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
        Abonnement abonnement = ServiceAbonnement.getInstance().getAbonnementByUser();
        if (abonnement != null) {
            Offre offre = ServiceOffre.getInstance().getOffre(abonnement.getIdoffre());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            Label lbType = new Label("Type d'abonnement: " + abonnement.getType());
            Label lbPrix = new Label("Prix abonnement: " + abonnement.getPrix() + "€");
            Label lbDateDebut = new Label("Date d'achat: " + formatter.format(abonnement.getDateDebut()));
            Label lbDateFin = new Label("Date d'expiration: " + formatter.format(abonnement.getDateFin()));
            Label lbOffreType = new Label("Sous l'offre: " + offre.getType());
            Label lbPromotion = new Label("Promotion de l'offre: " + offre.getPromotion() + "%");
            SpanLabel lbDescription = new SpanLabel("Description de l'offre: " + offre.getDescription());
            Button button = new Button("Télécharger la facture");

            button.addActionListener(e -> {
                ServiceAbonnement.getInstance().generatePDF(abonnement.getId());

                FileSystemStorage fs = FileSystemStorage.getInstance();
                String fileName = fs.getAppHomePath() + "Facture "+System.currentTimeMillis()+".pdf";
                FileDownloadHelper.downloadUrlTo(Statics.BASE_URL+"abonnements/pdf/"+abonnement.getId(),fileName,true,false);
                Display.getInstance().execute(fileName);
            });
            //image start
            EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(this.getWidth(), this.getWidth() / 2, 0xffff0000), true);

            ImageViewer imgv;
            String url = offre.getImage();

            if (url != null) {
                ConnectionRequest.setDefaultUserAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0");
                URLImage background = URLImage.createToStorage(placeholder, url, url, URLImage.RESIZE_SCALE_TO_FILL);

                //imgs = URLImage.createToStorage(placeholder, url, url, URLImage.RESIZE_SCALE);
                //premier url est le nom de l'image et le deuxieme est l'image elle même
                imgv = new ImageViewer(background);
                //image end

                addAll(lbType, imgv, lbPrix, lbDateDebut, lbDateFin, lbOffreType, lbPromotion, lbDescription, button);
            } else addAll(lbType, lbPrix, lbDateDebut, lbDateFin, lbOffreType, lbPromotion, lbDescription, button);

        } else {

            Label lbMsg = new Label("Vous n'êtes pas actuellement abonné(e) à une offre");
            Label lbMsg2 = new Label("Pour vous abonner, veuillez consulter nos offres");

            Button btnListOffres = new Button("List des offres");

            btnListOffres.addActionListener(e -> new ListOffres(this).show());

            addAll(lbMsg, lbMsg2, btnListOffres);
        }
    }
}
