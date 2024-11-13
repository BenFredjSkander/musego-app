package com.tn.musego.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sponsor {

    private long id;
    private String nom;
    private String photo;
    private int capaciteFin;
    private int idEvenement;


    public Sponsor(String nom, String photo, int capaciteFin, int idEvenement) {
        this.nom = nom;
        this.photo = photo;
        this.capaciteFin = capaciteFin;
        this.idEvenement = idEvenement;
    }

}
