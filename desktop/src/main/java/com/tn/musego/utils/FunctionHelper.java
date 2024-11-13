package com.tn.musego.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Skander Ben Fredj
 * @created 13-Feb-23
 * @project musego
 */
@Log
public class FunctionHelper {

    /**
     * Encrypt a plain password based on the BCrypt algorithme
     *
     * @param plainText plain string containing the password
     * @return the encrypted password
     */
    public static String encryptPassword(@NonNull String plainText) {
        return BCrypt.withDefaults().hashToString(13, plainText.toCharArray());
    }

    /**
     * Check if the given plain password and the hashed password match
     *
     * @param hash      string containing the password
     * @param plainText string containing the hash
     * @return boolean value if the args match
     */
    public static boolean passwordMatch(@NonNull String hash, String plainText) {
        return BCrypt.verifyer().verify(plainText.toCharArray(), hash.toCharArray()).verified;
    }

    /**
     * generate uuid
     *
     * @return uuid
     */
    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * Method to return a prepared statement with N number of criteria
     *
     * @param tableName   name of the table to select from
     * @param criteriaMap Map containing as key the name of column and value as the object to find
     * @param connection  connection parameter from the service
     * @return a prepared statement to execute from the service
     * @throws SQLException
     */
    public static PreparedStatement preparedStCriteria(@NonNull String tableName,
                                                       Map<String, Object> criteriaMap, Connection connection) throws SQLException {

        StringBuilder req = new StringBuilder("select * from ").append(tableName);

        boolean first = true;

        for (String paramName : criteriaMap.keySet()) {
            Object paramValue = criteriaMap.get(paramName);
            if (paramValue != null) {
                if (first) {
                    req.append(" where ").append(paramName).append("=?");
                    first = false;
                } else {
                    req.append(" and ").append(paramName).append("=?");
                }
            }
        }
        try {
            PreparedStatement ps = connection.prepareStatement(req.toString());
            int paramIndex = 1;
            for (String paramName : criteriaMap.keySet()) {
                Object paramValue = criteriaMap.get(paramName);
                if (paramValue != null) {
                    if (paramValue instanceof Timestamp) {
                        ps.setTimestamp(paramIndex, (Timestamp) paramValue);
                    } else if (paramValue instanceof Integer) {
                        ps.setInt(paramIndex, (Integer) paramValue);
                    } else if (paramValue instanceof Boolean) {
                        ps.setBoolean(paramIndex, (Boolean) paramValue);
                    } else {
                        ps.setString(paramIndex, paramValue.toString());
                    }
                    paramIndex++;
                }
            }
            return ps;
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return null;
    }

    /**
     * Generate a unique id for the machine
     *
     * @return the unique id
     */
    public static String getMachineID() {
        String OS = System.getProperty("os.name").toLowerCase();
        String machineId = null;
        if (OS.indexOf("win") >= 0) {
            StringBuilder output = new StringBuilder();
            Process process;
            String[] cmd = {"wmic", "csproduct", "get", "UUID"};
            try {
                process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            } catch (Exception e) {
                log.severe(e.getMessage());
            }
            machineId = output.toString();
        } else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0) {

            StringBuilder output = new StringBuilder();
            Process process;
            String[] cmd = {"/bin/sh", "-c", "echo <password for superuser> | sudo -S cat /sys/class/dmi/id/product_uuid"};
            try {
                process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            machineId = output.toString();
        }
        String result;
        assert machineId != null;
        result = machineId.replace("UUID", "").trim();
        return result;
    }

    public static void disablePastDates(DatePicker picker) {
        picker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    public static void disableFutureDates(DatePicker picker) {
        picker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });
    }

    public static void openNewStage(String location, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FunctionHelper.class.getResource(location));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene sc = new Scene(root);
        sc.getStylesheets().add(FunctionHelper.class.getResource("/styles/styles.css")
                .toExternalForm());
        stage.setResizable(false);
        stage.getIcons().add(new Image(FunctionHelper.class.getResource("/images/icon.png").openStream()));
        stage.setTitle(title);
        stage.setScene(sc);
        stage.showAndWait();
    }

    /**
     * Get a property from a configuration file
     *
     * @param fileName    name of the config file
     * @param propertyKey name of the property key
     * @return the property value
     */
    public String getProperty(String fileName, String propertyKey) {
        try {
            Properties properties = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = loader.getResourceAsStream(fileName);
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file " + fileName + " not found in the classpath");
            }
            return properties.getProperty(propertyKey);
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
        return null;
    }
}

