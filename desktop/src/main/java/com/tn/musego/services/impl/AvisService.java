package com.tn.musego.services.impl;

import com.tn.musego.entities.Avis;
import com.tn.musego.services.IAvisService;
import com.tn.musego.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AvisService implements IAvisService {
    Connection connection = DBConnection.getInstance().getConnection();

    public AvisService() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void ajouterAvis(Avis avis) {

        try {

            // Requête SQL pour insérer un nouvel avis
            String query = "INSERT INTO `avis` (type, description , avis_sur, id_user) VALUES (?, ?, ?,?)";
            PreparedStatement a = connection.prepareStatement(query);

            a.setString(1, avis.getType());
            a.setString(2, avis.getDescription());
            a.setString(3, avis.getAvisSur());
            a.setLong(4, avis.getIdUser());


            a.executeUpdate();
            a.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création de l'avis : " + e.getMessage());
        }

    }

    @Override
    public void supprimerAvis(int id) {
        try {
            // Requête SQL pour supprimer un avis par son id
            String query = "DELETE FROM avis WHERE id= ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'avis : " + e.getMessage());
        }
    }


    /*@Override
    public void modifierAvis(Avis a) {
        try {
            // Requête SQL pour mettre à jour un avis
            String query = "UPDATE `avis` SET typee=?, description=?, avis_sur=?, =? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,Avis.getType());
            ps.setString(2, Avis.getDescription());
            ps.setString(3, Avis.getAvis_sur());
            ps.setInt(4, Avis.getId_user());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'avis : " + e.getMessage());
        }

    }*/
    public void modifierAvis(Avis a) {
        try {
            //nb: on ne peut pas modifier la date
            String requete4 = "UPDATE avis SET type = ?, description = ?, avis_sur = ?  WHERE id = ?";
            PreparedStatement st = connection.prepareStatement(requete4);
            st.setString(1, a.getType());
            st.setString(2, a.getDescription());
            st.setString(3, a.getAvisSur());
            st.setLong(4, a.getId());
            st.executeUpdate(requete4);
            System.out.println("modification avec succes");
        } catch (SQLException ex) {
            System.out.println("erreur de modification");
            System.out.println(ex);
        }
    }

    @Override
    public List<Avis> afficherAvis() {
        List<Avis> myList = new ArrayList<>();

        try {
            String requete = "SELECT * FROM `avis`";
            Statement pst = connection.createStatement();
            ResultSet rs = pst.executeQuery(requete);

            while (rs.next()) {
                Avis m = new Avis();
                m.setId(rs.getInt("id"));
                m.setType(rs.getString("type"));
                m.setDescription(rs.getString("description"));
                m.setAvisSur(rs.getString("avis_sur"));
                m.setIdUser(rs.getInt("id_user"));
                myList.add(m);


            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

        }

        return myList;

    }

    public List<Avis> rechercherAvis(int idUser) {

        List<Avis> avis = new ArrayList<Avis>();
        try {
            String requette = "SELECT * FROM `avis` WHERE id_user = ?";
            Statement pst = connection.createStatement();
            ResultSet result = pst.executeQuery(requette);

            while (result.next()) {

                Avis resultAvis = new Avis(result.getString("type"), result.getString("description"), result.getString("avis_sur"), result.getInt("id_user"));
                avis.add(resultAvis);
            }
            System.out.println(avis);
            System.out.println("Avis trouvé !");
        } catch (SQLException ex) {
            System.out.println("Avis non trouvé !");
            System.out.println(ex.getMessage());
        }
        return avis;
    }
}






