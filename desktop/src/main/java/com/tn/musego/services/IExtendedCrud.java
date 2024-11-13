package com.tn.musego.services;

import com.tn.musego.exceptions.MyCustomException;
import javafx.collections.ObservableList;

import java.util.Map;

/**
 * @author Skander Ben Fredj
 * @created 21-Feb-23
 * @project musego
 */

public interface IExtendedCrud<T> extends IBaseCrud<T> {
    T getEntityByCriteriaSingle(Map<String, Object> criteriaMap) throws MyCustomException;

    ObservableList<T> getEntityByCriteriaList(Map<String, Object> criteriaMap);
}
