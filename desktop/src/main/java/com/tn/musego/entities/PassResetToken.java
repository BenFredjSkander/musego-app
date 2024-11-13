package com.tn.musego.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Skander Ben Fredj
 * @created 26-Feb-23
 * @project musego
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassResetToken {


    private Long id;
    private Date createdDate;

    private String token;

    private boolean used = false;

    private Long idUser;

    public PassResetToken(Date createdDate, String token, Long idUser) {
        this.createdDate = createdDate;
        this.token = token;
        this.idUser = idUser;
    }
}
