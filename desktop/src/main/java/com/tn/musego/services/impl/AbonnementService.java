package com.tn.musego.services.impl;

import com.tn.musego.entities.Abonnement;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.IAbonnementService;
import com.tn.musego.utils.DBConnection;
import com.tn.musego.utils.DateHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.java.Log;

import java.sql.*;

@Log
public class AbonnementService implements IAbonnementService {
    Connection connection;

    public AbonnementService() {
        this.connection = new DBConnection().getConnection();
    }

    @Override
    public void createEntity(Abonnement abonnement) {
        String req = "insert into abonnement (type, date_debut, date_fin, prix, id_offre, id_user) " +
                "values (?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setString(1, abonnement.getType().toString());
            statement.setTimestamp(2, DateHelper.timestampFromDate(abonnement.getDateDebut()));
            statement.setTimestamp(3, DateHelper.timestampFromDate(abonnement.getDateFin()));
            statement.setDouble(4, abonnement.getPrix());
            statement.setLong(5, abonnement.getIdOffre());
            statement.setLong(6, abonnement.getIdUser());
            statement.executeUpdate();
            log.info("abonnement created successfully");
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public Abonnement getEntityByID(Long id) throws MyCustomException {
        String req = "select * from abonnement where id = ?";
        Abonnement abonnement = new Abonnement();

        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new MyCustomException("Erreur, abonnement non existant");
            }

            abonnement = new Abonnement().mapFromResultSet(resultSet);

            log.info(String.format("Abonnement available with id: %d", abonnement.getId()));
            return abonnement;
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return null;
    }

    @Override
    public Abonnement getEntityByUserID(Long id) {
        String req = "select * from abonnement where id_user = ?";
        Abonnement abonnement = new Abonnement();
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new MyCustomException("Erreur, abonnement non existant");
            }
            abonnement = new Abonnement().mapFromResultSet(resultSet);
            log.info(String.format("Abonnement available with id: %d", abonnement.getId()));
            return abonnement;
        } catch (SQLException | MyCustomException e) {
            log.severe(e.getMessage());
        }
        return null;
    }


    @Override
    public ObservableList<Abonnement> getAll() {
        ObservableList<Abonnement> abonnements = FXCollections.observableArrayList();
        String req = "select * from abonnement";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(req);
            while (resultSet.next()) {
                Abonnement abonnement = new Abonnement().mapFromResultSet(resultSet);
                abonnements.add(abonnement);
            }
            resultSet.close();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return abonnements;
    }

    @Override
    public void updateEntity(Abonnement abonnement) {
        String req = "update abonnement set type = ? , date_debut=?, date_fin=?, prix=?, id_offre=?, id_user=? where id = ?";

        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setString(1, abonnement.getType().toString());
            statement.setTimestamp(2, DateHelper.timestampFromDate(abonnement.getDateDebut()));
            statement.setTimestamp(3, DateHelper.timestampFromDate(abonnement.getDateFin()));
            statement.setDouble(4, abonnement.getPrix());
            statement.setLong(5, abonnement.getIdOffre());
            statement.setLong(6, abonnement.getIdUser());
            statement.setLong(7, abonnement.getId());
            statement.executeUpdate();
            log.info("abonnement updated successfully");
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public void deleteEntityById(Long id) {
        String req = "delete from abonnement where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public void deleteEntityByUserIdOffreId(Long userId, Long offreId) {
        String req = "delete from abonnement where id_offre = ? and id_user = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setLong(1, offreId);
            statement.setLong(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public void deleteEntityByUserId(Long userId) {
        String req = "delete from abonnement where id_user = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public boolean isUserSubscribed(long userId, long offreId) {
        String query = "SELECT * FROM abonnement WHERE id_offre = ? AND id_user = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, offreId);
            preparedStatement.setLong(2, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean haveActiveSubscription(Long userid) {
        String query = "SELECT * FROM abonnement WHERE  id_user = ? AND DATE_ADD(CURDATE(), INTERVAL 1 day) between date_debut and date_fin";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
        return false;
    }

    @Override
    public Abonnement getActiveSubscription(Long userid) {
        String query = "SELECT * FROM abonnement WHERE  id_user = ?";
        Abonnement abonnement = new Abonnement();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userid);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new MyCustomException("Erreur, abonnement non existant");
            }
            abonnement = new Abonnement().mapFromResultSet(resultSet);
            log.info(String.format("Abonnement available with id: %d", abonnement.getId()));
            return abonnement;
        } catch (SQLException | MyCustomException e) {
            log.severe(e.getMessage());
        }
        return null;
    }

}
