package com.tn.musego.services;

import com.tn.musego.entities.User;
import com.tn.musego.exceptions.MyCustomException;

import java.util.prefs.BackingStoreException;

public interface IAuthService {

    User login(String email, String password) throws MyCustomException;

    void signup(User user) throws MyCustomException;

    void checkEmailVerifCode(String token) throws MyCustomException;

    void destroyEmailVerifToken(String token);

    void requestResetPassword(String email) throws MyCustomException;

    void logout() throws BackingStoreException;

}
