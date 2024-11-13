package tn.musego.app.entities;

public class Avis {
    private Long id;
    private String type, description, avisSur;

    public Avis() {
    }

    public Avis(Long id, String type, String description, String avisSur) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.avisSur = avisSur;
    }

    public Avis(String type, String description, String avisSur) {
        this.type = type;
        this.description = description;
        this.avisSur = avisSur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvisSur() {
        return avisSur;
    }

    public void setAvisSur(String avisSur) {
        this.avisSur = avisSur;
    }
}
