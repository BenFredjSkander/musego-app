package com.tn.musego.utils.validator;

import com.tn.musego.utils.StringCnst;
import javafx.scene.control.Control;

/**
 * @author Skander Ben Fredj
 * @created 26-Feb-23
 * @project musego
 */

public class EmailFieldValidator extends BaseValidator {

    public EmailFieldValidator(Control field, String textError) {
        super(field, textError);
    }

    public EmailFieldValidator(Control field) {
        super(field, StringCnst.invalidEmail);
    }

}
