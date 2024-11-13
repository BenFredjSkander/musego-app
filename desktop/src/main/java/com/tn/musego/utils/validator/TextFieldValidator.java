package com.tn.musego.utils.validator;

import com.tn.musego.utils.StringCnst;
import javafx.scene.control.Control;

/**
 * @author Skander Ben Fredj
 * @created 26-Feb-23
 * @project musego
 */

public class TextFieldValidator extends BaseValidator {

    public TextFieldValidator(Control field, String textError) {
        super(field, textError);
    }

    public TextFieldValidator(Control field) {
        super(field, StringCnst.champObligatoire);
    }
}
