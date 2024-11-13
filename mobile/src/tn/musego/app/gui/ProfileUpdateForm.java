package tn.musego.app.gui;

import com.codename1.ui.*;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.RegexConstraint;
import com.codename1.ui.validation.Validator;
import tn.musego.app.entities.User;
import tn.musego.app.services.UserService;
import tn.musego.app.utils.CustomComponent;

/**
 * @author Skander Ben Fredj
 * @created 5/8/2023
 * @project pi-3a-mobile
 */

public class ProfileUpdateForm extends Form {
    public ProfileUpdateForm(Form previous, User user) {
        setSafeArea(true);
        getToolbar().addMaterialCommandToLeftBar(null, FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

        TextComponent username = new TextComponent().text(user.getUsername()).constraint(TextField.USERNAME)
                .labelAndHint("Nom").focusAnimation(false);
        TextComponent email = new TextComponent().text(user.getEmail()).constraint(TextField.EMAILADDR)
                .labelAndHint("Email").focusAnimation(false);
        TextComponent phone = new TextComponent().text(user.getPhone()).constraint(TextField.PHONENUMBER)
                .labelAndHint("N° de telephone").focusAnimation(false);

        Button registerBtn = new Button("Modifier");
        registerBtn.addActionListener(evt -> UserService.getInstance().updateUser(email.getText(), phone.getText(), username.getText()));
        Validator val = new Validator();
        val.setValidationFailureHighlightMode(Validator.HighlightMode.UIID_AND_EMBLEM);
        val.addSubmitButtons(registerBtn)
                .addConstraint(email, RegexConstraint.validEmail())
                .addConstraint(username, new LengthConstraint(3))
                .addConstraint(phone, new LengthConstraint(8));

        addAll(username, CustomComponent.emptyContainer(1), phone, CustomComponent.emptyContainer(1), email, CustomComponent.emptyContainer(1), registerBtn);

    }
}
