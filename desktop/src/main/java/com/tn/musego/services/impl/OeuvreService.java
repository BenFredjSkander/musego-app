package com.tn.musego.services.impl;

import com.tn.musego.entities.Oeuvre;
import com.tn.musego.services.IOeuvreService;
import com.tn.musego.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.java.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.tn.musego.utils.DateHelper.timestampFromDate;

@Log
public class OeuvreService implements IOeuvreService {
    Connection connection;

    public OeuvreService() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void ajouterOeuvre(Oeuvre o) {
        String req = "INSERT INTO oeuvre (titre, date_creation, description, lieu_conservation, image,id_categorie,id_artiste) VALUES (?, ?, ?, ?, ?, ?, ?)";
        System.out.println("oooo"+req);
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setString(1, o.getTitre());
            statement.setTimestamp(2, timestampFromDate(o.getDateCreation()));
            statement.setInt(6, o.getCategorie());
            statement.setString(3, o.getDescription());
            statement.setString(4, o.getLieuDeConservartion());
            statement.setString(5, o.getImage());
            statement.setInt(7, o.getIdArtiste());

            System.out.println("eeeeeeee"+statement);
            statement.executeUpdate();
            log.info("Oeuvre ajoutée");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }

}


    @Override
    public void modifierOeuvre(Oeuvre o) {
        String req = "UPDATE oeuvre SET titre = ?,date_creation = ?, description = ?,lieu_conservation = ?,image = ?,id_artiste = ?,id_categorie = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setString(1, o.getTitre());
            statement.setTimestamp(2, timestampFromDate(o.getDateCreation()));
            statement.setInt(7, o.getCategorie());
            statement.setString(3, o.getDescription());
            statement.setString(4, o.getLieuDeConservartion());
            statement.setString(5, o.getImage());
            statement.setInt(6, o.getIdArtiste());
            statement.setString(8, String.valueOf(o.getId()));
            System.out.println("pppppppppppppppppppppppp"+statement);
            statement.executeUpdate();
            log.info("Oeuvre modifiée !");
        } catch (SQLException ex) {
            log.info(ex.getMessage());
        }
    }

    @Override
    public void supprimerOeuvre(int id) {
        String req = "DELETE FROM `oeuvre` WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public ObservableList<Oeuvre> afficherOeuvres() {
        ObservableList<Oeuvre> oeuvres = FXCollections.observableArrayList();
        String req = "SELECT o.*, CONCAT(a.nom, CONCAT(' ', a.prenom)) AS prenom, c.nom AS nom FROM oeuvre o JOIN artiste a ON o.id_artiste = a.id JOIN categorie c ON o.id_categorie = c.id";

        try (Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(req);
            while (result.next()) {
                Oeuvre resultOeuvre = new Oeuvre().mapFromResultSet(result);
                oeuvres.add(resultOeuvre);
            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
        return oeuvres;
    }
    @Override
    public ObservableList<Oeuvre> afficherAllOeuvres() {
        ObservableList<Oeuvre> oeuvres = FXCollections.observableArrayList();
        String req = "SELECT o.id,o.titre,o.date_creation,o.description,o.lieu_conservation,o.image, c.nom AS nom,CONCAT(a.nom, CONCAT(' ', a.prenom)) AS prenom FROM oeuvre o JOIN artiste a ON o.id_artiste = a.id left JOIN categorie c ON o.id_categorie = c.id";

        try (Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(req);

            while (result.next()) {
                Oeuvre resultOeuvre = new Oeuvre().mapFromResultSet2(result);
                System.out.println("ppppppppp"+resultOeuvre);
                oeuvres.add(resultOeuvre);
            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
        return oeuvres;
    }
    @Override
    public List<Oeuvre> afficherListOeuvres(String test) {
        List<Oeuvre> mylist = new ArrayList<>();
        if (test.equals("all")) {
            mylist = afficherOeuvres();
        } else {
            String req = "SELECT * FROM oeuvre WHERE titre LIKE CONCAT('%',?,'%')";
            try (PreparedStatement statement = connection.prepareStatement(req)) {
                statement.setString(1, test);
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    Oeuvre resultOeuvre = new Oeuvre().mapFromResultSet(result);
                    mylist.add(resultOeuvre);
                }
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }
        return mylist;
    }
}
