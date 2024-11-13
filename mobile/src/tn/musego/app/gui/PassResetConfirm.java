package tn.musego.app.gui;

import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.Validator;
import tn.musego.app.services.AuthService;

/**
 * @author Skander Ben Fredj
 * @created 5/9/2023
 * @project pi-3a-mobile
 */

public class PassResetConfirm extends Form {
    public PassResetConfirm(String email) {
        super();
        setLayout(BoxLayout.y());
        setSafeArea(true);
        SpanLabel label = new SpanLabel("Veuillez taper le nouveau mot de passe pour le compte de " + email);

        TextComponent password = new TextComponentPassword()
                .labelAndHint("Password").columns(10);
        TextComponent password2 = new TextComponentPassword()
                .labelAndHint("Password").columns(10);
        Button reset = new Button("Confirmer");

        Validator val = new Validator();
        val.setValidationFailureHighlightMode(Validator.HighlightMode.UIID_AND_EMBLEM);
        val.addSubmitButtons(reset)
                .addConstraint(password, new LengthConstraint(6))
                .addConstraint(password2, new LengthConstraint(6));

        reset.addActionListener(evt -> {
            if (!password2.getText().equals(password.getText())) {
                Dialog.show("Erreur", "Les mots de pass ne sont pas identiques", "OK", null);
            } else {
                AuthService.getInstance().resetConfirm(email, password.getText());
            }
        });
        addAll(label, password, password2, reset);
    }
}
