package com.tn.musego.utils.validator;

import com.tn.musego.utils.StringCnst;
import javafx.scene.control.Control;

/**
 * @author Skander Ben Fredj
 * @created 07-Mar-23
 * @project musego
 */

public class TextAreaValidator extends BaseValidator {

    public TextAreaValidator(Control field, String textError) {
        super(field, textError);
    }

    public TextAreaValidator(Control field) {
        super(field, StringCnst.champObligatoire);
    }
}
