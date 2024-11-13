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
public class Oeuvre {
    private int id;
    private String titre;
    private Date dateCreation;
   // private String categorie;
    private String description;
    private String lieuDeConservartion;
    private int idArtiste;
    private String artiste;
    private int categorie;
    private String categoriee;
    private String image;
    public Artiste artisteget;
    public Categorie categorieGet;

    //sans id:
    public Oeuvre(int id,String titre, Date dateCreation, int categorieNom, String description, String lieuDeConservartion,int idArtiste, String image) {
       this.id=id;
        this.titre = titre;
        this.dateCreation = dateCreation;
        this.categorie = categorieNom;
        this.description = description;
        this.lieuDeConservartion = lieuDeConservartion;
        this.idArtiste = idArtiste;
     //   this.artiste=nom;
        this.image = image;
    }

    public Oeuvre(int id,String titre, Date dateCreation, String description, String lieu, String image,String categorieNom,String nom) {
this.id=id;
        this.titre = titre;
        this.dateCreation = dateCreation;

        this.description = description;
        this.lieuDeConservartion = lieu;

        this.artiste=nom;

        this.categoriee = categorieNom;
        this.image = image;
        //this.idArtiste = idArtiste;


    }

    public Oeuvre mapFromResultSet(ResultSet result) throws SQLException {
        return new Oeuvre(result.getInt("id"),
                result.getString("titre"),
                result.getDate("date_creation"),
                result.getInt("id_categorie"),
                result.getString("description"),
                result.getString("lieu_conservation"),
                result.getInt("id_artiste"),
                result.getString("image"));
    }
    public Oeuvre mapFromResultSet2(ResultSet result) throws SQLException {
        return new Oeuvre(
                result.getInt("id"),
                result.getString("titre"),
                result.getDate("date_creation"),
                result.getString("description"),
                result.getString("lieu_conservation"),
                result.getString("image"),

                result.getString("nom"),
                result.getString("prenom"));

    }
}
