package tn.musego.app.gui;

import com.codename1.components.ImageViewer;
import com.codename1.l10n.DateFormat;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;
import tn.musego.app.MyApplication;
import tn.musego.app.entities.User;
import tn.musego.app.services.UserService;

/**
 * @author Skander Ben Fredj
 * @created 5/8/2023
 * @project pi-3a-mobile
 */

public class ProfileForm extends Form {
    public ProfileForm(Form previous) {
        setLayout(BoxLayout.y());
        setSafeArea(true);
        getToolbar().addMaterialCommandToLeftBar(null, FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
        User user = UserService.getInstance().getCurrentUser();
        ImageViewer pic = new ImageViewer(MyApplication.theme.getImage("profilepic.png").scaledHeight(Display.getInstance().getDisplayHeight() / 5));

        String nom = user.getUsername();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String since = dateFormat.format(user.getCreatedDate());

        getToolbar().addMaterialCommandToRightBar(null, FontImage.MATERIAL_EDIT, e -> new ProfileUpdateForm(this, user).show());
        addAll(pic, new Label("Nom: " + nom), new Label("Membre depuis: " + since));
    }
}
