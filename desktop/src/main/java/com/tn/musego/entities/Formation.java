package com.tn.musego.entities;


import com.tn.musego.entities.enums.Niveau;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Formation {
    private Long id;
    private String nom;
    private Date dateDebut;
    private Date dateFin;
    private Niveau niveau; /*avance,intermediaire,debutant*/
    private long idFormateur; /*clé étrangère*/
    private long idAtelier; /*clé étrangère*/
    private String nomAtelier;
    private String nomFormateur;

    /**
     * @param nom
     * @param dateDebut
     * @param dateFin
     * @param niveau
     * @param idFormateur
     * @param idAtelier
     */
    public Formation(String nom, Date dateDebut, Date dateFin, Niveau niveau, long idFormateur, long idAtelier) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.niveau = niveau;
        this.idFormateur = idFormateur;
        this.idAtelier = idAtelier;
    }

    public Formation mapFromResultSet(ResultSet resultSet) throws SQLException {
        return new Formation(
                resultSet.getString("nom"),
                resultSet.getDate("date_debut"),
                resultSet.getDate("date_fin"),
                Niveau.getByValue(resultSet.getString("niveau")),
                resultSet.getLong("id_formateur"),
                resultSet.getLong("atelier_id")

        );
    }
    public Formation mapFromResultSetWithAtelierAndFormateur(ResultSet resultSet) throws SQLException {
        return new Formation(
                resultSet.getLong("id"),
                resultSet.getString("nom"),
                resultSet.getDate("date_debut"),
                resultSet.getDate("date_fin"),
                Niveau.getByValue(resultSet.getString("niveau")),
                resultSet.getLong("id_formateur"),
                resultSet.getLong("atelier_id"),
                resultSet.getString("nom_atelier"),
                resultSet.getString("nom_formateur")
                );
    }


}