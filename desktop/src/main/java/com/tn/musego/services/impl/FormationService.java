package com.tn.musego.services.impl;

import com.tn.musego.entities.Formation;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.IFormationService;
import com.tn.musego.utils.DBConnection;
import com.tn.musego.utils.DateHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.java.Log;

import java.sql.*;

@Log
public class FormationService implements IFormationService {

    Connection connection;

    public FormationService() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void createEntity(Formation f) {
        String requete = "INSERT INTO `formation` (nom,date_debut,date_fin,niveau,id_formateur,atelier_id)"
                + "VALUES (?,?,?,?,?,?)";
        try (PreparedStatement pst = connection.prepareStatement(requete)) {

            pst.setString(1, f.getNom());
            pst.setTimestamp(2, DateHelper.timestampFromDate(f.getDateDebut()));
            pst.setTimestamp(3, DateHelper.timestampFromDate(f.getDateFin()));
            pst.setString(4, f.getNiveau().label);
            pst.setLong(5, f.getIdFormateur());
            pst.setLong(6, f.getIdAtelier());

            pst.executeUpdate();
            log.info("Formation Ajoutée");


            //getLong(),getString(),getString(),getInt(),getDate(),getDate(),getString(),getLong()
            log.info("Formation ajoutée avec succès!");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
    }

    @Override
    public Formation getEntityByID(Long id) throws MyCustomException {
        String requete = "SELECT * FROM `formation` where id = ?";
        try (PreparedStatement pst = connection.prepareStatement(requete)) {
            pst.setLong(1, id);
            System.out.println(pst);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                throw new MyCustomException("Erreur, formation non existante");
            }
            Formation f = new Formation().mapFromResultSet(rs);
            System.out.println(f);
            return f;
        } catch (Exception ex) {
            log.severe(ex.getMessage());
        }
        return null;
    }

    @Override
    public ObservableList<Formation> getByAtelier(long atelier) {
        ObservableList<Formation> myList = FXCollections.observableArrayList();
        String requete = "select f.id, f.nom, f.atelier_id, f.date_debut, f.date_fin, f.niveau, f.id_formateur, u.username as 'nom_formateur', a.nom as 'nom_atelier' from formation f left join user u on f.id_formateur = u.id left JOIN atelier a on f.atelier_id = a.id where atelier_id = ?";

        try (PreparedStatement pst = connection.prepareStatement(requete)) {
            pst.setLong(1, atelier);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Formation f = new Formation().mapFromResultSetWithAtelierAndFormateur(rs);
                myList.add(f);
            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }

        return myList;
    }

    @Override
    public ObservableList<Formation> getAll() {
        ObservableList<Formation> myList = FXCollections.observableArrayList();
        String requete = "select f.id, f.nom, f.atelier_id, f.date_debut, f.date_fin, f.niveau, f.id_formateur,  u.username as 'nom_formateur', a.nom as 'nom_atelier' from formation f left join user u on f.id_formateur = u.id left JOIN atelier a on f.atelier_id = a.id;";

        try (Statement pst = connection.createStatement()) {
            ResultSet rs = pst.executeQuery(requete);
            while (rs.next()) {
                Formation f = new Formation().mapFromResultSetWithAtelierAndFormateur(rs);
                myList.add(f);

            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }

        return myList;
    }

    @Override
    public void deleteEntityById(Long id) {
        String requete = "DELETE FROM formation WHERE id =?";

        try (PreparedStatement pst = connection.prepareStatement(requete)) {
            pst.setLong(1, id);
            pst.executeUpdate();
            log.info("Suppression formateur confirmée!");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }


    }


    @Override
    public void updateEntity(Formation f) {
        String requete = "UPDATE `formation` SET `nom` = ?, `atelier_id` = ?, `date_debut` = ?, `date_fin` = ?, `niveau` = ?, `id_formateur` = ? WHERE `formation`.`id`=?";
        try (PreparedStatement pst = connection.prepareStatement(requete)) {
            pst.setString(1, f.getNom());
            pst.setLong(2, f.getIdAtelier());
            pst.setTimestamp(3, DateHelper.timestampFromDate(f.getDateDebut()));
            pst.setTimestamp(4, DateHelper.timestampFromDate(f.getDateFin()));
            pst.setString(5, f.getNiveau().label);
            pst.setLong(6, f.getIdFormateur());
            pst.setLong(7, f.getId());

            pst.executeUpdate();
            log.info("Formation updated !");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }

    }
}
