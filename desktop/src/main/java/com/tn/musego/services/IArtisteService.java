package com.tn.musego.services;

import com.tn.musego.entities.Artiste;
import com.tn.musego.exceptions.MyCustomException;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface IArtisteService {
    void ajouterArtiste(Artiste a) throws SQLException;

    void modifierArtiste(Artiste a);

    void supprimerArtiste(int id);

    ObservableList<Artiste> afficherArtistes();

    Artiste afficherUnArtiste(Integer s) throws MyCustomException;
}
