package tn.musego.app.utils;

import com.codename1.ui.Container;
import com.codename1.ui.plaf.Style;

/**
 * @author Skander Ben Fredj
 * @created 5/8/2023
 * @project pi-3a-mobile
 */

public class CustomComponent {
    public static Container emptyContainer(int height) {
        Container c = new Container();
        c.getStyle().setPaddingUnit(Style.UNIT_TYPE_SCREEN_PERCENTAGE);
        c.getStyle().setPaddingTop(height);
        return c;
    }
}
