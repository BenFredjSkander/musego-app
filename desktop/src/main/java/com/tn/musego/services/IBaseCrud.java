package com.tn.musego.services;

import com.tn.musego.exceptions.MyCustomException;
import javafx.collections.ObservableList;

public interface IBaseCrud<T> {
    void createEntity(T t);

    T getEntityByID(Long id) throws MyCustomException;

    ObservableList<T> getAll();

    void updateEntity(T t) throws MyCustomException;

    void deleteEntityById(Long id);

}
