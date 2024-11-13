package com.tn.musego.services;

import com.tn.musego.entities.User;
import com.tn.musego.entities.enums.RoleEnum;
import com.tn.musego.exceptions.MyCustomException;
import javafx.collections.ObservableList;

public interface IUserService extends IExtendedCrud<User> {

    void lockUser(Long id);

    void changePassword(String email, String password);

    void enableUser(String email);

    Long getCurrentUserID();

    User getCurrentLoggedinUser() throws MyCustomException;

    ObservableList<User> getUsersByRole(RoleEnum role);

}
