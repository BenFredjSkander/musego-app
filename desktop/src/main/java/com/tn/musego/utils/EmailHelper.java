package com.tn.musego.utils;

import com.tn.musego.entities.enums.TypeOffreEnum;
import lombok.extern.java.Log;
import sendinblue.ApiClient;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.CreateSmtpEmail;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Log
public class EmailHelper {
    String myApiKey;
    String myemail;

    public EmailHelper() {
        myApiKey = new FunctionHelper().getProperty("project.properties", "sendinblueapikey");
        myemail = new FunctionHelper().getProperty("project.properties", "emailcontact");
    }

    public void sendEmail(String userEmail, String code, int template) {
        try {
            ApiClient defaultClient = Configuration.getDefaultApiClient();
            // Configure API key authorization: api-key
            ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
            apiKey.setApiKey(myApiKey);
            TransactionalEmailsApi api = new TransactionalEmailsApi();
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(myemail);
            sender.setName("Musego.tn");
            List<SendSmtpEmailTo> toList = new ArrayList<>();
            SendSmtpEmailTo to = new SendSmtpEmailTo();
            to.setEmail(userEmail);
            toList.add(to);
            Properties params = new Properties();
            params.setProperty("CODE", code);
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSender(sender);
            sendSmtpEmail.setTo(toList);
            sendSmtpEmail.setParams(params);
            sendSmtpEmail.setTemplateId((long) template);
            CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);
            log.info(response.toString());
        } catch (Exception e) {
            log.severe(e.getMessage());
            log.severe(String.format("Could not send email to : %s", userEmail));
        }

    }


    public void confirmationFormation(String userEmail, String formationName, Date dateDebut, Date dateFin) {
        try {
            ApiClient defaultClient = Configuration.getDefaultApiClient();
            // Configure API key authorization: api-key
            ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
            apiKey.setApiKey(myApiKey);
            TransactionalEmailsApi api = new TransactionalEmailsApi();
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(myemail);
            sender.setName("Musego.tn");
            List<SendSmtpEmailTo> toList = new ArrayList<>();
            SendSmtpEmailTo to = new SendSmtpEmailTo();
            to.setEmail(userEmail);
            toList.add(to);
            Properties params = new Properties();
            params.setProperty("FORMATION_NAME", formationName);
            params.setProperty("FORMATION_BEGIN_DATE", dateDebut.toString());
            params.setProperty("FORMATION_END_DATE", dateFin.toString());
            System.out.println(params);
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSender(sender);
            sendSmtpEmail.setTo(toList);
            sendSmtpEmail.setParams(params);
            sendSmtpEmail.setTemplateId(3L);
            CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);
            log.info(response.toString());
        } catch (Exception e) {
            log.severe(e.getMessage());
            log.severe(String.format("Could not send email to : %s", userEmail));
        }

    }

    public void confirmationPaiement(String userEmail, String username, TypeOffreEnum toffre, Double price) {
        try {
            ApiClient defaultClient = Configuration.getDefaultApiClient();
            // Configure API key authorization: api-key
            ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
            apiKey.setApiKey(myApiKey);
            TransactionalEmailsApi api = new TransactionalEmailsApi();
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(myemail);
            sender.setName("Musego.tn");
            List<SendSmtpEmailTo> toList = new ArrayList<>();
            SendSmtpEmailTo to = new SendSmtpEmailTo();
            to.setEmail(userEmail);
            toList.add(to);
            Properties params = new Properties();
            params.setProperty("FIRSTNAME", username);
            params.setProperty("PRICE_ABONN", toffre.getLabel());
            params.setProperty("DATENOW", new Date().toString());
            params.setProperty("PRICE_ABONN", price.toString()+"€");
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSender(sender);
            sendSmtpEmail.setTo(toList);
            sendSmtpEmail.setParams(params);
            sendSmtpEmail.setTemplateId(4L);
            CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);
            log.info(response.toString());
        } catch (Exception e) {
            log.severe(e.getMessage());
            log.severe(String.format("Could not send email to : %s", userEmail));
        }

    }
}
