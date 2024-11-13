package tn.musego.app.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import tn.musego.app.entities.Avis;
import tn.musego.app.utils.HttpStatusCode;
import tn.musego.app.utils.PreferencesCnst;
import tn.musego.app.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AvisService {

    private static AvisService instance = null;
    private ConnectionRequest req;
    private boolean resultOK;
    private ArrayList<Avis> avis;

    private AvisService() {
        req = new ConnectionRequest();
    }

    public static AvisService getInstance() {
        if (instance == null) {
            instance = new AvisService();
        }
        return instance;
    }

    public ArrayList<Avis> parseAvis(String jsonText) {
        try {
            avis = new ArrayList<>();
            JSONParser j = new JSONParser();

            Map<String, Object> avisListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String, Object>> list = (List<Map<String, Object>>) avisListJson.get("root");
            for (Map<String, Object> obj : list) {
                Avis a = new Avis();
                float id = Float.parseFloat(obj.get("id").toString());
                a.setId((long) id);
                a.setType(obj.get("type").toString());
                a.setDescription(obj.get("description").toString());
                a.setAvisSur(obj.get("avisSur").toString());

                avis.add(a);
            }

        } catch (IOException ex) {
        }

        return avis;
    }

    public ArrayList<Avis> getAllAvis() {
        ArrayList<Avis> listAvis = new ArrayList<>();
        String url = Statics.BASE_URL + "avis/getAll";
        req.setUrl(url);
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
        req.setPost(false);
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                    avis = parseAvis(new String(req.getResponseData()));
                    ip.dispose();
                } else {
                    ip.dispose();
                    Dialog.show("Erreur", "erreur", "OK", null);
                }
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return avis;
    }

    public boolean addAvis(Avis a) {

        String url = Statics.BASE_URL + "avis/new?type=" + a.getType() + "&description=" + a.getDescription() + "&avis_sur=" + a.getAvisSur();
        req.setUrl(url);
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200;
                req.removeResponseListener(this);
                ip.dispose();
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

    public boolean editAvis(Avis a) {

        String url = Statics.BASE_URL + "avis/edit?id=" + a.getId() + "&type=" + a.getType() + "&description=" + a.getDescription() + "&avis_sur=" + a.getAvisSur();
        req.setUrl(url);
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
        NetworkManager.getInstance().addToQueueAndWait(req);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200;
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }


    public void deleteAvis(Long id) {
        Dialog d = new Dialog();
        if (d.show("Delete Avis", "Do you really want to remove this Avis", "Yes", "No")) {
            String url = Statics.BASE_URL + "avis/delete?id=" + id;
            req.setUrl(url);
            req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
            NetworkManager.getInstance().addToQueueAndWait(req);
            d.dispose();
        }
    }
}