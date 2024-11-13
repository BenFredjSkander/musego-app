package tn.musego.app.entities;

import tn.musego.app.utils.DateHelper;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class Evenement {

    private int id;
    private String nom;
    private Date dateDebut;
    private Date dateFin;
    private String type;
    private String lieu;
    private String description;
    private String poster;
    private int nb_participants;

    private Boolean participated;


//    public Evenement(String nom, Date dateDebut, Date datefin, String type, String lieu, String description, String poster, int nbparticipants, Boolean participated) {
//        this.nom = nom;
//        this.dateDebut = dateDebut;
//        this.dateFin = datefin;
//        this.type = type;
//        this.lieu = lieu;
//        this.description = description;
//        this.poster = poster;
//        this.nb_participants = nbparticipants;
//        this.participated = participated;
//    }


    public Evenement(int id, String nom, Date dateDebut, Date datefin, String type, String lieu, String description, String poster, int nbparticipants, Boolean participated) {
        this.id = id;
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = datefin;
        this.type = type;
        this.lieu = lieu;
        this.description = description;
        this.poster = poster;
        this.nb_participants = nbparticipants;
        this.participated = participated;
    }


    public Evenement() {

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public int getNb_participants() {
        return nb_participants;
    }

    public void setNb_participants(int nb_participants) {
        this.nb_participants = nb_participants;
    }

    public Boolean getParticipated() {
        return participated;
    }

    public void setParticipated(Boolean participated) {
        this.participated = participated;
    }

    public Evenement parseFromJson(Map<String, Object> map) {
        try {
            return new Evenement(
                    ((Double) map.get("id")).intValue(),
                    map.get("nom").toString(),
                    DateHelper.convertDateFormat((map.get("date_debut").toString())),
                    DateHelper.convertDateFormat((map.get("date_fin").toString())),
                    map.get("type").toString(),
                    map.get("lieu").toString(),
                    map.get("description").toString(),
                    map.get("poster").toString(),
                    ((Double) map.get("nbParticipants")).intValue(),
                    Boolean.parseBoolean((String) map.get("participated"))
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", type='" + type + '\'' +
                ", lieu='" + lieu + '\'' +
                ", description='" + description + '\'' +
                ", poster='" + poster + '\'' +
                ", nb_participants=" + nb_participants +
                ", participated=" + participated +
                '}';
    }
}
