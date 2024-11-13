package com.tn.musego.services.impl;

import com.tn.musego.entities.Atelier;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.IAtelierService;
import com.tn.musego.utils.DBConnection;
import com.tn.musego.utils.DateHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.java.Log;

import java.sql.*;

@Log
public class AtelierService implements IAtelierService {
    Connection connection;

    public AtelierService() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void createEntity(Atelier a) {
        String requete = "INSERT INTO `atelier` (nom,created_at,image)"
                + "VALUES (?,?,?)";
        try (PreparedStatement pst = connection.prepareStatement(requete)) {

            pst.setString(1, a.getNom());
            pst.setTimestamp(2, DateHelper.currentTime());
            pst.setString(3, a.getImage());

            pst.executeUpdate();
            //getLong(),getString(),getString(),getInt(),getDate(),getDate(),getString(),getLong()
            log.info("Atelier ajoutée avec succès!");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
    }

    @Override
    public Atelier getEntityByID(Long id) throws MyCustomException {
        String requete = "SELECT * FROM `atelier` where id = ?";
        try (PreparedStatement pst = connection.prepareStatement(requete)) {
            pst.setLong(1, id);
            System.out.println(pst);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                throw new MyCustomException("Erreur, Atelier non existant");
            }
            Atelier a = new Atelier().mapFromResultSet(rs);
            System.out.println(a);
            return a;
        } catch (Exception ex) {
            log.severe(ex.getMessage());
        }
        return null;
    }

    @Override
    public ObservableList<Atelier> getAll() {
        ObservableList<Atelier> myList = FXCollections.observableArrayList();
        String requete = "select a.id, a.nom, a.created_at,a.image from atelier a;";

        try (Statement pst = connection.createStatement()) {
            ResultSet rs = pst.executeQuery(requete);
            while (rs.next()) {
                Atelier a = new Atelier().mapFromResultSet(rs);
                myList.add(a);

            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }

        return myList;
    }

    @Override
    public void updateEntity(Atelier a) throws MyCustomException {
        String requete = "UPDATE `atelier`SET `nom` = ?, `image` = ? WHERE `atelier`.`id`=?";
        try (PreparedStatement pst = connection.prepareStatement(requete)) {
            pst.setString(1, a.getNom());
            pst.setString(2, a.getImage());
            pst.setLong(3, a.getId());
            pst.executeUpdate();
            log.info("Atelier updated !");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }

    }

    @Override
    public void deleteEntityById(Long id) {
        String requete = "DELETE FROM atelier WHERE id =?";

        try (PreparedStatement pst = connection.prepareStatement(requete)) {
            pst.setLong(1, id);
            pst.executeUpdate();
            log.info("Suppression Atelier avec Succès");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }

    }
}
