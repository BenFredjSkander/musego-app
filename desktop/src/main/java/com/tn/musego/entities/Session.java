package com.tn.musego.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    private Long id;
    private Date createdDate;
    private Date expiry;

    private Long idUser;
    private String device;
    private String browser;
    private String uid;

    public Session(Long id, Long idUser, String device, String browser, String uid) {
        this.id = id;
        this.idUser = idUser;
        this.device = device;
        this.browser = browser;
        this.uid = uid;
    }

    public Session mapFromResultSet(ResultSet rs) throws SQLException {
        return new Session(rs.getLong("id"),
                rs.getLong("id_user"),
                rs.getString("device"),
                rs.getString("browser"),
                rs.getString("uid")
        );
    }

}
