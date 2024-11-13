package tn.musego.app.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import tn.musego.app.entities.Formation;
import tn.musego.app.utils.HttpStatusCode;
import tn.musego.app.utils.PreferencesCnst;
import tn.musego.app.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceFormation {
    public static ServiceFormation instance = null;
    public ArrayList<Formation> formations;
    public boolean resultOK;

    private ConnectionRequest req;

    private boolean okParticip = false;

    private ServiceFormation() {
        req = new ConnectionRequest(){
            @Override
            public void setContentType(String contentType) {
                super.setContentType("application/json");
            }
        };
    }

    public static ServiceFormation getInstance() {
        if (instance == null) {
            instance = new ServiceFormation();
        }
        return instance;
    }

    public boolean participer(int id) {

        String url = Statics.BASE_URL + "formation/participer/" + id;

        req.setUrl(url);

        req.setPost(true);
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    Map<String, Object> apiReponseMap = new JSONParser().parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));

                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        String message = (String) apiReponseMap.get("message");
                        ip.dispose();
                        Dialog.show("Confirmation", message, "OK", null);
                        resultOK = message.contains("Confirmée");
                    } else {
                        ip.dispose();
                        Dialog.show("Erreur", (String) apiReponseMap.get("title"), "OK", null);
                        System.out.println("Error" + (String) apiReponseMap.get("title"));
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
        return resultOK;
    }

    public boolean isParticipated(int id) {

        String url = Statics.BASE_URL + "formation/isparticipated/" + id;

        req.setUrl(url);

        req.setPost(false);
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    Map<String, Object> apiReponseMap = new JSONParser().parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));

                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        String message = (String) apiReponseMap.get("message");
                        ip.dispose();
                        okParticip = !message.contains("pas");
                    } else {
                        System.out.println("Error" + (String) apiReponseMap.get("title"));
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
        return okParticip;
    }

    public ArrayList<Formation> getAllFormations(int id) {
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
        String url = Statics.BASE_URL + "formation/atelier/" + id;
        req.setUrl(url);
        req.setPost(false);
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        formations = new ArrayList<>();
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    Map<String, Object> apiReponseMap = new JSONParser().parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));
                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) apiReponseMap.get("data");
                        for (Map<String, Object> item : list) {
                            formations.add(new Formation().mapFromJson(item));
                        }
                        ip.dispose();
                    } else {
                        ip.dispose();
                        Dialog.show("Erreur", "erreur", "OK", null);
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
        return formations;
    }
}
