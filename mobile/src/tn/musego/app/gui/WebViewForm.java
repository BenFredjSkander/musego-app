package tn.musego.app.gui;

import com.codename1.ui.BrowserComponent;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;

/**
 * @author Skander Ben Fredj
 * @created 5/11/2023
 * @project pi-3a-mobile
 */

public class WebViewForm extends Form {
    public WebViewForm(Form previous, String link) {
        getToolbar().addMaterialCommandToLeftBar(null, FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
        setLayout(BoxLayout.y());

        BrowserComponent browser = new BrowserComponent();
        browser.setURL(link);
        browser.setPinchToZoomEnabled(false);
        add(browser);
    }
}
