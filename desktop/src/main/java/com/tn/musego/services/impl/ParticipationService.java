package com.tn.musego.services.impl;

import com.tn.musego.entities.Participation;
import com.tn.musego.services.IParticipationService;
import com.tn.musego.utils.DBConnection;
import lombok.extern.java.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log
public class ParticipationService implements IParticipationService {
    Connection connection = null;

    public ParticipationService() {
        connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void ajouterParticipation(int idUser, int idEvenement) {
        String requete = "INSERT INTO participation (id_user,id_evenement) VALUES (?,?)";
        String requete2 = "UPDATE `evenement` SET nb_participants = nb_participants + 1 where id = ? ";

        try (PreparedStatement pst = connection.prepareStatement(requete);
             PreparedStatement pst2 = connection.prepareStatement(requete2)) {
            pst.setInt(1, idUser);
            pst.setInt(2, idEvenement);
            pst.executeUpdate();
//////////requete 2 bech nincrementiw nb partcip  fel table event  ///////
            pst2.setInt(1, idEvenement);
            pst2.executeUpdate();
            log.info("participation ajouté avec succes ! ");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
    }


    @Override
    public void supprimerParticipation(int idUser, int idEvenement) {
        String requete = "DELETE FROM `participation` WHERE id_user = ? AND id_evenement = ? ;";
        String requete2 = "UPDATE `evenement` SET nb_participants = nb_participants - 1 where id = ? ";

        try (PreparedStatement pst = connection.prepareStatement(requete);
             PreparedStatement pst2 = connection.prepareStatement(requete2)) {

//////////requete 1 bech najoutiw fel table participation ///////

            pst.setInt(1, idUser);
            pst.setInt(2, idEvenement);
            pst.executeUpdate();
            log.info("participation supprimé  avec succes ! ");

//////////////nombre tee participation fel table events ydecremnti////////
            pst2.setInt(1, idEvenement);
            pst2.executeUpdate();

        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
    }


    @Override
    public List<Participation> afficherParticipations() {
        List<Participation> mylist = new ArrayList<>(); //on  a deplacé lbara   khatr ken nhotoha fel try maach yakraha ///
        String requete = " SELECT  *  FROM participation";

        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                /// injercter les valeurs de la requete//

                Participation participation = new Participation();

                participation.setIdUser(rs.getInt(1));
                participation.setIdEvenement(rs.getInt(2));

                mylist.add(participation);
            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
        return mylist;
    }


    @Override
    public List<String> afficherParticipationsbyid(int id) {
        List<String> participations = new ArrayList<>();
        String requete = "SELECT e.nom FROM participation p JOIN evenement e ON p.id_evenement = e.id WHERE p.id_user = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(requete)) {
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                participations.add(rs.getString("nom"));
            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }

        return participations;
    }

    @Override
    public boolean isUserAlreadyRegistered(int userId, int eventId) {
        String query = "SELECT * FROM participation WHERE id_user = ? AND id_evenement = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, eventId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }

        return false;
    }


}
