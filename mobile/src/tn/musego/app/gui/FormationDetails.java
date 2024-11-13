package tn.musego.app.gui;

import com.codename1.l10n.DateFormat;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import tn.musego.app.entities.Formation;
import tn.musego.app.services.ServiceFormation;

public class FormationDetails extends Form {
    private boolean response;
    private boolean isParticipated;

    public FormationDetails(Form previous, Formation formation) {

        setTitle("Détails ");
        setLayout(BoxLayout.y());

        isParticipated = ServiceFormation.getInstance().isParticipated(formation.getId());
        //
        if (isParticipated)
            add(showFormation(formation, "Annuler", "Désirez-Vous Annuler votre Participation à la Formation "));
        else add(showFormation(formation, "Participer", "Confirmez votre participation à la Formation "));


        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public Container showFormation(Formation formation, String action, String message) {
        Container container = new Container(BoxLayout.y());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Label lbNom = new Label("nom: " + formation.getNom());
        Label lbNiveau = new Label("niveau: " + formation.getNiveau());
        Label lbDateDebut = new Label("Début: " + dateFormat.format(formation.getDateDebut()));
        Label lbDateFin = new Label("Fin: " + dateFormat.format(formation.getDateFin()));

        Label lbFormatteur = new Label("Nom Formatteur: " + formation.getNomFormateur());
        Button button = new Button(action);
        button.addActionListener(e -> {
            if (Dialog.show("Confirmation", message + formation.getNom() + " ?", "Oui", "Non")) {
                response = ServiceFormation.getInstance().participer(formation.getId());
                System.out.println("response is ::" + response);
                if (response) {
                    //refresh(formation,"Annuler");
                    button.setText("Annuler");
                } else {
                    //refresh(formation,"Participer");
                    button.setText("Participer");
                }
            }
        });


        container.addAll(lbNom, lbNiveau, lbDateDebut, lbDateFin,lbFormatteur, button);

        return container;
    }
}