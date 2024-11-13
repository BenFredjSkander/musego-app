package tn.musego.app.gui;

import com.codename1.components.ImageViewer;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.SpanLabel;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.io.Preferences;
import com.codename1.l10n.DateFormat;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import tn.musego.app.entities.Evenement;
import tn.musego.app.services.ServiceEvenement;
import tn.musego.app.utils.HTTPMethod;
import tn.musego.app.utils.HttpStatusCode;
import tn.musego.app.utils.PreferencesCnst;
import tn.musego.app.utils.Statics;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class EvenementDetailsForm extends Form {


    public EvenementDetailsForm(Form previous, int id) {
        super();
        setLayout(BoxLayout.y());
        setSafeArea(true);
        this.setTitle("Détails de l'événement");
        this.getToolbar().addMaterialCommandToLeftBar(null, FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

        Evenement evenement = ServiceEvenement.getInstance().getEvenement(id);

        Label nomLabel = new Label("Nom : " + evenement.getNom());
        Label lieuLabel = new Label("Lieu : " + evenement.getLieu());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Label dateDebutLabel = new Label("Date début : " + dateFormat.format(evenement.getDateDebut()));


        Label dateFinLabel = new Label("Date fin : " + dateFormat.format(evenement.getDateFin()));

        Label typeLabel = new Label("Type : " + evenement.getType());
        Style s = new Style();
        s.setBgColor(0xa9a9a9);
        EncodedImage placeholder = EncodedImage.createFromImage(
                FontImage.createMaterial(FontImage.MATERIAL_SYNC, s).scaledHeight(Display.getInstance().getDisplayHeight() / 3), false);

        URLImage urlImage = URLImage.createToStorage(placeholder, "poster_" + evenement.getId(), evenement.getPoster());
        ImageViewer posterImageViewer = new ImageViewer(urlImage);

        add(posterImageViewer);


        Label descriptionTitle = new Label("Description :");
        descriptionTitle.getStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));

        SpanLabel descriptionTextArea = new SpanLabel(evenement.getDescription());


        Button voirSurMap = new Button("Voir sur map");
        voirSurMap.addActionListener(e -> new MapForm(this));


        add(nomLabel);
        add(lieuLabel);
        add(dateDebutLabel);
        add(dateFinLabel);
        add(typeLabel);
        add(voirSurMap);

        System.out.println(evenement);
        Button participationButton;
        participationButton = new Button(evenement.getParticipated() ? "Annuler" : "Participer");
        participationButton.addActionListener(e -> {
            long eventId = evenement.getId();

            String url = Statics.BASE_URL + "evenements/participer/" + eventId;

            ConnectionRequest request = new ConnectionRequest();
            request.setUrl(url);
            request.setHttpMethod(HTTPMethod.POST.name());
            request.setContentType("application/json");
            request.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
            Dialog ip = new InfiniteProgress().showInfiniteBlocking();

            request.addResponseListener(ev -> {

                try {
                    Map<String, Object> apiReponseMap = new JSONParser().parseJSON(new InputStreamReader(new ByteArrayInputStream(request.getResponseData()), "UTF-8"));
                    String message = (String) apiReponseMap.get("message");
                    if (request.getResponseCode() == HttpStatusCode.CREATED.value()) {
                        participationButton.setText("Annuler");
                    } else {
                        participationButton.setText("Participer");
                    }
                    ip.dispose();
                    Dialog.show("Confirmation", message, "OK", null);


                } catch (IOException ex) {
                    System.out.println(ex);
                } finally {
                    ip.dispose();
                }
                // Vérifier la réponse de l'API


            });

            NetworkManager.getInstance().addToQueue(request);
        });
//        if (new Date().before(evenement.getDateDebut())) {
        add(participationButton);
//        }

        add(descriptionTitle);
        add(descriptionTextArea);

//        Util.downloadUrlToFile("http://www.polyu.edu.hk/iaee/files/pdf-sample.pdf", fileName, true);
    }


}




















