package tn.musego.app.utils;

import com.codename1.ui.Display;

/**
 * @author Skander Ben Fredj
 * @created 5/8/2023
 * @project pi-3a-mobile
 */

public class SizeHelper {
    public static int percentageWidth(int percentage) {
        return (int) (Display.getInstance().getDisplayWidth() * (0.01 * percentage));
    }

    public static int percentageHeight(int percentage) {
        return (int) (Display.getInstance().getDisplayHeight() * (0.01 * percentage));
    }
}
