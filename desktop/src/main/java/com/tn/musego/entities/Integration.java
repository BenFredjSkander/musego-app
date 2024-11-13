package com.tn.musego.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Integration {
    private int idUser;
    private int idFormation;

    public Integration mapFromResulSet(ResultSet resultSet) throws SQLException {
        return new Integration(resultSet.getInt("id_user"), resultSet.getInt("id_formation"));
    }


}
