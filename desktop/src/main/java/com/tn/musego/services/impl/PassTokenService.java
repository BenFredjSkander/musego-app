package com.tn.musego.services.impl;

import com.tn.musego.entities.PassResetToken;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.IPassTokenService;
import com.tn.musego.utils.DBConnection;
import com.tn.musego.utils.DateHelper;
import lombok.extern.java.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Skander Ben Fredj
 * @created 26-Feb-23
 * @project musego
 */
@Log
public class PassTokenService implements IPassTokenService {

    Connection connection = null;

    public PassTokenService() {
        connection = DBConnection.getInstance().getConnection();
    }

    //method used for unit tests
    public PassTokenService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addToken(PassResetToken resetToken) throws MyCustomException {
        String req = "insert into pass_token (created_date, token, used, id_user) VALUES (?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(req);
            statement.setTimestamp(1, DateHelper.currentTime());
            statement.setString(2, resetToken.getToken());
            statement.setBoolean(3, resetToken.isUsed());
            statement.setLong(4, resetToken.getIdUser());
            statement.executeUpdate();
            statement.close();
            log.info("token inserted");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void tokenExist(String token) throws MyCustomException {
        String req = "select * from pass_token where token = ? and used = false";
        try {
            PreparedStatement statement = connection.prepareStatement(req);
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new MyCustomException("Code invalide");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (MyCustomException e) {
            throw e;
        }
    }

    @Override
    public void destroyToken(String token) {
        String req = "update pass_token set used = true where token = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(req);
            statement.setString(1, token);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
