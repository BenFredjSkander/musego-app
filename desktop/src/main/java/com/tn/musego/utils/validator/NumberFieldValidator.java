package com.tn.musego.utils.validator;

import com.tn.musego.utils.StringCnst;
import javafx.scene.control.Control;

/**
 * @author Skander Ben Fredj
 * @created 03-Mar-23
 * @project musego
 */

public class NumberFieldValidator extends BaseValidator {
    public NumberFieldValidator(Control field, String textError) {
        super(field, textError);
    }

    public NumberFieldValidator(Control field, String textError, int length) {
        super(field, textError, length);
    }

    public NumberFieldValidator(Control field) {
        super(field, StringCnst.champObligatoire);
    }

    public NumberFieldValidator(Control field, int length) {
        super(field, StringCnst.champObligatoire, length);
    }
}
