package com.tn.musego.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.java.Log;

/**
 * @author Skander Ben Fredj
 * @created 28-Feb-23
 * @project musego
 */
@Log
public class SMSHelper {

    String ACCOUNTSID;
    String AUTHTOKEN;

    public SMSHelper() {
        ACCOUNTSID = new FunctionHelper().getProperty("project.properties", "twiliosid");
        AUTHTOKEN = new FunctionHelper().getProperty("project.properties", "twilioauthtoken");
    }

    public void notifySuspectLogin(String phonenumber) {

        Twilio.init(ACCOUNTSID, AUTHTOKEN);

        Message message = Message.creator(
                        new PhoneNumber(phonenumber),
                        new PhoneNumber("+15673716970"),
                        "Une nouvelle connexion a partir d'un appareil non reconnu. Si vous n'êtes pas l'origine de cette connexion veillez changer votre mot de passe")
                .create();

        log.info(message.getSid());
    }
}
