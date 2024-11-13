package com.tn.musego.services.impl;

import com.tn.musego.entities.Categorie;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.ICategorieService;
import com.tn.musego.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.java.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



@Log
public class CategorieService implements ICategorieService {
    Connection conn;

    public CategorieService() {
        this.conn = DBConnection.getInstance().getConnection();
    }


    @Override
    public ObservableList<Categorie> afficherCategories() {
        ObservableList<Categorie> artistes = FXCollections.observableArrayList();
        String req = "SELECT * FROM categorie";
        try (PreparedStatement statement = conn.prepareStatement(req)) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Categorie resultCategorie = new Categorie().mapFromResultSet(result);
                artistes.add(resultCategorie);
            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
        return artistes;
    }

    @Override
    public Categorie afficherUnCategorie(Integer s) throws MyCustomException {
        String req = "select * from artiste where id = ?";
        Categorie artiste = new Categorie();

        try (PreparedStatement statement = conn.prepareStatement(req)) {
            statement.setLong(1, s);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new MyCustomException("Erreur, artiste non existant");
            }
            artiste = new Categorie().mapFromResultSet(resultSet);

            return artiste;
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return null;
    }

}
