package com.tn.musego.services;


import com.tn.musego.entities.Avis;

import java.util.List;

public interface IAvisService {
    public void ajouterAvis(Avis avis);

    public void modifierAvis(Avis a);

    public void supprimerAvis(int id);

    public List<Avis> afficherAvis();

    public List<Avis> rechercherAvis(int idUser);


}

