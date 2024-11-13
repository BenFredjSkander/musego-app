package com.tn.musego.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DBConnectionTest {

    private static Connection connection = null;
    private static Properties prop = null;

    private String getProperty(String key) {
        if (prop != null) {
            return prop.getProperty(key);

        } else {
            Properties prop = new Properties();
            InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream("project.properties");
            try {
                prop.load(inputStream);
                return prop.getProperty(key);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Test
    @DisplayName("Database properties")
    void getDatabaseProperties() {
        assertNotNull(getProperty("username"), "database user not found");
        assertNotNull(getProperty("username"), "database username not found");
        assertNotNull(getProperty("password"), "database password not found");
    }

    @Test
    @DisplayName("Database Connection")
    void getConnectionTest() {
        Connection dbConnection = DBConnection.getInstance().getConnection();
        assertNotNull(dbConnection, "connection should be successfull.");

    }
}
