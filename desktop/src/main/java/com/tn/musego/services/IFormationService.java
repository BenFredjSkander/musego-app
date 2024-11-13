package com.tn.musego.services;

import com.tn.musego.entities.Formation;
import javafx.collections.ObservableList;

public interface IFormationService extends IBaseCrud<Formation> {

    ObservableList<Formation> getByAtelier(long atelier);
}
