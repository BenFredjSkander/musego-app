package tn.musego.app.entities;

import tn.musego.app.utils.DateHelper;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class Abonnement {

    private int id;
    private int idoffre;
    private String type;
    private float prix;
    private Date dateDebut;
    private Date dateFin;
    private String user;
    private String offre;

    public Abonnement(int id, int idoffre, String type, float prix, Date dateDebut, Date dateFin, String user, String offre) {
        this.id = id;
        this.idoffre = idoffre;
        this.type = type;
        this.prix = prix;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.user = user;
        this.offre = offre;
    }

    public Abonnement(String type, float prix, Date dateDebut, Date dateFin, String user, String offre) {
        this.type = type;
        this.prix = prix;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.user = user;
        this.offre = offre;
    }

    public Abonnement() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdoffre() {
        return idoffre;
    }

    public void setIdoffre(int idoffre) {
        this.idoffre = idoffre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOffre() {
        return offre;
    }

    public void setOffre(String offre) {
        this.offre = offre;
    }

    @Override
    public String toString() {
        return "Abonnement{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", prix=" + prix +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", user='" + user + '\'' +
                ", offre='" + offre + '\'' +
                ", idoffre=" + idoffre +
                '}';
    }


    public Abonnement mapFromMap(Map<String, Object> map) {
        try {
            return new Abonnement(((Double) map.get("id")).intValue(),
                    ((Double) map.get("idoffre")).intValue(),
                    map.get("type").toString(),
                    Float.parseFloat(map.get("prix").toString()),
                    DateHelper.convertDateFormat(map.get("dateDebut").toString()),
                    DateHelper.convertDateFormat(map.get("dateFin").toString()),
                    map.get("user").toString(),
                    map.get("offre").toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
