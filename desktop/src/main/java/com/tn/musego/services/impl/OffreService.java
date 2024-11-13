package com.tn.musego.services.impl;

import com.tn.musego.entities.Offre;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.IOfferService;
import com.tn.musego.utils.DBConnection;
import com.tn.musego.utils.DateHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.java.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Log
public class OffreService implements IOfferService {
    Connection connection = null;

    public OffreService() {
        this.connection = new DBConnection().getConnection();
    }

    @Override
    public void createEntity(Offre offre) {
        String req = "insert into offre (type,image,prix, promotion, date_debut, date_fin,description) " +
                "values (?,?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setString(1, offre.getType().toString());
            statement.setString(2, offre.getImage());
            statement.setDouble(3, offre.getPrix());
            statement.setDouble(4, offre.getPromotion());
            statement.setTimestamp(5, DateHelper.timestampFromDate(offre.getDateDebut()));
            statement.setTimestamp(6, DateHelper.timestampFromDate(offre.getDateFin()));
            statement.setString(7, offre.getDescription());
            statement.executeUpdate();
            log.info("offre created successfully");
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public Offre getEntityByID(Long id) {
        String req = "select * from offre where id = ?";
        Offre offre = new Offre();

        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new MyCustomException("Erreur, offre non existant");
            }
            offre = new Offre().mapFromResultSet(resultSet);
            log.info(String.format("Offre available with id: %d", offre.getId()));
            return offre;
        } catch (SQLException | MyCustomException e) {
            log.severe(e.getMessage());
        }
        return null;
    }


    @Override
    public ObservableList<Offre> getAll() {
        ObservableList<Offre> offres = FXCollections.observableArrayList();
        String req = "select * from offre";
        try (PreparedStatement statement = connection.prepareStatement(req)
        ) {
            ResultSet resultSet = statement.executeQuery(req);
            while (resultSet.next()) {
                Offre offre = new Offre().mapFromResultSet(resultSet);
                offres.add(offre);
            }
            resultSet.close();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return offres;
    }

    @Override
    public void updateEntity(Offre offre) {
        String req = "update offre set type = ? , image=?,prix = ? , promotion=?, date_debut=?, date_fin=?, description=? where id = ?";

        try (PreparedStatement statement = connection.prepareStatement(req)
        ) {
            statement.setString(1, offre.getType().toString());
            statement.setString(2, offre.getImage());
            statement.setDouble(3, offre.getPrix());
            statement.setDouble(4, offre.getPromotion());
            statement.setTimestamp(5, DateHelper.timestampFromDate(offre.getDateDebut()));
            statement.setTimestamp(6, DateHelper.timestampFromDate(offre.getDateFin()));
            statement.setString(7, offre.getDescription());
            statement.setLong(8, offre.getId());
            statement.executeUpdate();
            log.info("offre updated successfully");
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public void deleteEntityById(Long id) {
        String req = "delete from offre where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

}
