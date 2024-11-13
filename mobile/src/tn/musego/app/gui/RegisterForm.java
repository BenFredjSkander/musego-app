package tn.musego.app.gui;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.RegexConstraint;
import com.codename1.ui.validation.Validator;
import tn.musego.app.services.AuthService;
import tn.musego.app.utils.CustomComponent;

/**
 * @author Skander Ben Fredj
 * @created 5/3/2023
 * @project pi-3a-mobile
 */

public class RegisterForm extends Form {

    public RegisterForm(Form previous) {
        super();
        setLayout(BoxLayout.y());
        setSafeArea(true);
        getToolbar().addMaterialCommandToLeftBar(null, FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
        TextComponent username = new TextComponent().constraint(TextField.USERNAME)
                .labelAndHint("Nom").focusAnimation(false);
        TextComponent email = new TextComponent().constraint(TextField.EMAILADDR)
                .labelAndHint("Email").focusAnimation(false);
        TextComponent phone = new TextComponent().constraint(TextField.PHONENUMBER)
                .labelAndHint("N° de telephone").focusAnimation(false);
        TextComponent password = new TextComponentPassword()
                .labelAndHint("Password").focusAnimation(false);
        Button registerBtn = new Button("Créer mon compte");

        username.getStyle().setPaddingUnit(Style.UNIT_TYPE_PIXELS);
        username.getStyle().setPaddingBottom(10);

        email.getStyle().setPaddingUnit(Style.UNIT_TYPE_PIXELS);
        email.getStyle().setPaddingBottom(10);

        phone.getStyle().setPaddingUnit(Style.UNIT_TYPE_PIXELS);
        phone.getStyle().setPaddingBottom(10);

        password.getStyle().setPaddingUnit(Style.UNIT_TYPE_PIXELS);
        password.getStyle().setPaddingBottom(10);

        Validator val = new Validator();
        val.setValidationFailureHighlightMode(Validator.HighlightMode.UIID_AND_EMBLEM);
        val.addSubmitButtons(registerBtn)
                .addConstraint(email, RegexConstraint.validEmail())
                .addConstraint(password, new LengthConstraint(6))
                .addConstraint(username, new LengthConstraint(3))
                .addConstraint(phone, new LengthConstraint(8));
        registerBtn.addActionListener(evt -> AuthService.getInstance().register(this, email.getText(), username.getText(), phone.getText(), password.getText()));

        addAll(username, CustomComponent.emptyContainer(1), phone, CustomComponent.emptyContainer(1), email, CustomComponent.emptyContainer(1), password, CustomComponent.emptyContainer(1), registerBtn);
    }
}
