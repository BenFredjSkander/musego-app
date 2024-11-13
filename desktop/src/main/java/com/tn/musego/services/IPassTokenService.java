package com.tn.musego.services;

import com.tn.musego.entities.PassResetToken;
import com.tn.musego.exceptions.MyCustomException;

/**
 * @author Skander Ben Fredj
 * @created 26-Feb-23
 * @project musego
 */

public interface IPassTokenService {
    void addToken(PassResetToken resetToken) throws MyCustomException;

    void tokenExist(String token) throws MyCustomException;

    void destroyToken(String token) throws MyCustomException;
}
