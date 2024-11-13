package tn.musego.app.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import tn.musego.app.entities.Abonnement;
import tn.musego.app.utils.HttpStatusCode;
import tn.musego.app.utils.PreferencesCnst;
import tn.musego.app.utils.Statics;

import java.io.IOException;
import java.util.Map;

public class ServiceAbonnement {

    public static ServiceAbonnement instance = null;
    public boolean resultOK;
    public String htmlres;

    private ConnectionRequest req;

    private ServiceAbonnement() {
        req = new ConnectionRequest() {
            @Override
            public void setContentType(String contentType) {
                super.setContentType("application/json");
            }
        };
    }

    public static ServiceAbonnement getInstance() {
        if (instance == null) {
            instance = new ServiceAbonnement();
        }
        return instance;
    }

    public Abonnement getAbonnementByUser() {
        final Abonnement[] abonnement = {new Abonnement()};
        String url = Statics.BASE_URL + "abonnements/existAbonnementByUser";

        req.setUrl(url);
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
        req.setPost(false);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                Dialog ip = new InfiniteProgress().showInfiniteBlocking();
                try {
                    Map<String, Object> apiReponseMap = new JSONParser().parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));
                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        if (apiReponseMap.get("data") != null) {
                            abonnement[0] = new Abonnement().mapFromMap((Map<String, Object>) apiReponseMap.get("data"));
                            ip.dispose();
                        } else {
                            abonnement[0] = null;
                            ip.dispose();
                        }
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
        return abonnement[0];
    }

    public Abonnement getAbonnementByUserOffre(int idoffre) {
        final Abonnement[] abonnement = {new Abonnement()};
        String url = Statics.BASE_URL + "abonnements/existAbonnementByUserOffre?idoffre=" + idoffre;


        req.setUrl(url);
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                Dialog ip = new InfiniteProgress().showInfiniteBlocking();

                try {
                    Map<String, Object> apiReponseMap = new JSONParser().parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));

                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        if (apiReponseMap.get("data") != null) {
                            abonnement[0] = new Abonnement().mapFromMap((Map<String, Object>) apiReponseMap.get("data"));
                        } else {
                            abonnement[0] = null;
                        }
                        ip.dispose();
                    } else {
                        ip.dispose();
                        Dialog.show("Erreur", (String) apiReponseMap.get("data"), "OK", null);
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
        return abonnement[0];
    }

    public boolean addAbonnement(int idoffre, String type, double prix) {

        String url = Statics.BASE_URL + "abonnements/add?idoffre=" + idoffre + "&type=" + type + "&prix=" + prix;

        req.setUrl(url);

        req.setPost(true);
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

    public boolean generatePDF(int idsub) {

        String url = Statics.BASE_URL + "abonnements/pdf/" + idsub;

        req.setUrl(url);

        req.setPost(true);
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK

                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

    public void stripePayment(String card, String type, double prix) {
        String url = Statics.BASE_URL + "abonnements/stripe/create-charge?stripeKey=sk_test_51MfrUJCFMOW49kAFTbESKgCT2eFqsze1SJ18WHIRNa7thrhtBprnaL9qZwiDtapzZZxWuuI8DvvcicFolagbl7q7005gJm14xW&type=" + type + "&prix=" + prix + "&card=" + card;
        req.setUrl(url);

        req.setPost(false);
        req.addRequestHeader("Authorization", "Bearer " + Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                htmlres = new String(req.getResponseData());
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
    }
}
