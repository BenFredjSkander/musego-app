package com.tn.musego.services.impl;

import com.tn.musego.entities.Sponsor;
import com.tn.musego.services.ISponsorService;
import com.tn.musego.utils.DBConnection;
import lombok.extern.java.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log
public class SponsorService implements ISponsorService {
    Connection connection = null;

    public SponsorService() {
        connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void ajouterSponsor(Sponsor sponsor) {
        String requete = "INSERT INTO sponsor (nom, photo, capacite_fin, id_evenement) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(requete)) {
            pst.setString(1, sponsor.getNom());
            pst.setString(2, sponsor.getPhoto());
            pst.setInt(3, sponsor.getCapaciteFin());
            pst.setInt(4, sponsor.getIdEvenement());

            pst.executeUpdate();
            log.info("Sponsor ajouté avec succès !");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
    }


    @Override
    public List<Sponsor> afficherSponsors() {
        List<Sponsor> sponsors = new ArrayList<>();
        String requete = "SELECT * FROM sponsor";

        try (Statement st = connection.createStatement();
        ) {
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {

                Sponsor sponsor = new Sponsor();
                sponsor.setId(rs.getLong("id"));
                sponsor.setNom(rs.getString("nom"));
                sponsor.setPhoto(rs.getString("photo"));
                sponsor.setCapaciteFin(rs.getInt("capacite_fin"));
                sponsor.setIdEvenement(rs.getInt("id_evenement"));
                sponsors.add(sponsor);
            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }

        return sponsors;
    }

    @Override
    public void modifierSponsor(Sponsor sponsor) {
        String requete = "UPDATE sponsor SET nom = ?, photo = ?, capacite_fin = ?,id_evenement = ? WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(requete)) {
            pst.setString(1, sponsor.getNom());
            pst.setString(2, sponsor.getPhoto());
            pst.setInt(3, sponsor.getCapaciteFin());
            pst.setInt(4, sponsor.getIdEvenement());
            pst.setLong(5, sponsor.getId());

            pst.executeUpdate();
            log.info("Sponsor modifié avec succès !");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
    }


    @Override
    public void supprimerSponsor(int id) {
        String requete = "DELETE FROM sponsor WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(requete);
        ) {

            pst.setLong(1, id);

            pst.executeUpdate();
            log.info("Sponsor supprimé avec succès !");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
    }
}
