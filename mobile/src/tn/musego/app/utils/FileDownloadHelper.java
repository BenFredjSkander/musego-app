package tn.musego.app.utils;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.Dialog;

public class FileDownloadHelper {


    public static boolean downloadUrlTo(String url, String fileName, boolean showProgress, boolean storage) {

        ConnectionRequest cr = new ConnectionRequest();
        cr.setPost(false);
        cr.setFailSilently(true);
        cr.setReadResponseForErrors(false);
        cr.setDuplicateSupported(true);
        cr.setUrl(url);

        cr.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));

        cr.setDestinationFile(fileName);

        if (showProgress) {
            InfiniteProgress ip = new InfiniteProgress();
            Dialog d = ip.showInfiniteBlocking();
            NetworkManager.getInstance().addToQueueAndWait(cr);
            d.dispose();
        } else {
            NetworkManager.getInstance().addToQueueAndWait(cr);
        }
        if (cr.getContentLength() > 0) {
            // verify the resulting file has the same size as the content length
            if (storage) {
                if (Storage.getInstance().entrySize(fileName) < cr.getContentLength()) {
                    return false;
                }
            } else {
                if (FileSystemStorage.getInstance().getLength(fileName) < cr.getContentLength()) {
                    return false;
                }
            }
        }
        int rc = cr.getResponseCode();
        return rc == 200 || rc == 201;
    }
}
