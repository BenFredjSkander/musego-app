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
public class Atelier {
    private Long id;
    private String nom;
    private Date created_at;
    private String image;

    public Atelier(String nom, String image) {
        this.nom = nom;
        this.image = image;
    }

    public Atelier mapFromResultSet(ResultSet resultSet) throws SQLException {
        return new Atelier(
                resultSet.getLong("id"),
                resultSet.getString("nom"),
                resultSet.getDate("created_at"),
                resultSet.getString("image")
        );
    }




    @Override
    public String toString() {
        return "Atelier{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", created_at=" + created_at +
                ", image='" + image + '\'' +
                '}';
    }

}
