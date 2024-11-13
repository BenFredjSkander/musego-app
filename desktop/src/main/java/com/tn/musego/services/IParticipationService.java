package com.tn.musego.services;

import com.tn.musego.entities.Participation;

import java.util.List;

public interface IParticipationService {
    void ajouterParticipation(int idUser, int idEvenement);

    void supprimerParticipation(int idUser, int idEvenement);

    List<Participation> afficherParticipations();

    List<String> afficherParticipationsbyid(int id);

    boolean isUserAlreadyRegistered(int userId, int eventId);
}
