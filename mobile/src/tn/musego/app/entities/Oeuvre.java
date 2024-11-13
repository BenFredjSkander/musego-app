package tn.musego.app.entities;

import tn.musego.app.utils.DateHelper;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class Oeuvre {

    private int id;
    private String titre;
    private String lieuConservation;
    private String description;
    private String image;
    private Categorie categorie;
    private Artiste artiste;
    private Date dateCreation;

    public Oeuvre() {
    }

    public Oeuvre(int id, String titre, String lieuConservation, String description, String image, Categorie categorie, Artiste artiste, Date dateCreation) {
        this.id = id;
        this.titre = titre;
        this.lieuConservation = lieuConservation;
        this.description = description;
        this.image = image;
        this.categorie = categorie;
        this.artiste = artiste;
        this.dateCreation = dateCreation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getLieuConservation() {
        return lieuConservation;
    }

    public void setLieuConservation(String lieuConservation) {
        this.lieuConservation = lieuConservation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Artiste getArtiste() {
        return artiste;
    }

    public void setArtiste(Artiste artiste) {
        this.artiste = artiste;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Oeuvre mapFromJson(Map<String, Object> map) {
        try {
            return new Oeuvre(
                    ((Double) map.get("id")).intValue(),
                    map.get("titre").toString(),
                    map.get("lieuConservation").toString(),
                    map.get("description").toString(),
                    map.get("image").toString(),
                    new Categorie().mapFromJson((Map<String, Object>) map.get("categorie")),
                    new Artiste().mapFromJson((Map<String, Object>) map.get("artiste")),
                    DateHelper.convertDateFormat(map.get("dateCreation").toString())
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


}
