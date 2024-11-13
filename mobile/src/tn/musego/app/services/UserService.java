package tn.musego.app.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import tn.musego.app.entities.User;
import tn.musego.app.gui.HomeForm;
import tn.musego.app.utils.HttpStatusCode;
import tn.musego.app.utils.PreferencesCnst;
import tn.musego.app.utils.Statics;

import java.io.IOException;
import java.util.Map;

/**
 * @author Skander Ben Fredj
 * @created 5/6/2023
 * @project pi-3a-mobile
 */

public class UserService {

    public static UserService instance = null;
    User u;
    private ConnectionRequest req;
    private ConnectionRequest reqForm;


    private UserService() {
        req = new ConnectionRequest();
        reqForm = new ConnectionRequest();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public User getCurrentUser() {
        req.setUrl(Statics.BASE_URL + "users/me");
        req.setPost(false);
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    Map<String, Object> apiReponseMap = new JSONParser().parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));


                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        System.out.println((Map<String, Object>) apiReponseMap.get("data"));
                        u = new User().mapFromJson((Map<String, Object>) apiReponseMap.get("data"));
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
        return u;

    }

    public void updateUser(String email, String phone, String username) {
        reqForm.setUrl(Statics.BASE_URL + "users/me");
        req.setPost(true);
        reqForm.addArgument("email", email);
        reqForm.addArgument("phone", phone);
        reqForm.addArgument("username", username);
        reqForm.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));

        reqForm.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                Dialog ip = new InfiniteProgress().showInfiniteBlocking();

                try {
                    Map<String, Object> apiReponseMap = new JSONParser().parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));


                    if (reqForm.getResponseCode() == HttpStatusCode.OK.value()) {

                        ip.dispose();
                        if (Dialog.show(null, "Profil mis a jour avec success", "OK", null)) {
                            new HomeForm().show();
                        }

                    } else {
                        ip.dispose();
                        Dialog.show("Erreur", (String) apiReponseMap.get("detail"), "OK", null);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    ip.dispose();
                    reqForm.removeResponseListener(this);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(reqForm);

    }

}
