package com.tn.musego.entities;

import com.tn.musego.entities.enums.TypeAbEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Abonnement {
    private Long id;
    private TypeAbEnum type;
    private Double prix;
    private Date dateDebut;
    private Date dateFin;
    private Long idOffre;
    private Long idUser;

    public Abonnement(TypeAbEnum type, Double prix, Date dateDebut, Date dateFin, Long idOffre, Long idUser) {
        this.type = type;
        this.prix = prix;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.idOffre = idOffre;
        this.idUser = idUser;
    }

    public Abonnement mapFromResultSet(ResultSet resultSet) throws SQLException {
        return new Abonnement(
                resultSet.getLong("id"),
                TypeAbEnum.getByValue(resultSet.getString("type")),
                resultSet.getDouble("prix"),
                resultSet.getDate("date_debut"),
                resultSet.getDate("date_fin"),
                resultSet.getLong("id_offre"),
                resultSet.getLong("id_user")
        );
    }
}
