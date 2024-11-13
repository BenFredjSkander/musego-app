package com.tn.musego.utils;

import lombok.extern.java.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executors;

@Log
public class DBConnection {
    private static DBConnection instance = null;
    Connection connection = null;
    private String propetiesFile = "project.properties";

    public DBConnection() {
        try {
            FunctionHelper fh = new FunctionHelper();
            String url = fh.getProperty(propetiesFile, "url");
            String username = fh.getProperty(propetiesFile, "username");
            String password = fh.getProperty(propetiesFile, "password");
            connection = DriverManager.getConnection(url, username, password);
            connection.setNetworkTimeout(Executors.newFixedThreadPool(3), 3600000);//fix disconnecting remote database
            log.info("connection established");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
    }

    public static DBConnection getInstance() {
        if (DBConnection.instance == null) {
            DBConnection.instance = new DBConnection();
        }
        return DBConnection.instance;
    }

    public Connection getConnection() {
        return DBConnection.getInstance().connection;
    }
}