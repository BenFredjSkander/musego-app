package tn.musego.app.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import tn.musego.app.entities.Offre;
import tn.musego.app.utils.HttpStatusCode;
import tn.musego.app.utils.PreferencesCnst;
import tn.musego.app.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceOffre {
    public ArrayList<Offre> offres;

    public static ServiceOffre instance = null;
    private ConnectionRequest req;

    private ServiceOffre() {
        req = new ConnectionRequest(){
            @Override
            public void setContentType(String contentType) {
                super.setContentType("application/json");
            }
        };
    }

    public static ServiceOffre getInstance() {
        if (instance == null) {
            instance = new ServiceOffre();
        }
        return instance;
    }

    public Offre getOffre(int idoffre) {
        final Offre[] offre = {new Offre()};
        String url = Statics.BASE_URL + "offres/get?idoffre=" + idoffre;

        req.setUrl(url);
        req.setPost(false);
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                try {
                    Dialog ip = new InfiniteProgress().showInfiniteBlocking();
                    Map<String, Object> apiReponseMap = new JSONParser()
                            .parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));

                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        offre[0] = new Offre().mapFromJson((Map<String, Object>) apiReponseMap.get("data"));
                        ip.dispose();
                    } else {
                        ip.dispose();
                        Dialog.show("Erreur", (String) apiReponseMap.get("title"), "OK", null);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    req.removeResponseListener(this);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return offre[0];
    }

    public ArrayList<Offre> getAllOffres() {
        String url = Statics.BASE_URL + "offres/list";
        req.setUrl(url);

        req.setPost(false);
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
        offres = new ArrayList<>();
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    Map<String, Object> apiReponseMap = new JSONParser().parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));

                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) apiReponseMap.get("data");
                        for (Map<String, Object> item : list) {
                            offres.add(new Offre().mapFromJson(item));
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
        return offres;
    }
}
