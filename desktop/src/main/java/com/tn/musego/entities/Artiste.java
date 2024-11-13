package com.tn.musego.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artiste {
    private int id;
    private int cin;
    private String nom;
    private String prenom;
    private Date dateDeNaissance;
    private String adresse;
    private String specialite;
    private String email;
    private String nationalite;
    private String image;

    public Artiste(String nom, String prenom, int cin, String email, Date dateDeNaissance, String adresse, String specialite, String nationalite, String image) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.dateDeNaissance = dateDeNaissance;
        this.adresse = adresse;
        this.specialite = specialite;
        this.email = email;
        this.nationalite = nationalite;
        this.image = image;
    }

    public Artiste mapFromResultSet(ResultSet result) throws SQLException {
        return new Artiste(
                result.getInt("id"),
                result.getInt("cin"),
                result.getString("nom"),
                result.getString("prenom"),
                result.getDate("date_naissance"),
                result.getString("adresse"),
                result.getString("specialite"),
                result.getString("email"),
                result.getString("nationalite"),
                result.getString("image")
        );
    }
}
