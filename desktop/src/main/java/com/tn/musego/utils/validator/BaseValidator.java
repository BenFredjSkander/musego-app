package com.tn.musego.utils.validator;

import javafx.scene.control.Control;

/**
 * @author Skander Ben Fredj
 * @created 26-Feb-23
 * @project musego
 */
public class BaseValidator {

    public Control field;
    public String textError;

    public int maxLength;

    public BaseValidator(Control field, String textError) {
        this.field = field;
        this.textError = textError;
    }

    public BaseValidator(Control field, String textError, int maxLength) {
        this.field = field;
        this.textError = textError;
        this.maxLength = maxLength;
    }
}
