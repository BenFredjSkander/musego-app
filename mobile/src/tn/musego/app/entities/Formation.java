package tn.musego.app.entities;

import tn.musego.app.utils.DateHelper;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class Formation {
    private int id;

    private String nom;

    private Date dateDebut;

    private Date dateFin;

    private String niveau;
    private int idAtelier;
    private String nomAtelier;

    private int idFormateur;
    private String nomFormateur;


    public Formation() {
    }


    public Formation(int id, String nom, Date dateDebut, Date dateFin, String niveau, int idAtelier, String nomAtelier, int idFormateur, String nomFormateur) {
        this.id = id;
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.niveau = niveau;
        this.idAtelier = idAtelier;
        this.nomAtelier = nomAtelier;
        this.idFormateur = idFormateur;
        this.nomFormateur = nomFormateur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public int getIdAtelier() {
        return idAtelier;
    }

    public void setIdAtelier(int idAtelier) {
        this.idAtelier = idAtelier;
    }

    public String getNomAtelier() {
        return nomAtelier;
    }

    public void setNomAtelier(String nomAtelier) {
        this.nomAtelier = nomAtelier;
    }

    public int getIdFormateur() {
        return idFormateur;
    }

    public void setIdFormateur(int idFormateur) {
        this.idFormateur = idFormateur;
    }

    public String getNomFormateur() {
        return nomFormateur;
    }

    public void setNomFormateur(String nomFormateur) {
        this.nomFormateur = nomFormateur;
    }


    public Formation mapFromJson(Map<String, Object> map) {
        try {
            return new Formation(
                    ((Double) map.get("id")).intValue(),
                    map.get("nom").toString(),
                    DateHelper.convertDateFormat(map.get("dateDebut").toString()),
                    DateHelper.convertDateFormat(map.get("dateFin").toString()),
                    map.get("niveau").toString(),
                    ((Double) map.get("idAtelier")).intValue(),
                    map.get("nomAtelier").toString(),
                    ((Double) map.get("idFormateur")).intValue(),
                    map.get("nomFormateur").toString()
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}