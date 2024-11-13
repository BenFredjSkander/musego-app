package com.tn.musego.services.impl;

import com.tn.musego.entities.User;
import com.tn.musego.entities.enums.RoleEnum;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.IUserService;
import com.tn.musego.utils.DBConnection;
import com.tn.musego.utils.DateHelper;
import com.tn.musego.utils.FunctionHelper;
import com.tn.musego.utils.StringCnst;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.java.Log;
import org.json.JSONArray;

import java.sql.*;
import java.util.Map;
import java.util.prefs.Preferences;


@Log
public class UserService implements IUserService {
    Connection connection;

    public UserService() {
        connection = DBConnection.getInstance().getConnection();
    }

    //method used for unit tests
    public UserService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createEntity(User user) {
        String req = "insert into user (created_date, username, email, password, is_verified, profile_pic, birthdate, phone_number, speciality, hiring_date, roles) " +
                "values (?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setTimestamp(1, DateHelper.currentTime());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getEmail());
            statement.setString(4, FunctionHelper.encryptPassword(user.getPassword()));
            statement.setBoolean(5, false);
            statement.setString(6, "false");
            statement.setTimestamp(7, DateHelper.timestampFromDate(user.getBirthDate()));
            statement.setString(8, user.getPhoneNumber());
            statement.setString(9, user.getSpeciality());
            statement.setTimestamp(10, DateHelper.timestampFromDate(user.getHiringDate()));
            statement.setString(11, RoleEnum.ROLE_USER.toString());
            statement.executeUpdate();
            log.info("user created successfully");
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }

    }

    @Override
    public User getEntityByID(Long id) throws MyCustomException {
        String req = "select * from user where id = ?";
        User user = new User();

        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new MyCustomException("Erreur, utilisateur non existant");
            }
            user = new User().mapFromResultSet(resultSet);

            log.info(String.format("user available with id: %d", user.getId()));
            return user;
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return null;
    }

    @Override
    public User getEntityByCriteriaSingle(Map<String, Object> criteriaMap) throws MyCustomException {
        User user = new User();
        try (PreparedStatement statement = FunctionHelper.preparedStCriteria("user", criteriaMap, connection)) {
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                throw new MyCustomException("Erreur, utilisateur non existant");
            }
            user = new User().mapFromResultSet(resultSet);
            log.info(String.format("user available with id: %d", user.getId()));
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return user;
    }

    @Override
    public ObservableList<User> getEntityByCriteriaList(Map<String, Object> criteriaMap) {
        ObservableList<User> users = FXCollections.observableArrayList();
        try (PreparedStatement statement = FunctionHelper.preparedStCriteria("user", criteriaMap, connection)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User p = new User().mapFromResultSet(resultSet);
                users.add(p);
            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return users;
    }

    @Override
    public ObservableList<User> getAll() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String req = "select * from user";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(req);
            while (resultSet.next()) {
                User p = new User().mapFromResultSet(resultSet);
                users.add(p);
            }
            resultSet.close();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return users;
    }

    @Override
    public void updateEntity(User user) throws MyCustomException {

        String req = "update user set username = ? , email=?, is_verified=?, profile_pic=?, birthdate=?, phone_number=?, speciality=?, hiring_date=?, roles=? where id = ?";
        JSONArray roles = new JSONArray(user.getRoles());
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setBoolean(3, user.isVerified());
            statement.setString(4, user.getProfilePic());
            if ((user.getRoles()).contains(RoleEnum.ROLE_ADMIN)) {
                statement.setTimestamp(5, null);
                statement.setString(6, user.getPhoneNumber());
                statement.setString(7, null);
                statement.setTimestamp(8, null);
            } else if (user.getRoles().contains(RoleEnum.ROLE_FORMATEUR)) {
                statement.setTimestamp(5, DateHelper.timestampFromDate(user.getBirthDate()));
                statement.setString(6, user.getPhoneNumber());
                statement.setString(7, user.getSpeciality());
                statement.setTimestamp(8, DateHelper.timestampFromDate(user.getHiringDate()));
            } else {
                statement.setTimestamp(5, null);
                statement.setString(6, null);
                statement.setString(7, null);
                statement.setTimestamp(8, null);
            }
            statement.setObject(9, roles.toString());
            statement.setLong(10, user.getId());
            statement.executeUpdate();
            log.info("user updated successfully");
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new MyCustomException("Email déja utilisé");
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }

    }

    @Override
    public void deleteEntityById(Long id) {
        String req = "delete from user where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }

    }

    @Override
    public void lockUser(Long id) {
        String req = "update user set locked = not locked WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public void changePassword(String email, String password) {
        String req = "update user set password = ? where email = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setString(1, FunctionHelper.encryptPassword(password));
            statement.setString(2, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public void enableUser(String email) {
        String req = "update user set is_verified = ? where email = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setBoolean(1, true);
            statement.setString(2, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public Long getCurrentUserID() {
        Preferences userPreferences = Preferences.userRoot();

        return userPreferences.getLong(StringCnst.user_id_key, 0);
    }

    @Override
    public User getCurrentLoggedinUser() throws MyCustomException {
        Preferences userPreferences = Preferences.userRoot();

        Long id = userPreferences.getLong(StringCnst.user_id_key, 0);
        return getEntityByID(id);
    }

    @Override
    public ObservableList<User> getUsersByRole(RoleEnum role) {
        ObservableList<User> users = FXCollections.observableArrayList();
        String req = "SELECT * FROM user WHERE json_contains(roles, '[\"" + role + "\"]')";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(req);
            while (resultSet.next()) {
                User p = new User().mapFromResultSet(resultSet);
                users.add(p);
            }
            resultSet.close();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return users;
    }
}
