package com.tn.musego.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Avis {

    private int id;

    private String type;
    private String description;

    private String avisSur;
    private int idUser;

    public Avis(String type, String description, String avisSur, int idUser) {
        this.type = type;
        this.description = description;
        this.avisSur = avisSur;
        this.idUser = idUser;
    }

    public Avis mapFromResultSet(ResultSet rs) throws SQLException {
        return new Avis(
                rs.getInt("id"),
                rs.getString("type"),
                rs.getString("description"),
                rs.getString("avis_sur"),
                rs.getInt("id_user")
        );
    }
}


