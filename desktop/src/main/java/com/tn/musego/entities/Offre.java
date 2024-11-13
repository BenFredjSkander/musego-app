package com.tn.musego.entities;

import com.tn.musego.entities.enums.TypeOffreEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Offre {
    private Long id;
    private TypeOffreEnum type;
    private String image;
    private Double prix;
    private Double promotion;
    private Date dateDebut;
    private Date dateFin;
    private String description;

    public Offre(TypeOffreEnum type,String image, Double prix, Double promotion, Date dateDebut, Date dateFin, String description) {
        this.type = type;
        this.image = image;
        this.prix = prix;
        this.promotion = promotion;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
    }

    public Offre mapFromResultSet(ResultSet resultSet) throws SQLException {
        return new Offre(
                resultSet.getLong("id"),
                TypeOffreEnum.getByValue(resultSet.getString("type")),
                resultSet.getString("image"),
                resultSet.getDouble("prix"),
                resultSet.getDouble("promotion"),
                resultSet.getDate("date_debut"),
                resultSet.getDate("date_fin"),
                resultSet.getString("description")
        );
    }
}
