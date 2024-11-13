package com.tn.musego.services.impl;

import com.tn.musego.entities.PassResetToken;
import com.tn.musego.entities.Session;
import com.tn.musego.entities.User;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.IAuthService;
import com.tn.musego.utils.*;
import lombok.extern.java.Log;
import org.json.JSONArray;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

@Log
public class AuthService implements IAuthService {
    private static final Random RANDOM = new Random();

    Connection connection = null;

    public AuthService() {
        connection = DBConnection.getInstance().getConnection();
    }

    //method used for unit tests
    public AuthService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User login(String email, String password) throws MyCustomException {
        String req1 = "select * from user where email = ?";
        String req2 = "insert into session (created_date, expiry, id_user, device, uid) VALUES (?,?,?,?,?)";
        Preferences userPreferences = Preferences.userRoot();
        User user = new User();
        try (PreparedStatement pst1 = connection.prepareStatement(req1);
             PreparedStatement pst2 = connection.prepareStatement(req2)) {
            pst1.setString(1, email);
            ResultSet resultSet1 = pst1.executeQuery();
            if (!resultSet1.next()) {
                throw new MyCustomException("Email ou mot de passe incorrect");
            }
            if (!FunctionHelper.passwordMatch(resultSet1.getString("password"), password)) {
                throw new MyCustomException("Mot de passe incorrect");
            }
            user = new User().mapFromResultSet(resultSet1);
            if (user.isLocked()) {
                throw new MyCustomException("Votre de compte est bloqué, Veuillez contacter l'administrateur");
            }
            if (!user.isVerified()) {
                throw new MyCustomException("Votre de compte n'est pas activé");
            }
            Timestamp currentTimestamp = DateHelper.currentTime();
            String deviceUUID = FunctionHelper.getMachineID();

            if (userSession(user.getId(), deviceUUID)) {
                log.info("sending SMS");
                //TODO ENABLE IN PROD
//                SMSHelper smsHelper = new SMSHelper();
//                smsHelper.notifySuspectLogin("+21693934708");
            }else {
                pst2.setTimestamp(1, currentTimestamp);
                pst2.setTimestamp(2, DateHelper.addDays(30, currentTimestamp));
                pst2.setLong(3, resultSet1.getLong("id"));
                pst2.setString(4, "Desktop");
                pst2.setString(5, deviceUUID);
                pst2.executeUpdate();
            }
            userPreferences.putByteArray("user_uuid", deviceUUID.getBytes());
            userPreferences.putLong(StringCnst.user_id_key, resultSet1.getLong("id"));
//            userPreferences.putByteArray(StringCnst.user_role_key, resultSet1.getString("role").getBytes());
            log.info("user logged in successfully");
            return user;
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return null;
    }

    @Override
    public void signup(User user) throws MyCustomException {
        String req = "insert into user (created_date, username, email, password, is_verified, roles, locked,phone_number) values (?,?,?,?,?,?,?,?)";
        String req2 = "select id from user where email = ?";
        JSONArray roles = new JSONArray(user.getRoles());
        String req3 = "insert into email_verification (created_date, token, used, id_user) VALUES (?,?,?,?)";
        try (PreparedStatement pst = connection.prepareStatement(req);
             PreparedStatement pst2 = connection.prepareStatement(req2);
             PreparedStatement pst3 = connection.prepareStatement(req3)) {
            ResultSet resultSet;
            pst.setTimestamp(1, DateHelper.currentTime());
            pst.setString(2, user.getUsername());
            pst.setString(3, user.getEmail());
            pst.setString(4, FunctionHelper.encryptPassword(user.getPassword()));
            pst.setBoolean(5, false);
            pst.setObject(6, roles.toString());
            pst.setBoolean(7, user.isLocked());
            pst.setString(8, "12 236 456");
            pst.executeUpdate();

            pst2.setString(1, user.getEmail());
            resultSet = pst2.executeQuery();
            resultSet.next();
            long userid = resultSet.getLong("id");


            String token = FunctionHelper.generateUUID();
            pst3.setTimestamp(1, DateHelper.currentTime());
            pst3.setString(2, token);
            pst3.setBoolean(3, false);
            pst3.setLong(4, userid);
            pst3.executeUpdate();

            EmailHelper emailHelper = new EmailHelper();
            emailHelper.sendEmail(user.getEmail(), token, 1);

            log.info("user signup successfully");

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new MyCustomException("Email déja utilisé");
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public void checkEmailVerifCode(String token) throws MyCustomException {
        String req = "select * from email_verification where token = ? and used = false";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new MyCustomException("Code invalide");
            }
            resultSet.close();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public void destroyEmailVerifToken(String token) {
        String req = "update email_verification set used = true where token = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setString(1, token);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public void requestResetPassword(String email) throws MyCustomException {
        String req = "select * from user where email = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new MyCustomException("Email invalide");
            }
            String token = FunctionHelper.generateUUID();
            PassResetToken resetToken = new PassResetToken(DateHelper.currentTime(), token, resultSet.getLong("id"));
            PassTokenService passTokenService = new PassTokenService();
            passTokenService.addToken(resetToken);
            resultSet.close();
            EmailHelper emailHelper = new EmailHelper();
            emailHelper.sendEmail(email, token, 2);
            log.info("email sent with token");
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public void logout() throws BackingStoreException {
        Preferences userPreferences = Preferences.userRoot();
        userPreferences.clear();
    }

    public String generateRandomNumber() {
        String numbers = "0123456789";
        char[] otp = new char[6];
        for (int i = 0; i < 6; i++) {
            otp[i] = numbers.charAt(RANDOM.nextInt(numbers.length()));
        }
        return String.valueOf(otp);
    }


    public List<Session> getSessionsByCriteria(Map<String, Object> criteriaMap) {
        List<Session> users = new ArrayList<>();
        try (PreparedStatement statement = FunctionHelper.preparedStCriteria("session", criteriaMap, connection);
             ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                Session p = new Session().mapFromResultSet(resultSet);
                users.add(p);
            }
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return users;
    }

    public boolean userSession(Long userid, String session){
        String req = "select count(*) as c_sessions from session where id_user = ? and uid = ?";
        boolean isNew = true;
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setLong(1, userid);
            statement.setString(1, session);
            ResultSet resultSet = statement.executeQuery();
            resultSet.close();
            isNew =  resultSet.getInt("c_sessions") == 0;
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return isNew;
    }
}
