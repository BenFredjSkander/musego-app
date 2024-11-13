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
public class Attendance {
    private Long idUser;
    private Long idFormation;

    public Attendance mapFromResultset(ResultSet resultSet) throws SQLException {
        return new Attendance(resultSet.getLong("id_user"), resultSet.getLong("id_formation"));
    }
}
