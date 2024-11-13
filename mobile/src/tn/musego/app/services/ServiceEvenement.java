package tn.musego.app.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import tn.musego.app.entities.Evenement;
import tn.musego.app.utils.HTTPMethod;
import tn.musego.app.utils.HttpStatusCode;
import tn.musego.app.utils.PreferencesCnst;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static tn.musego.app.utils.Statics.BASE_URL;

public class ServiceEvenement {

    public static ServiceEvenement instance = null;

    public ArrayList<Evenement> evenements;

    private ConnectionRequest req;

    private ServiceEvenement() {

        req = new ConnectionRequest(){
            @Override
            public void setContentType(String contentType) {
                super.setContentType("application/json");
            }
        };
    }

    public static ServiceEvenement getInstance() {
        if (instance == null) {
            instance = new ServiceEvenement();
        }
        return instance;
    }


    public ArrayList<Evenement> getAllEvenements() {
        String url = BASE_URL + "evenements";
        req.setUrl(url);
        req.setPost(false);
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        evenements = new ArrayList<>();
        req.addResponseListener(new ActionListener<NetworkEvent>() {

            @Override
            public void actionPerformed(NetworkEvent evt) {

                try {
                    Map<String, Object> apiReponseMap = new JSONParser().parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));
                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) apiReponseMap.get("data");
                        for (Map<String, Object> evenement : list) {
                            evenements.add(new Evenement().parseFromJson(evenement));
                        }
                        ip.dispose();
                    } else {
                        ip.dispose();
                        Dialog.show("Erreur", (String) apiReponseMap.get("detail"), "OK", null);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    req.removeResponseListener(this);
                }
            }

        });

        NetworkManager.getInstance().addToQueueAndWait(req);
        return evenements;
    }


    public Evenement getEvenement(int id) {
        String url = BASE_URL + "evenements/" + id;
        req.setUrl(url);
        req.setPost(false);
        req.setHttpMethod(HTTPMethod.GET.name());
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));

        // Declare an Evenement variable
        final Evenement[] evenement = {null};
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                try {
                    Map<String, Object> apiReponseMap = new JSONParser().parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));
                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        Map<String, Object> eventMap = (Map<String, Object>) apiReponseMap.get("data");
                        evenement[0] = new Evenement().parseFromJson(eventMap);

                        ip.dispose();
                    } else {
                        ip.dispose();
                        Dialog.show("Erreur", (String) apiReponseMap.get("detail"), "OK", null);
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

        // Return the Evenement object outside the actionPerformed() method
        return evenement[0];
    }


}
