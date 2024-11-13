package tn.musego.app.services;

import ca.weblite.codename1.json.JSONException;
import ca.weblite.codename1.json.JSONObject;
import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionListener;
import tn.musego.app.gui.*;
import tn.musego.app.utils.HttpStatusCode;
import tn.musego.app.utils.JwtHelper;
import tn.musego.app.utils.PreferencesCnst;
import tn.musego.app.utils.Statics;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Skander Ben Fredj
 * @created 5/5/2023
 * @project pi-3a-mobile
 */

public class AuthService {

    public static AuthService instance = null;
    ConnectionRequest reqForm = new ConnectionRequest();
    private ConnectionRequest req;
    private JSONObject requestBody = new JSONObject();


    private AuthService() {
        req = new ConnectionRequest() {
            @Override
            protected void buildRequestBody(OutputStream os) throws IOException {
                os.write(requestBody.toString().getBytes("UTF-8"));
            }
        };
        reqForm = new ConnectionRequest();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }


    public void login(String email, String password) {


        req.setUrl(Statics.BASE_URL + "login");
        req.setPost(true);
        req.setContentType("application/json");
        req.addArgument("body", requestBody.toString());
        try {
            requestBody.put("email", email);
            requestBody.put("password", password);
            requestBody.put("code", "123456789");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    Map<String, Object> apiReponseMap = new JSONParser().parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));
                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        saveToken((String) apiReponseMap.get("token"), (String) apiReponseMap.get("refresh_token"));
                        System.out.println("******************");
                        ip.dispose();
                        new HomeForm().show();
                    } else if (req.getResponseCode() == HttpStatusCode.UNAUTHORIZED.value()) {
                        ip.dispose();
                        Dialog.show("Erreur", (String) apiReponseMap.get("message"), "OK", null);
                        System.out.println("Error" + (String) apiReponseMap.get("message"));
                    } else {
                        ip.dispose();
                        Dialog.show("Erreur", (String) apiReponseMap.get("title"), "OK", null);

                        System.out.println("Error" + (String) apiReponseMap.get("title"));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    req.removeResponseListener(this);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);

    }

    public void register(Form form, String email, String username, String phone, String password) {

        req.setUrl(Statics.BASE_URL + "register");
        req.setPost(true);
        req.setContentType("application/json");
        req.addArgument("body", requestBody.toString());
        try {
            requestBody.put("email", email);
            requestBody.put("username", username);
            requestBody.put("phone", phone);
            requestBody.put("password", password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    JSONParser j = new JSONParser();
                    Map<String, Object> apiReponseMap = j.parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));

                    if (req.getResponseCode() == HttpStatusCode.CREATED.value()) {
                        saveToken((String) apiReponseMap.get("token"), (String) apiReponseMap.get("refresh_token"));
                        System.out.println(Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
                        System.out.println(Preferences.get(PreferencesCnst.Preferences_REFRESH, null));
                        ip.dispose();
                        new VerifyEmailForm(form, email).show();
                    } else {
                        ip.dispose();
                        Dialog.show("Erreur", (String) apiReponseMap.get("title"), "OK", null);

                        System.out.println("Error" + (String) apiReponseMap.get("title"));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    req.removeResponseListener(this);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
    }


    public void refreshAuthToken() {
        if (JwtHelper.isValid(Preferences.get(PreferencesCnst.Preferences_TOKEN, null))) {
            return;
        }
        System.out.println("refreshing token");
        req.setUrl(Statics.BASE_URL + "token/refresh");
        req.setPost(true);
        req.setContentType("application/json");
        req.addArgument("body", requestBody.toString());
        try {
            requestBody.put("refresh_token", Preferences.get(PreferencesCnst.Preferences_REFRESH, null));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    JSONParser j = new JSONParser();
                    Map<String, Object> apiReponseMap = j.parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));

                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        saveToken((String) apiReponseMap.get("token"), (String) apiReponseMap.get("refresh_token"));
                        System.out.println(Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
                        System.out.println(Preferences.get(PreferencesCnst.Preferences_REFRESH, null));
                    } else {
                        Preferences.clearAll();
                        new LoginForm().show();

                        System.out.println("Error" + (String) apiReponseMap.get("title"));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    req.removeResponseListener(this);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
    }

    public void saveToken(String token, String refresh) {
        Preferences.set(PreferencesCnst.Preferences_TOKEN, token);
        Preferences.set(PreferencesCnst.Preferences_REFRESH, refresh);
    }


    public void verifyAccount(String email, String code) {

        req.setUrl(Statics.BASE_URL + "register/verify");
        req.setPost(true);
        req.setContentType("application/json");
        req.addArgument("body", requestBody.toString());
        try {
            requestBody.put("email", email);
            requestBody.put("code", code);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    JSONParser j = new JSONParser();

                    Map<String, Object> apiReponseMap = j.parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));

                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        ip.dispose();
//                        if (Dialog.show("Confirmation", (String) apiReponseMap.get("message"), "Terminer", null)) {
                        new LoginForm().show();
//                        }
                    } else {
                        ip.dispose();
                        Dialog.show("Erreur", (String) apiReponseMap.get("message"), "Terminer", null);
//                        System.out.println("Error" + (String) apiReponseMap.get("title"));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    req.removeResponseListener(this);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
    }

    /**
     * Invalidate token in the server and logout current user
     */
    public void logout() {
        req.setUrl(Statics.BASE_URL + "token/invalidate");
        req.setPost(true);
        req.setContentType("application/json");
        req.addArgument("body", requestBody.toString());
        try {
            requestBody.put("refresh_token", Preferences.get(PreferencesCnst.Preferences_REFRESH, null));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                Preferences.clearAll();
                Preferences.delete(PreferencesCnst.Preferences_TOKEN);
                Preferences.delete(PreferencesCnst.Preferences_REFRESH);
                req.removeResponseListener(this);
                ip.dispose();
                new LoginForm().show();
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);

    }

    public void requestReset(Form previous, String email) {
        reqForm.setUrl(Statics.BASE_URL + "reset/request");
        req.setPost(true);
        reqForm.addArgument("email", email);
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        reqForm.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    JSONParser j = new JSONParser();
                    Map<String, Object> apiReponseMap = j.parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));

                    if (reqForm.getResponseCode() == HttpStatusCode.OK.value()) {
                        ip.dispose();
                        new PassResetVerifyForm(previous, email).show();
                    } else {
                        ip.dispose();
                        Dialog.show("Erreur", (String) apiReponseMap.get("message"), "Terminer", null);
//                        System.out.println("Error" + (String) apiReponseMap.get("title"));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    reqForm.removeResponseListener(this);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(reqForm);
    }


    public void verifyReset(Form previous, String email, String code) {

        reqForm.setUrl(Statics.BASE_URL + "reset/verify");
        req.setPost(true);
        reqForm.addArgument("email", email);
        reqForm.addArgument("code", code);
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        reqForm.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    JSONParser j = new JSONParser();
                    Map<String, Object> apiReponseMap = j.parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));

                    if (reqForm.getResponseCode() == HttpStatusCode.OK.value()) {
                        ip.dispose();
                        new PassResetConfirm(email).show();
                    } else {
                        ip.dispose();
                        Dialog.show("Erreur", (String) apiReponseMap.get("message"), "Terminer", null);
//                        System.out.println("Error" + (String) apiReponseMap.get("title"));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    reqForm.removeResponseListener(this);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(reqForm);
    }


    public void resetConfirm(String email, String password) {

        req.setUrl(Statics.BASE_URL + "reset/confirm");
        req.setPost(true);
        req.setContentType("application/json");
        req.addArgument("body", requestBody.toString());
        try {
            requestBody.put("email", email);
            requestBody.put("newpass", password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    JSONParser j = new JSONParser();
                    Map<String, Object> apiReponseMap = j.parseJSON(new CharArrayReader((new String(req.getResponseData())).toCharArray()));

                    if (req.getResponseCode() == HttpStatusCode.OK.value()) {
                        ip.dispose();
                        new LoginForm().show();
                    } else {
                        ip.dispose();
                        Dialog.show("Erreur", (String) apiReponseMap.get("message"), "Terminer", null);
//                        System.out.println("Error" + (String) apiReponseMap.get("title"));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    req.removeResponseListener(this);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
    }


}
