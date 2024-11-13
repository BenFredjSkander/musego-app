package com.tn.musego.utils;

import javafx.scene.control.Alert;

/**
 * @author Skander Ben Fredj
 * @created 06-Mar-23
 * @project musego
 */

public class AlertHelper {

    private AlertHelper() {
    }

    public static void showSuccess(String content) {
        alert(Alert.AlertType.INFORMATION, content, null, null);
    }

    public static void showError(String content) {
        alert(Alert.AlertType.ERROR, content, null, null);
    }

    public static void showWarning(String content) {
        alert(Alert.AlertType.ERROR, content, null, null);
    }

    static void alert(Alert.AlertType type, String content, String header, String title) {
        Alert a = new Alert(type, content);
        a.setTitle(title);
        a.setHeaderText(header);
        a.showAndWait();
    }
}
