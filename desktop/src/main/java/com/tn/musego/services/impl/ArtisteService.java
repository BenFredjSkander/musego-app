package com.tn.musego.services.impl;

import com.tn.musego.entities.Artiste;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.IArtisteService;
import com.tn.musego.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.java.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.tn.musego.utils.DateHelper.timestampFromDate;

@Log
public class ArtisteService implements IArtisteService {
    Connection conn;

    public ArtisteService() {
        this.conn = DBConnection.getInstance().getConnection();
    }


    @Override
    public void ajouterArtiste(Artiste a) {
        String req = "Insert into artiste (id,nom,prenom,cin,email,date_naissance,adresse,specialite,nationalite,image) values (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = conn.prepareStatement(req)) {

            statement.setInt(1, a.getId());
            statement.setString(2, a.getNom());
            statement.setString(3, a.getPrenom());
            statement.setInt(4, a.getCin());
            statement.setString(5, a.getEmail());
            statement.setTimestamp(6, timestampFromDate(a.getDateDeNaissance()));
            statement.setString(7, a.getAdresse());
            statement.setString(8, a.getSpecialite());
            statement.setString(9, a.getNationalite());
            statement.setString(10, a.getImage());
            statement.executeUpdate();
            log.info("Artiste ajouté");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
    }

    @Override
    public void modifierArtiste(Artiste a) {
        String req = "UPDATE artiste SET id = ?,nom = ?,prenom = ?,cin = ?, email = ?,date_naissance = ?,adresse = ?,specialite = ?,nationalite = ?,image = ? WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(req)) {

            statement.setInt(1, a.getId());
            statement.setString(2, a.getNom());
            statement.setString(3, a.getPrenom());
            statement.setInt(4, a.getCin());
            statement.setString(5, a.getEmail());
            statement.setTimestamp(6, timestampFromDate(a.getDateDeNaissance()));
            statement.setString(7, a.getAdresse());
            statement.setString(8, a.getSpecialite());
            statement.setString(9, a.getNationalite());
            statement.setString(10, a.getImage());
            statement.setInt(11, a.getId());
            statement.executeUpdate();
            log.info("Artiste modifié !");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }


    }

    @Override
    public void supprimerArtiste(int id) {
        String req = "delete from artiste where id = ?";
        try (PreparedStatement statement = conn.prepareStatement(req)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public ObservableList<Artiste> afficherArtistes() {
        ObservableList<Artiste> artistes = FXCollections.observableArrayList();
        String req = "SELECT * FROM `artiste`";
        try (PreparedStatement statement = conn.prepareStatement(req)) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Artiste resultArtiste = new Artiste().mapFromResultSet(result);
                artistes.add(resultArtiste);
            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
        return artistes;
    }

    @Override
    public Artiste afficherUnArtiste(Integer s) throws MyCustomException {
        String req = "select * from artiste where id = ?";
        Artiste artiste = new Artiste();

        try (PreparedStatement statement = conn.prepareStatement(req)) {
            statement.setLong(1, s);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new MyCustomException("Erreur, artiste non existant");
            }
            artiste = new Artiste().mapFromResultSet(resultSet);

            return artiste;
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return null;
    }


}
