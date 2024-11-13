package tn.musego.app.gui;

import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.validation.RegexConstraint;
import com.codename1.ui.validation.Validator;
import tn.musego.app.services.AuthService;
import tn.musego.app.utils.CustomComponent;

/**
 * @author Skander Ben Fredj
 * @created 5/9/2023
 * @project pi-3a-mobile
 */

public class PassResetRequestForm extends Form {
    public PassResetRequestForm(Form previous) {
        setLayout(BoxLayout.y());
        setSafeArea(true);
        getToolbar().addMaterialCommandToLeftBar(null, FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
        SpanLabel label = new SpanLabel("Veuillez entre votre email pour réinitialiser votre mot de passe");
        TextComponent email = new TextComponent().constraint(TextField.EMAILADDR)
                .labelAndHint("Email").focusAnimation(false);

        Button requestResetBtn = new Button("Commencer");

        Validator val = new Validator();
        val.setValidationFailureHighlightMode(Validator.HighlightMode.UIID_AND_EMBLEM);
        val.addSubmitButtons(requestResetBtn)
                .addConstraint(email, RegexConstraint.validEmail());
        requestResetBtn.addActionListener(evt -> AuthService.getInstance().requestReset(this, email.getText()));
        addAll(label, CustomComponent.emptyContainer(3), email, CustomComponent.emptyContainer(4), requestResetBtn);
    }
}
