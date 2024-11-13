package tn.musego.app.utils;

import ca.weblite.codename1.json.JSONException;
import ca.weblite.codename1.json.JSONObject;
import com.codename1.util.Base64;
import com.codename1.util.DateUtil;
import com.codename1.util.StringUtil;

import java.util.Date;

/**
 * Utility class to extract expiration time from jwt token and determine if the token is still valid
 *
 * @author Skander Ben Fredj
 * @created 5/11/2023
 * @project pi-3a-mobile
 */

public class JwtHelper {

    public static boolean isValid(String token) {
        boolean isValid = false;

        String payloadString = StringUtil.tokenize(token, "\\.").get(1);
        System.out.println(token);
        System.out.println("token = " + payloadString);

        //while loop to fix missing padding in base64 string (bad implementation in codename one base64 class :/
        while (payloadString.length() % 4 != 0) {
            payloadString = payloadString.concat("=");
        }

        byte[] decodedBytes = Base64.decode(payloadString.getBytes());
        String decodedString = new String(decodedBytes);

        try {
            JSONObject payloadMap = new JSONObject(decodedString);
            Date expiry = new Date(payloadMap.getLong("exp") * 1000);
            int comRes = DateUtil.compare(expiry, new Date());
            isValid = (comRes == 1);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return isValid;
    }
}
