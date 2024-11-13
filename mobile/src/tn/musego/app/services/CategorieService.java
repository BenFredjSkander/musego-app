package tn.musego.app.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import tn.musego.app.entities.Categorie;
import tn.musego.app.utils.HttpStatusCode;
import tn.musego.app.utils.PreferencesCnst;
import tn.musego.app.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CategorieService {
    public static volatile CategorieService instance = null;
    public ArrayList<Categorie> categories;
    public boolean resultOK;
    private ConnectionRequest req;
    private ArrayList<Categorie> list;

    public CategorieService() {
        req = new ConnectionRequest(){
            @Override
            public void setContentType(String contentType) {
                super.setContentType("application/json");
            }
        };
    }

    public static CategorieService getInstance() {
        if (instance == null) {
            instance = new CategorieService();
        }
        return instance;
    }

    public ArrayList<Categorie> getAllCategories() {
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
        String url = Statics.BASE_URL + "categories/list";
        req.setUrl(url);
        req.setPost(false);
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        categories = new ArrayList<>();
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    Map<String, Object> apiReponseMap = new JSONParser()
                            .parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));
                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) apiReponseMap.get("data");
                        for (Map<String, Object> item : list) {
                            categories.add(new Categorie().mapFromJson(item));
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
        return categories;
    }
}
