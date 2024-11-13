package com.tn.musego.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailToken {

    private Long id;
    private Date createdDate;

    private String token;

    private boolean used = false;

    private Long idUser;

}
