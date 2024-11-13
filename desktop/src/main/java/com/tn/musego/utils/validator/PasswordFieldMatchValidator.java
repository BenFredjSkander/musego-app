package com.tn.musego.utils.validator;

import com.tn.musego.utils.StringCnst;
import javafx.scene.control.Control;

/**
 * @author Skander Ben Fredj
 * @created 26-Feb-23
 * @project musego
 */
public class PasswordFieldMatchValidator extends BaseValidator {
    public Control field2;

    public PasswordFieldMatchValidator(Control field, Control field2, String textError) {
        super(field, textError);
        this.field2 = field2;
    }

    public PasswordFieldMatchValidator(Control field, Control field2) {
        super(field, StringCnst.champObligatoire);
        this.field2 = field2;
    }
}
