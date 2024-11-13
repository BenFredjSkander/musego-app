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
public class Categorie {
    private int id;
    private  String image;
    private String nom;
    private String description;
    public Categorie(String nom, String description, String image) {

        this.nom = nom;
        this.description = description;

        this.image = image;
    }

    public Categorie(String nom) {
        this.nom=nom;
    }

    public Categorie mapFromResultSet(ResultSet result) throws SQLException {
        return new Categorie(

result.getInt("id"),
                result.getString("image"),
                result.getString("nom"),


                result.getString("description")


        );

    }
    public Categorie mapFromResultSet2(ResultSet result) throws SQLException {
        return new Categorie(


                result.getString("nom"));


               // result.getString("description")


        }

}
