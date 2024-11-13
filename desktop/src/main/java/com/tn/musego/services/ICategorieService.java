package com.tn.musego.services;

import com.tn.musego.entities.Categorie;
import com.tn.musego.exceptions.MyCustomException;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface ICategorieService {
    ObservableList<Categorie> afficherCategories() ;

    Categorie afficherUnCategorie(Integer s) throws MyCustomException;
}
