package tn.musego.app.entities;

import java.util.Date;
import java.util.Map;

public class Atelier {
    private int id;

    private String nom;

    private String image;

    private Date createdAt;

    public Atelier() {
    }

//    public Atelier(String nom, String image) {
//        this.nom = nom;
//        this.image = image;
//    }

    public Atelier(int id, String nom, String image) {
        this.id = id;
        this.nom = nom;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public String toString() {
        return "Atelier{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", image='" + image +
                '}';
    }

    public Atelier mapFromJson(Map<String, Object> map) {
        return new Atelier(
                ((Double) map.get("id")).intValue(),
                map.get("nom").toString(),
                map.get("image").toString()
        );
    }
}
