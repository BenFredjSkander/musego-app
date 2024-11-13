package com.tn.musego.services;

import com.tn.musego.entities.Sponsor;

import java.util.List;

public interface ISponsorService {


    List<Sponsor> afficherSponsors();

    void modifierSponsor(Sponsor sponsor);

    void ajouterSponsor(Sponsor sponsor);

    void supprimerSponsor(int id);

}
