package com.tn.musego.utils.validator;

import com.tn.musego.utils.StringCnst;
import javafx.scene.control.Control;

/**
 * @author Skander Ben Fredj
 * @created 06-Mar-23
 * @project musego
 */

public class DatepickerValidator extends BaseValidator {

    public DatepickerValidator(Control field, String textError) {
        super(field, textError);
    }

    public DatepickerValidator(Control field) {
        super(field, StringCnst.champObligatoire);
    }
}