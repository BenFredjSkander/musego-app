package tn.musego.app.services;


import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import tn.musego.app.entities.Oeuvre;
import tn.musego.app.utils.HttpStatusCode;
import tn.musego.app.utils.PreferencesCnst;
import tn.musego.app.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OeuvreService {
    public static volatile OeuvreService instance = null;
    public ArrayList<Oeuvre> oeuvres;
    public boolean resultOK;
    private ConnectionRequest req;

    public OeuvreService() {
        req = new ConnectionRequest(){
            @Override
            public void setContentType(String contentType) {
                super.setContentType("application/json");
            }
        };
    }

    public static OeuvreService getInstance() {
        if (instance == null) {
            instance = new OeuvreService();
        }
        return instance;
    }


//    public ArrayList<Oeuvre> getAllOeuvres() {
//        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
//        String url = Statics.BASE_URL + "oeuvres/list";
//        System.out.println(url + "-------------------------------------------- vtvtvybgy ");
//        req.setUrl(url);
//        req.setPost(false);
//        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
//
//        req.addResponseListener(new ActionListener<NetworkEvent>() {
//            @Override
//            public void actionPerformed(NetworkEvent evt) {
//
//
//                if (req.getResponseCode() == HttpStatusCode.OK.value()) {
//                    oeuvres = parseOeuvres(new String(req.getResponseData()));
//                    ip.dispose();
//                } else {
//                    ip.dispose();
//                    Dialog.show("Erreur", "erreur", "OK", null);
//                }
//
//                req.removeResponseListener(this);
//            }
//        });
//        NetworkManager.getInstance().addToQueueAndWait(req);
//        return oeuvres;
//    }

    public ArrayList<Oeuvre> getAllOeuvresByCategorie(int categorieId) {
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
        String url = Statics.BASE_URL + "oeuvres/categorie/" + categorieId;
        req.setUrl(url);
        req.setPost(false);
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        oeuvres = new ArrayList<>();
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    Map<String, Object> apiReponseMap = new JSONParser().parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));
                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) apiReponseMap.get("data");
                        for (Map<String, Object> item : list) {
                            oeuvres.add(new Oeuvre().mapFromJson(item));
                        }
                        ip.dispose();
                    } else {
                        ip.dispose();
                        Dialog.show("Erreur", "erreur", "OK", null);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    req.removeResponseListener(this);

                }
            }
        });

        NetworkManager.getInstance().addToQueueAndWait(req);


        return oeuvres;
    }

    public Oeuvre getOeuvreByTitre(String titre) {
        for (Oeuvre o : oeuvres) {
            if (o.getTitre().equals(titre)) {
                return o;
            }
        }
        return null; // retourner null si l'oeuvre n'est pas trouvée
    }


}
