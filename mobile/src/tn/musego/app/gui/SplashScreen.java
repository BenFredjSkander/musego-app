package tn.musego.app.gui;

import com.codename1.io.Preferences;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.UITimer;
import tn.musego.app.MyApplication;
import tn.musego.app.services.AuthService;
import tn.musego.app.utils.PreferencesCnst;

/**
 * @author Skander Ben Fredj
 * @created 5/8/2023
 * @project pi-3a-mobile
 */

public class SplashScreen extends Form {
    public SplashScreen() {
        setLayout(new FlowLayout(CENTER, CENTER));
        getToolbar().hideToolbar();
        setSafeArea(true);

        Image logo = MyApplication.theme.getImage("LOGO dark.png").scaledHeight(Display.getInstance().getDisplayHeight() / 3);
        add(logo);

        new UITimer(() -> {
            if (Preferences.get(PreferencesCnst.Preferences_TOKEN, null) == null) {
                new LoginForm().show();
            } else {
                AuthService.getInstance().refreshAuthToken();
                new HomeForm().show();
            }
        }).schedule(2000, false, this);
    }

}
