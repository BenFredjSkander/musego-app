package com.tn.musego.services;

import com.tn.musego.entities.Abonnement;

public interface IAbonnementService extends IBaseCrud<Abonnement> {
    Abonnement getEntityByUserID(Long id);

    void deleteEntityByUserIdOffreId(Long userId, Long offreId);

    void deleteEntityByUserId(Long userId);

    boolean isUserSubscribed(long userId, long offreId);

    boolean haveActiveSubscription(Long userid);

    Abonnement getActiveSubscription(Long userid);

}
