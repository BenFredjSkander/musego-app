//package tn.musego.app.utils;
//
//import ca.weblite.codename1.json.JSONObject;
//import com.codename1.io.*;
//import com.codename1.ui.Dialog;
//import com.codename1.ui.events.ActionListener;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author Skander Ben Fredj
// * @created 5/2/2023
// * @project pi-3a-mobile
// */
//public class CrudHelper {
//
//    public static CrudHelper instance = null;
//
//
//    private ConnectionRequest request;
//    private JSONObject requestBody = new JSONObject();
//
//
//    private CrudHelper() {
//        request = new ConnectionRequest() {
//            @Override
//            protected void buildRequestBody(OutputStream os) throws IOException {
//                os.write(requestBody.toString().getBytes("UTF-8"));
//            }
//        };
//    }
//
//
//    public List<Map<String, Object>> getAll(String url) {
//        final List<Map<String, Object>>[] result = new List[]{new ArrayList<>()};
//        request.setUrl(url);
//        request.setContentType("application/json");
//        request.setHttpMethod(HTTPMethod.GET.name());
//        request.addRequestHeader("Authorization", Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
//        request.addResponseListener(new ActionListener<NetworkEvent>() {
//            @Override
//            public void actionPerformed(NetworkEvent evt) {
//
//                try {
//                    Map<String, Object> apiReponseMap = new JSONParser().parseJSON(new InputStreamReader(new ByteArrayInputStream(request.getResponseData()), "UTF-8"));
//                    if (request.getResponseCode() == HttpStatusCode.OK.value()) {
//                        result[0] = (List<Map<String, Object>>) apiReponseMap.get("data");
//                    }
////                if(apiResponse.get("data") instanceof List) {
////                    result =  (List<Map<String, Object>>) apiResponse.get("data");
//                    new ArrayList<>();
////                }
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//        return null;
//    }
//
//
//    public Map<String, Object> getItem(String url) {
//        ConnectionRequest request = new ConnectionRequest(url);
//        request.setContentType("application/json");
//        request.setHttpMethod(HTTPMethod.GET.name());
//        request.addRequestHeader("Authorization", Preferences.get(PreferencesCnst.Preferences_TOKEN, null));
//        request.addExceptionListener(networkEvent -> Dialog.show("Error", networkEvent.getError().getLocalizedMessage(), "OK", null));
//        request.addResponseListener(networkEvent -> {
//            JSONParser j = new JSONParser();
//            try {
//                Map<String, Object> apiResponse = j.parseJSON(new CharArrayReader(Arrays.toString(request.getResponseData()).toCharArray()));
////                return apiResponse.get("data");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return null;
//    }
//}
