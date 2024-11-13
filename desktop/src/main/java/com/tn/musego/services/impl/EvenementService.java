package com.tn.musego.services.impl;

import com.tn.musego.entities.Evenements;
import com.tn.musego.services.IEvenementServie;
import com.tn.musego.utils.DBConnection;
import com.tn.musego.utils.DateHelper;
import lombok.extern.java.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log
public class EvenementService implements IEvenementServie {

    Connection connection;

    public EvenementService() {
        connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void ajouterEvenement(Evenements evenement) {
        String requete = "INSERT INTO evenement (nom,date_debut,date_fin,description,lieu,nb_participants,poster,type) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement pst = connection.prepareStatement(requete)) {
            pst.setString(1, evenement.getNom());
            pst.setTimestamp(2, DateHelper.timestampFromDate(evenement.getDateDebut()));
            pst.setTimestamp(3, DateHelper.timestampFromDate(evenement.getDateFin()));
            pst.setString(4, evenement.getDescription());
            pst.setString(5, evenement.getLieu());
            pst.setInt(6, 0);
            pst.setString(7, evenement.getPoster());
            pst.setString(8, evenement.getType());
            pst.executeUpdate();
            log.info("evenement  ajouté avec succes ! ");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
    }

    @Override
    public List<Evenements> afficherEvenements(String what) {
        List<Evenements> mylist = new ArrayList<>(); //on  a deplacé lbara   khatr ken nhotoha fel try maach yakraha ///

        if (what.equals("all")) {
            mylist = listeEvenements();
        } else {
            String requete = "SELECT * FROM evenement WHERE nom LIKE CONCAT( '%',?,'%')";
            try (PreparedStatement st = connection.prepareStatement(requete)) {
                st.setString(1, what);
                ResultSet rs = st.executeQuery();

                while (rs.next()) {
                    /// injercter les valeurs de la requete//

                    Evenements event = new Evenements();

                    event.setId(rs.getInt(1));
                    event.setNom(rs.getString("nom"));
                    event.setDateDebut(rs.getDate("date_debut"));
                    event.setDateFin(rs.getDate("date_fin"));
                    event.setDescription(rs.getString("description"));
                    event.setNbParticipants(rs.getInt("nb_participants"));
                    event.setPoster(rs.getString("poster"));
                    event.setType(rs.getString("type"));
                    event.setLieu(rs.getString("lieu"));

                    mylist.add(event);

                }

            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }


        return mylist;
    }


    @Override
    public void modifierEvenement(Evenements evenement) {
        String requete = "UPDATE `evenement` SET `nom` = ?, `date_debut` = ?, `date_fin` = ?, `type` = ?, `lieu` = ?, `description` = ?, `poster` = ?, `nb_participants` = ? WHERE `evenement`.`id`= ?";
        try (PreparedStatement pst = connection.prepareStatement(requete)) {
            pst.setString(1, evenement.getNom());
            pst.setTimestamp(2, DateHelper.timestampFromDate(evenement.getDateDebut()));
            pst.setTimestamp(3, DateHelper.timestampFromDate(evenement.getDateFin()));
            pst.setString(4, evenement.getType());
            pst.setString(5, evenement.getLieu());
            pst.setString(6, evenement.getDescription());
            pst.setString(7, evenement.getPoster());
            pst.setInt(8, evenement.getNbParticipants());
            pst.setInt(9, (int) evenement.getId());
            pst.executeUpdate();
            log.info("Evenement updated !");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
    }

    @Override
    public void supprimerEvenement(int id) {
        String req = "delete from evenement where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public boolean evenementExisteDeja(String nom) {
        boolean evenementExiste = false;
        // Recherche d'un événement avec le même nom
        String query = "SELECT * FROM evenement WHERE nom = ?";
        try (PreparedStatement preparedStmt = connection.prepareStatement(query)) {

            preparedStmt.setString(1, nom);
            ResultSet rs = preparedStmt.executeQuery();

            if (rs.next()) {
                // Un événement avec le même nom existe déjà
                evenementExiste = true;
            }

            rs.close();
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }

        return evenementExiste;
    }

    @Override
    public List<Evenements> listeEvenements() {
        List<Evenements> evenements = new ArrayList<>();
        String requete = "SELECT * FROM evenement";
        try (PreparedStatement pst = connection.prepareStatement(requete)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                Date dateDebut = rs.getDate("date_debut");
                Date dateFin = rs.getDate("date_fin");
                String type = rs.getString("type");
                String lieu = rs.getString("lieu");
                String description = rs.getString("description");
                String poster = rs.getString("poster");
                int nbParticipants = rs.getInt("nb_participants");
                Evenements evenement = new Evenements(id, nom, dateDebut, dateFin, type, lieu, description, poster, nbParticipants);
                evenements.add(evenement);
            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
        return evenements;

    }

}
