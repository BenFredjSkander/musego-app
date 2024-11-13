/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tn.musego.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author mahmo
 */
@Data
@NoArgsConstructor
public class Evenements {

    private long id;
    private String nom;
    private Date dateDebut;
    private Date dateFin;
    private String type;
    private String lieu;
    private String description;
    private String poster;
    private int nbParticipants;


    public Evenements(String nom, Date dateDebut, Date datefin, String type, String lieu, String description, String poster, int nbparticipants) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = datefin;
        this.type = type;
        this.lieu = lieu;
        this.description = description;
        this.poster = poster;
        this.nbParticipants = nbparticipants;
    }


    public Evenements(int id, String nom, Date dateDebut, Date datefin, String type, String lieu, String description, String poster, int nbparticipants) {
        this.id = id;
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = datefin;
        this.type = type;
        this.lieu = lieu;
        this.description = description;
        this.poster = poster;
        this.nbParticipants = nbparticipants;
    }

    public Evenements(String nom, Date dateDebut, Date dateFin, String type, String lieu, String description, int nbParticipants) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.type = type;
        this.lieu = lieu;
        this.description = description;
        this.nbParticipants = nbParticipants;

    }
}
