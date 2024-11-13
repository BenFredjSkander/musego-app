/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.musego.app.gui;

import com.codename1.components.SpanLabel;
import com.codename1.io.Preferences;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import tn.musego.app.MyApplication;
import tn.musego.app.entities.Abonnement;
import tn.musego.app.services.AuthService;
import tn.musego.app.services.ServiceAbonnement;
import tn.musego.app.utils.PreferencesCnst;

/**
 * @author bhk
 */
public class HomeForm extends Form {

    public HomeForm() {

        setTitle("Accueil");
        setSafeArea(true);

        setLayout(BoxLayout.y());

        setSafeArea(true);
//        Font materialFont = FontImage.getMaterialDesignFont();
//
//
//        FontImage.createFixed(String.valueOf(icon), materialFont, 0xff0000, w, w);
        this.getAllStyles().setBgImage(MyApplication.theme.getImage("bgm.jpg"));


        Abonnement abonnement = ServiceAbonnement.getInstance().getAbonnementByUser();
        Style iconStyle = new Style();
        iconStyle.setBgColor(0x000000, true);
        iconStyle.setFgColor(0xffffff, true);
        iconStyle.setPaddingUnit(Style.UNIT_TYPE_PIXELS);
        iconStyle.setPaddingRight(5);
        int iconSize = Display.getInstance().getDisplayWidth() / 12;
        getToolbar().addCommandToSideMenu("Profil", FontImage.createMaterial(FontImage.MATERIAL_ACCOUNT_CIRCLE, iconStyle).scaledHeight(iconSize), ev -> {
            new ProfileForm(this).show();
        });
        if (abonnement != null && (abonnement.getOffre().equals("Bronze") || abonnement.getOffre().equals("Silver") || abonnement.getOffre().equals("Gold"))) {
            getToolbar().addCommandToSideMenu("Oeuvres", FontImage.createMaterial(FontImage.MATERIAL_ART_TRACK, iconStyle).scaledHeight(iconSize), ev -> {
                new CategorieForm(this).show();
            });
            getToolbar().addCommandToSideMenu("Avis", FontImage.createMaterial(FontImage.MATERIAL_MESSAGE, iconStyle).scaledHeight(iconSize), ev -> {
                new ListeAvisForm(this).show();
            });
        }
        if (abonnement != null && (abonnement.getOffre().equals("Silver") || abonnement.getOffre().equals("Gold"))) {
            getToolbar().addCommandToSideMenu("Evenements", FontImage.createMaterial(FontImage.MATERIAL_EVENT, iconStyle).scaledHeight(iconSize), ev -> {
                new ListeEvenementsForm(this).show();
            });
        }
        if (abonnement != null && (abonnement.getOffre().equals("Gold"))) {
            getToolbar().addCommandToSideMenu("Ateliers", FontImage.createMaterial(FontImage.MATERIAL_SCHOOL, iconStyle).scaledHeight(iconSize), ev -> {
                new ListAteliers(this).show();
            });
        }
        getToolbar().addCommandToSideMenu("Abonnement", FontImage.createMaterial(FontImage.MATERIAL_CARD_MEMBERSHIP, iconStyle).scaledHeight(iconSize), ev -> {
            new AbonnementForm(this).show();
        });
        if (Preferences.get(PreferencesCnst.Preferences_TOKEN, null) != null) {
            getToolbar().addCommandToSideMenu("Se déconnecter", FontImage.createMaterial(FontImage.MATERIAL_LOGOUT, iconStyle).scaledHeight(iconSize), ev ->
                    AuthService.getInstance().logout()
            );

        }

//        ImageViewer image = new ImageViewer(MyApplication.theme.getImage("france-paris-louvre-museum-gold.png"));
        add(MyApplication.theme.getImage("france-paris-louvre-museum-gold.png").scaledWidth(Display.getInstance().getDisplayWidth()));

        // Créer une police en gras
        Font boldFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);

// Ajouter des slogans et une description
        Label slogan1 = new Label("Découvrez l'art et la culture");
        slogan1.getStyle().setFont(boldFont);

        Label slogan2 = new Label("du monde entier ...");
        slogan2.getStyle().setFont(boldFont);

        SpanLabel description = new SpanLabel("Bienvenue sur Musego, votre musée virtuel où vous pouvez explorer des œuvres d'art et des expositions fascinantes. Plongez dans l'histoire et l'esthétique de différentes cultures à travers le temps.");

        description.getAllStyles().setFont(boldFont);

        addAll(slogan1, slogan2, description);


        Label contactLabel = new Label("Contactez-nous : contact.musego@gmail.com");
        add(contactLabel);

        Button facebookButton = new Button(null, MyApplication.theme.getImage("facebook.png").scaled(200, 200));
        facebookButton.setHeight(200);

        facebookButton.setUIID("SocialButton");
        facebookButton.addActionListener(evt -> new WebViewForm(this, "https://www.facebook.com/profile.php?id=100090616883418"));

//        addAll(facebookButton);
    }

//    private FontImage createIcon(char icon){
//        FontImage.createFixed(String.valueOf(icon), materialFont, 0xff0000, w, w);
//    }


}
