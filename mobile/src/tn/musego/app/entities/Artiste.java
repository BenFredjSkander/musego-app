package tn.musego.app.entities;

import java.util.Date;
import java.util.Map;

public class Artiste {

    private int id;
    private int cin;
    private String nom;
    private String prenom;
    private String adresse;
    private String email;
    private String nationalite;
    private String specialite;
    private String image;
    private Date dateNaissance;

    public Artiste() {
    }


    public Artiste(int id, int cin, String nom, String prenom, String adresse, String email, String nationalite, String specialite, String image) {
        this.id = id;
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.email = email;
        this.nationalite = nationalite;
        this.specialite = specialite;
        this.image = image;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Artiste mapFromJson(Map<String, Object> map) {
        return new Artiste(
                ((Double) map.get("id")).intValue(),
                ((Double) map.get("cin")).intValue(),
                map.get("nom").toString(),
                map.get("prenom").toString(),
                map.get("adresse").toString(),
                map.get("email").toString(),
                map.get("nationalite").toString(),
                map.get("specialite").toString(),
                map.get("image").toString()
        );
    }

}
