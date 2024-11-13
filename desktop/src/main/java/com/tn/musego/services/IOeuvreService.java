package com.tn.musego.services;

import com.tn.musego.entities.Oeuvre;
import javafx.collections.ObservableList;

import java.util.List;

public interface IOeuvreService {

    void ajouterOeuvre(Oeuvre o);

    void modifierOeuvre(Oeuvre o);

    void supprimerOeuvre(int id);

    ObservableList<Oeuvre> afficherOeuvres();

    ObservableList<Oeuvre> afficherAllOeuvres();

    List<Oeuvre> afficherListOeuvres(String test);
}
