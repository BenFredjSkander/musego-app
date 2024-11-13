package tn.musego.app.entities;

import java.util.Map;

public class Categorie {
    private int id;
    private String nom;
    private String image;
    private String description;

    public Categorie() {
    }

    public Categorie(int id, String nom, String image, String description) {
        this.id = id;
        this.nom = nom;
        this.image = image;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public Categorie mapFromJson(Map<String, Object> map) {
        System.out.println("categorie " + map);
        return new Categorie(
                ((Double) map.get("id")).intValue(),
                (String) map.get("nom"),
                (String) map.get("image"),
                (String) map.get("description")
        );
    }
}
