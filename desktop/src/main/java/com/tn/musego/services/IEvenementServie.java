package com.tn.musego.services;

import com.tn.musego.entities.Evenements;

import java.util.List;

public interface IEvenementServie {

    List<Evenements> afficherEvenements(String what);

    void modifierEvenement(Evenements e);

    void ajouterEvenement(Evenements e);

    void supprimerEvenement(int id);

    boolean evenementExisteDeja(String nom);

    List<Evenements> listeEvenements();
}


