package tn.musego.app.gui;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.RegexConstraint;
import com.codename1.ui.validation.Validator;
import tn.musego.app.MyApplication;
import tn.musego.app.services.AuthService;
import tn.musego.app.utils.CustomComponent;

/**
 * @author Skander Ben Fredj
 * @created 5/3/2023
 * @project pi-3a-mobile
 */

public class LoginForm extends Form {

    public LoginForm() {
        super();
        setLayout(BoxLayout.y());
        setSafeArea(true);
        getToolbar().hideToolbar();
        ImageViewer logo = new ImageViewer(MyApplication.theme.getImage("LOGO dark.png").scaledHeight(Display.getInstance().getDisplayHeight() / 6));
        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Créer un compte", "register");
        Button resetBtn = new Button("Mot de pass oublié", "reset");

        registerBtn.addActionListener(evt -> new RegisterForm(this).show());
        resetBtn.addActionListener(evt -> new PassResetRequestForm(this).show());

        TextComponent email = new TextComponent().constraint(TextField.EMAILADDR)
                .labelAndHint("Email");

        TextComponent password = new TextComponentPassword()
                .labelAndHint("Password").columns(10);

        Validator val = new Validator();
        val.setValidationFailureHighlightMode(Validator.HighlightMode.UIID_AND_EMBLEM);
        val.addSubmitButtons(loginBtn)
                .addConstraint(email, RegexConstraint.validEmail())
                .addConstraint(password, new LengthConstraint(6));
        loginBtn.addActionListener(evt -> {
            AuthService.getInstance().login(email.getText(), password.getText());
        });

        addAll(CustomComponent.emptyContainer(8), logo, email, CustomComponent.emptyContainer(1), password, CustomComponent.emptyContainer(1), loginBtn, CustomComponent.emptyContainer(1), registerBtn, CustomComponent.emptyContainer(3), resetBtn);
    }
}
