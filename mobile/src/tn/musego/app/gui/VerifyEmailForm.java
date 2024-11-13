package tn.musego.app.gui;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.TextComponent;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.Validator;
import tn.musego.app.services.AuthService;
import tn.musego.app.utils.CustomComponent;

/**
 * @author Skander Ben Fredj
 * @created 5/6/2023
 * @project pi-3a-mobile
 */

public class VerifyEmailForm extends Form {
    public VerifyEmailForm(Form previous, String email) {
        super();
        setLayout(BoxLayout.y());
        setSafeArea(true);
        getToolbar().addMaterialCommandToLeftBar(null, FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
        SpanLabel text = new SpanLabel("Veillez entre le code envoyé à l'adresse: " + email);

        TextComponent code = new TextComponent()
                .labelAndHint("Code");

        Button verifButton = new Button("Vérifier mon compte");
        verifButton.addActionListener(evt -> AuthService.getInstance().verifyAccount(email, code.getText()));

        Validator val = new Validator();
        val.setValidationFailureHighlightMode(Validator.HighlightMode.UIID_AND_EMBLEM);
        val.addSubmitButtons(verifButton)
                .addConstraint(code, new LengthConstraint(6));

        addAll(CustomComponent.emptyContainer(15), text, code, verifButton);

    }
}
