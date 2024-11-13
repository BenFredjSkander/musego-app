package tn.musego.app.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import tn.musego.app.entities.Atelier;
import tn.musego.app.utils.HttpStatusCode;
import tn.musego.app.utils.PreferencesCnst;
import tn.musego.app.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceAtelier {

    public ArrayList<Atelier> ateliers;

    public static ServiceAtelier instance = null;
    public boolean resultOK;

    private ConnectionRequest req;

    private ServiceAtelier() {
        req = new ConnectionRequest(){
            @Override
            public void setContentType(String contentType) {
                super.setContentType("application/json");
            }
        };
    }

    public static ServiceAtelier getInstance() {
        if (instance == null) {
            instance = new ServiceAtelier();
        }
        return instance;
    }


    public ArrayList<Atelier> getAllAteliers() {
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
        String url = Statics.BASE_URL + "atelier/";
        req.setUrl(url);
        req.setPost(false);
        ateliers = new ArrayList<>();
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    Map<String, Object> apiReponseMap = new JSONParser().parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));
                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) apiReponseMap.get("data");
                        for (Map<String, Object> item : list) {
                            ateliers.add(new Atelier().mapFromJson(item));
                        }
                        ip.dispose();
                    } else {
                        ip.dispose();
                        Dialog.show("Erreur", (String) apiReponseMap.get("title"), "OK", null);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    ip.dispose();
                    req.removeResponseListener(this);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return ateliers;
    }
}
