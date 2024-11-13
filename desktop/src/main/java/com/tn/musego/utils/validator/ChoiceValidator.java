package com.tn.musego.utils.validator;

import com.tn.musego.utils.StringCnst;
import javafx.scene.control.Control;

/**
 * @author Skander Ben Fredj
 * @created 06-Mar-23
 * @project musego
 */

public class ChoiceValidator extends BaseValidator {

    public ChoiceValidator(Control field, String textError) {
        super(field, textError);
    }

    public ChoiceValidator(Control field) {
        super(field, StringCnst.champObligatoire);
    }

}